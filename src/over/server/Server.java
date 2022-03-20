package over.server;

import over.client.Client;
import over.config.Configurator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <code>Server</code>.
 *
 * @author Overload Inc.
 * @version 1.0, 03 Jun 2020
 */
public class Server {
    private static Set<String> clients = new HashSet<>();
    private static Set<PrintWriter> printWriters = new HashSet<>();

    /**
     * <code>ClientHandler</code>.
     */
    private static class ClientHandler implements Runnable {
        private String userName;
        private Socket socket;
        private Scanner input;
        private PrintWriter output;

        /**
         *
         * @param socket
         */
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
                    ex.getMessage();
                }
            }
        }
    }

    /**
     *
     * @param url
     */
    public static synchronized void playSound(final String url) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                Client.class.getResourceAsStream("/over/res/sound/" + url));
                clip.open(inputStream);
                clip.start();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Configurator.getInstance().setFile("en_config");
        
        System.out.println(Configurator.getInstance().getProperty("serverRunning"));
        
        ExecutorService pool = Executors.newFixedThreadPool(500);
        
        try (ServerSocket listener = new ServerSocket(59001)) {                        
            while (true)
                pool.execute(new ClientHandler(listener.accept()));            
        }
    }    
}