package com.raven.component;

import com.raven.app.MessageType;
import com.raven.event.PublicEvent;
import com.raven.model.Model_Send_Message;
import com.raven.model.Model_User_Account;
import com.raven.service.Service;
import com.raven.swing.JIMSendTextPane;
import com.raven.swing.ScrollBar;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Chat_Bottom extends javax.swing.JPanel {

    private Model_User_Account user;

    private Map<String, List<Model_Send_Message>> messageMap = new HashMap<>();
     private JTextArea messageTextArea;  // Declare the JTextArea component


    public Model_User_Account getUser() {
        return user;
    }
    

    public void setUser(Model_User_Account user) {
        this.user = user;
        panelMore.setUser(user);
    }
     private JButton encryptButton;  // Declare the Encrypt button

    //  private static final String AES_KEY = "128"; // Replace with your encryption key
    public Chat_Bottom() {
        initComponents();
        init();
    }

    private void init() {
        mig = new MigLayout("fillx, filly", "0[fill]0[]0[]2", "2[fill]2[]0");
        setLayout(mig);
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(null);
        JIMSendTextPane txt = new JIMSendTextPane();
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                refresh();
                if (ke.getKeyChar() == 10 && ke.isControlDown()) {
                    //  user press controll + enter
                    eventSend(txt);
                }
            }
        });
        txt.setBorder(new EmptyBorder(5, 5, 5, 5));
        txt.setHintText("Write Message Here ...");
        scroll.setViewportView(txt);
        ScrollBar sb = new ScrollBar();
        sb.setBackground(new Color(229, 229, 229));
        sb.setPreferredSize(new Dimension(2, 10));
        scroll.setVerticalScrollBar(sb);
        add(sb);
        add(scroll, "w 100%");
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("filly", "0[]3[]0", "0[bottom]0"));
        panel.setPreferredSize(new Dimension(30, 28));
        panel.setBackground(Color.WHITE);
        JButton cmd = new JButton();
        cmd.setBorder(null);
        cmd.setContentAreaFilled(false);
        cmd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmd.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/send.png")));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                eventSend(txt);
            }
        });
        JButton cmdMore = new JButton();
        cmdMore.setBorder(null);
        cmdMore.setContentAreaFilled(false);
        cmdMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more_disable.png")));
        cmdMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (panelMore.isVisible()) {
                    cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more_disable.png")));
                    panelMore.setVisible(false);
                    mig.setComponentConstraints(panelMore, "dock south,h 0!");
                    revalidate();
                } else {
                    cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more.png")));
                    panelMore.setVisible(true);
                    mig.setComponentConstraints(panelMore, "dock south,h 170!");
                    revalidate();
                }
            }
        });
        panel.add(cmdMore);
        panel.add(cmd);
        add(panel, "wrap");
        panelMore = new Panel_More();
        panelMore.setVisible(false);
        add(panelMore, "dock south,h 0!");  //  set height 0
        
            
        panel.setLayout(new MigLayout("filly", "0[]3[]3[]0", "0[bottom]0"));
        panel.setPreferredSize(new Dimension(30, 28));
        panel.setBackground(Color.WHITE);

        // Add the Encrypt button
        encryptButton = new JButton();
        encryptButton.setBorder(null);
        encryptButton.setContentAreaFilled(false);
        encryptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        encryptButton.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/encrypt.png")));
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                eventEncrypt();
            }
        });
        panel.add(encryptButton);

       
        // ... Existing code ...

        panel.add(cmdMore);
        panel.add(cmd);
        add(panel, "wrap");
    }

        private void eventEncrypt() {
        // Handle the logic for encryption here
        // For example:
        String textToEncrypt = messageTextArea.getText();
        String encryptedText = encryptText(textToEncrypt);
        messageTextArea.setText(encryptedText);
    }

    private String encryptText(String plainText) {
        // Replace with your encryption logic
        // ...
        return ""; // Return the encrypted text
    }
    
    
    
    
    
    private void eventSend(JIMSendTextPane txt) {

        String text = txt.getText().trim();
        if (!text.equals("")) {
            Model_Send_Message message = new Model_Send_Message(MessageType.TEXT, Service.getInstance().getUser().getUserID(), user.getUserID(), text);
            send(message);
            PublicEvent.getInstance().getEventChat().sendMessage(message);
            txt.setText("");
            txt.grabFocus();
            refresh();
            updateMessageTime(); // Call the method to update message time

            // Store the sent message in the messageMap
            int recipientUserId = user.getUserID();
            String recipientUserIdString = String.valueOf(recipientUserId); // Convert int to String
            List<Model_Send_Message> messageList = messageMap.getOrDefault(recipientUserIdString, new ArrayList<>());
            messageList.add(message);
            messageMap.put(recipientUserIdString, messageList);
        } else {
            txt.grabFocus();
        }

    }

  
    //update time section
    private void updateMessageTime() {
        // Get the current time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTime = sdf.format(new Date());

        // Set the current time to the message component(s)
        // Replace `yourMessageComponent` with the actual component(s) used to display the message time
        // For example:
        // yourMessageComponent.setTime(currentTime);
    }

    public void displayMessagesForUser(Model_User_Account user) {
        int recipientUserId = user.getUserID();
        List<Model_Send_Message> messageList = messageMap.get(recipientUserId);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Update your UI component(s) to display the messages for the selected user
                // For example, if you have a JTextArea named 'messageTextArea':
                 messageTextArea.setText(""); // Clear the existing text

                if (messageList.isEmpty()) {
                    // Handle the case when there are no previous messages
                    // For example:
                     messageTextArea.setText("No previous messages");
                } else {
                    // Display the previous messages
                    // For example:
                     for (Model_Send_Message message : messageList) {
                         messageTextArea.append(message.getMessageContent() + "\n");
                     }
                }
            }
        });
    }

    private void send(Model_Send_Message data) {
        Service.getInstance().getClient().emit("send_to_user", data.toJsonObject());
    }

    private void refresh() {
        revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(229, 229, 229));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private MigLayout mig;
    private Panel_More panelMore;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
