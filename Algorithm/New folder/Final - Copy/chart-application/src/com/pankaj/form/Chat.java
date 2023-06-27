package com.raven.form;

import com.raven.component.Chat_Body;
import com.raven.component.Chat_Bottom;
import com.raven.component.Chat_Title;
import com.raven.event.EventChat;
import com.raven.event.PublicEvent;
import com.raven.model.Model_Receive_Message;
import com.raven.model.Model_Send_Message;
import com.raven.model.Model_User_Account;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import net.miginfocom.swing.MigLayout;

public class Chat extends javax.swing.JPanel {

    private Chat_Title chatTitle;
    private Chat_Body chatBody;
    private Chat_Bottom chatBottom;

// Encrypte
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "128"; // Replace with your own encryption key

    private Cipher encryptionCipher;
    private Cipher decryptionCipher;

    public Chat() {
        initComponents();
        init();
        initEncryption();
    }

    private void initEncryption() {
        try {
            Key encryptionKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), ENCRYPTION_ALGORITHM);
            encryptionCipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, encryptionKey);

            decryptionCipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            decryptionCipher.init(Cipher.DECRYPT_MODE, encryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private byte[] encryptMessage(String message) {
        try {
            return encryptionCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return null;
    }

    private String decryptMessage(byte[] encryptedMessage) {
        try {
            byte[] decryptedBytes = decryptionCipher.doFinal(encryptedMessage);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return null;
    }

public void sendMessage(Model_Send_Message data) {
    // Encrypt the message content before sending
    byte[] encryptedContent = encryptMessage(data.getMessageContent());
    if (encryptedContent != null) {
        data.setEncryptedContent(encryptedContent); // Set the encrypted content in the model
        chatBody.addItemRight(data);
    }
}



public void receiveMessage(Model_Receive_Message data) {
    // Decrypt the message content before displaying
    byte[] encryptedContent = data.getEncryptedContent(); // Retrieve the encrypted content from the model
    String decryptedContent = decryptMessage(encryptedContent);
    if (decryptedContent != null) {
        data.setMessageContent(decryptedContent);
        if (chatTitle.getUser().getUserID() == data.getFromUserID()) {
            chatBody.addItemLeft(data);
        }
    }
}




    private void init() {
        setLayout(new MigLayout("fillx", "0[fill]0", "0[]0[100%, fill]0[shrink 0]0"));
        chatTitle = new Chat_Title();
        chatBody = new Chat_Body();
        chatBottom = new Chat_Bottom();
        PublicEvent.getInstance().addEventChat(new EventChat() {
            @Override
            public void sendMessage(Model_Send_Message data) {
                chatBody.addItemRight(data);
            }

            @Override
            public void receiveMessage(Model_Receive_Message data) {
                if (chatTitle.getUser().getUserID() == data.getFromUserID()) {
                    chatBody.addItemLeft(data);
                }
            }
        });
        add(chatTitle, "wrap");
        add(chatBody, "wrap");
        add(chatBottom, "h ::50%");
    }

    public void setUser(Model_User_Account user) {
        chatTitle.setUserName(user);
        chatBottom.setUser(user);
        chatBody.clearChat();
    }

    public void updateUser(Model_User_Account user) {
        chatTitle.updateUser(user);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 727, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
