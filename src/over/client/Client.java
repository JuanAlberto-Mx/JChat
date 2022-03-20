package over.client;

import over.config.Configurator;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

/**
 * <code>Client</code>.
 *
 * @author Overload Inc.
 * @version 1.0, 03 Jun 2020
 */
public class Client extends JFrame {
    private JButton btnSend;
    private JLabel lblApp;
    private JLabel lblStatus;
    private JPanel mainPanel;
    private JScrollPane scroll;
    private JPanel titlePanel;
    private JTextArea txtConversation;
    private JTextField txtMessage;
    private Scanner input;
    private PrintWriter output;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private int port = 59001;
    private boolean minimized;

    /**
     * 
     * @param serverIP
     */
    public Client(String serverIP) {
        initComponents();
        
        this.serverIP = serverIP;
        this.setTitle(Configurator.getInstance().getProperty("clientName"));
        this.setVisible(true);        
                
        playSound("bubble_wav.wav");
        
        lblStatus.setVisible(true);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        titlePanel = new JPanel();
        lblApp = new JLabel();
        mainPanel = new JPanel();
        scroll = new JScrollPane();
        txtConversation = new JTextArea();
        txtMessage = new JTextField();
        lblStatus = new JLabel();
        btnSend = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChatUp!");
        setIconImage(getIcon().getImage());
        setMaximumSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(600, 600));
        setName("frmClient"); 
        addWindowListener(new WindowAdapter() {
            public void windowDeiconified(WindowEvent evt) {
                formWindowDeiconified(evt);
            }
            public void windowIconified(WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        titlePanel.setBackground(new Color(63, 191, 19));
        titlePanel.setMaximumSize(null);
        titlePanel.setName("titlePanel"); 
        titlePanel.setPreferredSize(new Dimension(600, 50));
        FlowLayout flowLayout1 = new FlowLayout(FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        titlePanel.setLayout(flowLayout1);

        lblApp.setFont(new Font("Ubuntu", 1, 20)); 
        lblApp.setForeground(Color.white);
        lblApp.setHorizontalAlignment(SwingConstants.LEFT);
        lblApp.setText("ChatUp!");
        lblApp.setName("lblApp"); 
        titlePanel.add(lblApp);

        getContentPane().add(titlePanel, BorderLayout.NORTH);

        mainPanel.setBackground(Color.white);
        mainPanel.setName("mainPanel"); 
        mainPanel.setPreferredSize(new Dimension(600, 550));
        mainPanel.setLayout(new GridBagLayout());

        scroll.setName("scroll"); 

        txtConversation.setEditable(false);
        txtConversation.setColumns(20);
        txtConversation.setRows(5);
        txtConversation.setName("txtConversation"); 
        scroll.setViewportView(txtConversation);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 337;
        gridBagConstraints.ipady = 277;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 20, 0, 14);
        mainPanel.add(scroll, gridBagConstraints);

        txtMessage.setEditable(false);
        txtMessage.setName("txtMessage"); 
        txtMessage.addActionListener(evt -> txtMessageActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 354;
        gridBagConstraints.ipady = 50;
        gridBagConstraints.insets = new Insets(10, 20, 0, 14);
        mainPanel.add(txtMessage, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 260;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.insets = new Insets(10, 20, 11, 0);
        mainPanel.add(lblStatus, gridBagConstraints);

        btnSend.setText("Send");
        btnSend.setName("btnSend"); 
        btnSend.addActionListener(evt -> btnSendActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 20, 11, 14);
        mainPanel.add(btnSend, gridBagConstraints);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        setSize(new Dimension(410, 530));
        setLocationRelativeTo(null);
    }

    /**
     *
     * @param evt
     */
    private void txtMessageActionPerformed(ActionEvent evt) {
        sendMessage(txtMessage.getText());
        txtMessage.setText("");
    }

    /**
     *
     * @param evt
     */
    private void btnSendActionPerformed(ActionEvent evt) {
        sendMessage(txtMessage.getText());
        txtMessage.setText("");
    }

    /**
     *
     * @param evt
     */
    private void formWindowIconified(WindowEvent evt) {
        minimized = true;
    }

    /**
     *
     * @param evt
     */
    private void formWindowDeiconified(WindowEvent evt) {
        minimized = false;
    }

    /**
     *
     * @return
     */
    private ImageIcon getIcon() {
        URL iconURL = getClass().getResource("/com/res/img/icon_title.png");
        
        return new ImageIcon(iconURL);        
    }

    /**
     *
     */
    public void startRunning() {
        try {            
            connection = new Socket(serverIP, port);
            input = new Scanner(connection.getInputStream());
            output = new PrintWriter(connection.getOutputStream(), true);
            
            while (input.hasNextLine()) {
                String line = input.nextLine();
                
                if(line.startsWith("SubmitName"))
                    output.println(getUserName());
                else if(line.startsWith("NameAccepted")) {                    
                    lblStatus.setText(Configurator.getInstance().getProperty("connected") + " " + connection.getInetAddress().getHostName());
                    lblApp.setText(line.substring(13));
                    txtMessage.setEditable(true);
                }
                else if (line.startsWith("Message")) {
                    txtConversation.append(line.substring(8) + "\n");
                    
                    if(minimized)
                        playSound("sms_alert_wav.wav");
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, Configurator.getInstance().getProperty("serverDown"), Configurator.getInstance().getProperty("warning"), JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * @param message
     */
    private void sendMessage(String message) {
        output.println(message);
    }

    /**
     *
     * @return
     */
    private String getUserName() {
        return JOptionPane.showInputDialog(this, Configurator.getInstance().getProperty("userName"), Configurator.getInstance().getProperty("userTitle"), JOptionPane.DEFAULT_OPTION);
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
     */
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
}