package Chat;


/*
package group.chatting.application;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Server implements Runnable {

    Socket socket;
    public static Vector<ClientHandler> clients = new Vector<>();
    private static final String AES_KEY = "1234567891234567";

    public Server(Socket socket) {
        try {
            this.socket = socket;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Generate a unique client ID
            String clientId = UUID.randomUUID().toString();

            // Create a new ClientHandler for the client
            ClientHandler clientHandler = new ClientHandler(clientId, writer);

            // Add the client handler to the list
            clients.add(clientHandler);

            while (true) {
                String encryptedData = reader.readLine().trim();
                System.out.println("Received from " + clientId + ": " + encryptedData);

                // Decrypt the received message using AES
                String decryptedData = decrypt(encryptedData);

                // Broadcast the message to other clients
                broadcastMessage(clientId, decryptedData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String senderId, String message) {
        for (ClientHandler client : clients) {
            if (!client.getClientId().equals(senderId)) {
                try {
                    BufferedWriter bw = client.getWriter();

                    // Encrypt the message before sending it to clients
                    String encryptedMessage = encrypt(message);

                    bw.write(encryptedMessage);
                    bw.write("\r\n");
                    bw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String encrypt(String message) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decrypt(String encryptedMessage) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(2003);
        System.out.println("Server started...");
        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}

class ClientHandler {
    private String clientId;
    private BufferedWriter writer;

    public ClientHandler(String clientId, BufferedWriter writer) {
        this.clientId = clientId;
        this.writer = writer;
    }

    public String getClientId() {
        return clientId;
    }

    public BufferedWriter getWriter() {
        return writer;
    }
}




 */


import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable {
    
    Socket socket;
    
    public static Vector client = new Vector();
    
    public Server (Socket socket) {
        try {
            this.socket = socket;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            client.add(writer);
            
            while(true) {
                String data = reader.readLine().trim();
                System.out.println("Received " + data);
                
                for (int i = 0; i < client.size(); i++) {
                    try {
                        BufferedWriter bw = (BufferedWriter) client.get(i);
                        bw.write(data);
                        bw.write("\r\n");
                        bw.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        ServerSocket s = new ServerSocket(2003);
        while(true) {
            Socket socket = s.accept();
            Server server = new Server(socket);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}
