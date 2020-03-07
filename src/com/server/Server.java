package com.server;

import com.client.Client;
import com.config.Configurator;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.ResourceBundle;
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
    private static Set<String> clients = new HashSet<>();
    private static Set<PrintWriter> printWriters = new HashSet<>();
        
    private static class ClientHandler implements Runnable {
        private String userName;
        private Socket socket;
        private Scanner input;
        private PrintWriter output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    output.println("SubmitName");
                    userName = input.nextLine();
                    
                    if (userName == null)
                        return;
                    
                    synchronized (clients) {
                        if (!userName.isEmpty() && !clients.contains(userName)) {
                            clients.add(userName);
                            break;
                        }
                    }
                }

                output.println("NameAccepted " + userName);
                
                printWriters.forEach((writer) -> {
                    writer.println("Message " + userName + " has joined the chat");
                    playSound("elevator_ding_wav.wav");
                });
                
                printWriters.add(output);

                while (true) {
                    String inputLine = input.nextLine();
                    
                    if (inputLine.toLowerCase().startsWith("/quit"))
                        return;                    
                    
                    printWriters.forEach((writer) -> {
                        writer.println("Message " + userName + ": " + inputLine);
                    });
                }
            } 
            catch (IOException e) {
                System.out.println(e);
            } 
            finally {
                if (output != null)
                    printWriters.remove(output);
                
                if (userName != null) {
                    System.out.println(userName + " is leaving");
                    
                    clients.remove(userName);
                    
                    for (PrintWriter writer : printWriters) {
                        writer.println("Message " + userName + " has left");
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
        Configurator.getInstance().setFile("config");
        
        System.out.println("Chat server is running...");
        
        ExecutorService pool = Executors.newFixedThreadPool(500);
        
        try (ServerSocket listener = new ServerSocket(59001)) {                        
            while (true)
                pool.execute(new ClientHandler(listener.accept()));            
        }
    }    
}