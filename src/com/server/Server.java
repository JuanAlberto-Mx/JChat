package com.server;

import com.client.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Server {
    private static Set<String> clientNames = new HashSet<>();

    private static Set<PrintWriter> printWriters = new HashSet<>();
        
    private static class ClientHandler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SubmitName");
                    name = in.nextLine();
                    
                    if (name == null)
                        return;
                    
                    synchronized (clientNames) {
                        if (!name.isEmpty() && !clientNames.contains(name)) {
                            clientNames.add(name);
                            break;
                        }
                    }
                }

                out.println("NameAccepted " + name);
                
                printWriters.forEach((writer) -> {
                    writer.println("Message " + name + " has joined the chat");
                    playSound("elevator_ding_wav.wav");
                });
                
                printWriters.add(out);

                while (true) {
                    String input = in.nextLine();
                    
                    if (input.toLowerCase().startsWith("/quit"))
                        return;                    
                    
                    printWriters.forEach((writer) -> {
                        writer.println("Message " + name + ": " + input);
                    });
                }
            } 
            catch (IOException e) {
                System.out.println(e);
            } 
            finally {
                if (out != null)
                    printWriters.remove(out);
                
                if (name != null) {
                    System.out.println(name + " is leaving");
                    
                    clientNames.remove(name);
                    
                    for (PrintWriter writer : printWriters) {
                        writer.println("Message " + name + " has left");
                        playSound("airplane_ding_wav.wav");
                    }
                }
                try {
                    socket.close();
                } 
                catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {        
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Client.class.getResourceAsStream("/com/res/sound/" + url));
                    clip.open(inputStream);
                    clip.start(); 
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Chat server is running...");
        
        ExecutorService pool = Executors.newFixedThreadPool(500);
        
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true)
                pool.execute(new ClientHandler(listener.accept()));            
        }
    }    
}