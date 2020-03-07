package com.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Client extends javax.swing.JFrame {
    private Scanner input;
    private PrintWriter output;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private int port = 59001;
    private boolean minimized;

    public Client(String serverIP) {
        initComponents();

        this.serverIP = serverIP;
        this.setTitle("ChatUp! Messenger v0.1");
        this.setVisible(true);        
                
        playSound("bubble_wav.wav");
        
        lblStatus.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlePanel = new javax.swing.JPanel();
        lblApp = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        txtConversation = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JChat");
        setMaximumSize(new java.awt.Dimension(400, 500));
        setMinimumSize(new java.awt.Dimension(400, 500));
        setName("frmClient"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        titlePanel.setBackground(new java.awt.Color(63, 191, 19));
        titlePanel.setMaximumSize(new java.awt.Dimension(400, 50));
        titlePanel.setMinimumSize(new java.awt.Dimension(400, 50));
        titlePanel.setName("titlePanel"); // NOI18N
        titlePanel.setPreferredSize(new java.awt.Dimension(400, 50));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        titlePanel.setLayout(flowLayout1);

        lblApp.setFont(new java.awt.Font("Ubuntu", 1, 20)); // NOI18N
        lblApp.setForeground(java.awt.Color.white);
        lblApp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblApp.setText("ChatUp!");
        lblApp.setName("lblApp"); // NOI18N
        titlePanel.add(lblApp);

        getContentPane().add(titlePanel, java.awt.BorderLayout.NORTH);

        mainPanel.setBackground(java.awt.Color.white);
        mainPanel.setMaximumSize(new java.awt.Dimension(400, 450));
        mainPanel.setMinimumSize(new java.awt.Dimension(400, 450));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(400, 450));
        mainPanel.setLayout(null);

        scroll.setName("scroll"); // NOI18N

        txtConversation.setEditable(false);
        txtConversation.setColumns(20);
        txtConversation.setRows(5);
        txtConversation.setName("txtConversation"); // NOI18N
        scroll.setViewportView(txtConversation);

        mainPanel.add(scroll);
        scroll.setBounds(20, 10, 360, 300);

        txtMessage.setEditable(false);
        txtMessage.setName("txtMessage"); // NOI18N
        txtMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMessageActionPerformed(evt);
            }
        });
        mainPanel.add(txtMessage);
        txtMessage.setBounds(20, 320, 360, 70);

        btnSend.setText("Send");
        btnSend.setName("btnSend"); // NOI18N
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });
        mainPanel.add(btnSend);
        btnSend.setBounds(300, 400, 80, 30);
        mainPanel.add(lblStatus);
        lblStatus.setBounds(20, 400, 260, 30);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(410, 530));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMessageActionPerformed
        sendMessage(txtMessage.getText());
        
        txtMessage.setText("");
    }//GEN-LAST:event_txtMessageActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        sendMessage(txtMessage.getText());
        
        txtMessage.setText("");
    }//GEN-LAST:event_btnSendActionPerformed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        minimized = true;
    }//GEN-LAST:event_formWindowIconified

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        minimized = false;
    }//GEN-LAST:event_formWindowDeiconified

    public void startRunning() {
        try {            
            connection = new Socket(serverIP, port);
            
            input = new Scanner(connection.getInputStream());
            output = new PrintWriter(connection.getOutputStream(), true);
            
            while (input.hasNextLine()) {
                String line = input.nextLine();
                
                if(line.startsWith("SubmitName")) {
                    output.println(getUserName());
                }
                else if(line.startsWith("NameAccepted")) {
                    this.setTitle("ChatUp! Messenger v0.1");
                    lblStatus.setText("Connected to: " + connection.getInetAddress().getHostName());
                    lblApp.setText(line.substring(13));
                    txtMessage.setEditable(true);
                }
                else if (line.startsWith("Message")) {
                    txtConversation.append(line.substring(8) + "\n");
                    
                    if(minimized) {                        
                        playSound("sms_alert_wav.wav");
                    }
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Server might be down!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sendMessage(String message) {
        output.println(message);
    }
    
    private String getUserName() {
        return JOptionPane.showInputDialog(this, "Enter a user name", "User name selection", JOptionPane.DEFAULT_OPTION);
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
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        new Client("10.156.30.207").startRunning();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel lblApp;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextArea txtConversation;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}