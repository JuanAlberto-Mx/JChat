package com.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer extends javax.swing.JFrame {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private ServerSocket server;
    private int totalClients = 100;
    private int port = 6789;

    public MainServer() {

        initComponents();
        this.setTitle("Server");
        this.setVisible(true);
        lblStatus.setVisible(true);
    }

    public void startRunning() {
        try {
            server = new ServerSocket(port, totalClients);
            while (true) {
                try {
                    lblStatus.setText(" Waiting for Someone to Connect...");
                    connection = server.accept();
                    lblStatus.setText(" Now Connected to " + connection.getInetAddress().getHostName());

                    output = new ObjectOutputStream(connection.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(connection.getInputStream());

                    whileChatting();

                }
                catch (EOFException eofException) {
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void whileChatting() throws IOException {
        String message = "";
        txtMessage.setEditable(true);
        do {
            try {
                message = (String) input.readObject();
                txtConversation.append("\n" + message);
            }
            catch (ClassNotFoundException classNotFoundException) {

            }
        }
        while (!message.equals("Client - END"));
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
        setName("frmServer"); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 500));

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

        txtConversation.setColumns(20);
        txtConversation.setRows(5);
        txtConversation.setName("txtConversation"); // NOI18N
        scroll.setViewportView(txtConversation);

        mainPanel.add(scroll);
        scroll.setBounds(20, 10, 360, 300);

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

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed

        sendMessage(txtMessage.getText());
        txtMessage.setText("");
    }//GEN-LAST:event_btnSendActionPerformed

    private void txtMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMessageActionPerformed

        sendMessage(txtMessage.getText());
        txtMessage.setText("");
    }//GEN-LAST:event_txtMessageActionPerformed

    private void sendMessage(String message) {
        try {
            output.writeObject("Server - " + message);
            output.flush();
            txtConversation.append("\nServer - " + message);
        }
        catch (IOException ioException) {
            txtConversation.append("\n Unable to Send Message");
        }
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
