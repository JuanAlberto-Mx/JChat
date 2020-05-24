package com.client;

import com.config.Configurator;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
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
        this.setTitle(Configurator.getInstance().getProperty("clientName"));
        this.setVisible(true);        
                
        playSound("bubble_wav.wav");
        
        lblStatus.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titlePanel = new javax.swing.JPanel();
        lblApp = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        txtConversation = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChatUp!");
        setIconImage(getIcon().getImage());
        setMaximumSize(new java.awt.Dimension(600, 600));
        setMinimumSize(new java.awt.Dimension(600, 600));
        setName("frmClient"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        titlePanel.setBackground(new java.awt.Color(63, 191, 19));
        titlePanel.setMaximumSize(null);
        titlePanel.setName("titlePanel"); // NOI18N
        titlePanel.setPreferredSize(new java.awt.Dimension(600, 50));
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
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(600, 550));
        mainPanel.setLayout(new java.awt.GridBagLayout());

        scroll.setName("scroll"); // NOI18N

        txtConversation.setEditable(false);
        txtConversation.setColumns(20);
        txtConversation.setRows(5);
        txtConversation.setName("txtConversation"); // NOI18N
        scroll.setViewportView(txtConversation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 337;
        gridBagConstraints.ipady = 277;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 14);
        mainPanel.add(scroll, gridBagConstraints);

        txtMessage.setEditable(false);
        txtMessage.setName("txtMessage"); // NOI18N
        txtMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMessageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 354;
        gridBagConstraints.ipady = 50;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 14);
        mainPanel.add(txtMessage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 260;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 11, 0);
        mainPanel.add(lblStatus, gridBagConstraints);

        btnSend.setText("Send");
        btnSend.setName("btnSend"); // NOI18N
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 11, 14);
        mainPanel.add(btnSend, gridBagConstraints);

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

    private ImageIcon getIcon() {
        URL iconURL = getClass().getResource("/com/res/img/icon_title.png");
        
        return new ImageIcon(iconURL);        
    }
    
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
                    lblStatus.setText(Configurator.getInstance().getProperty("connected") + " " + connection.getInetAddress().getHostName());
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
            JOptionPane.showMessageDialog(null, Configurator.getInstance().getProperty("serverDown"), Configurator.getInstance().getProperty("warning"), JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sendMessage(String message) {
        output.println(message);
    }
    
    private String getUserName() {
        return JOptionPane.showInputDialog(this, Configurator.getInstance().getProperty("userName"), Configurator.getInstance().getProperty("userTitle"), JOptionPane.DEFAULT_OPTION);
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
        Configurator.getInstance().setFile("en_config");
        
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