package chatting.application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ChatServerClient implements ActionListener {
    private JFrame frame;
    private JTextField text;
    private JTextArea textarea;
    private JButton sendButton;
    private DataInputStream din;
    private DataOutputStream dout;
    private ServerSocket server;
    private Socket socket;

    public ChatServerClient() {
        // Create and configure the GUI
        frame = new JFrame("Chat Server/Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        text = new JTextField(20);
        textarea = new JTextArea();
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        JPanel panel = new JPanel();
        panel.add(text);
        panel.add(sendButton);

        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(textarea));

        frame.setVisible(true);

        // Start the server and connect to it
        try {
            server = new ServerSocket(3333);
            textarea.append("Server started...\n");
            socket = server.accept();
            textarea.append("Client connected...\n");
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            receiveMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServerClient();
    }

    public void actionPerformed(ActionEvent event) {
        try {
            // Get the text from the text field
            String out = text.getText();

            // Compress the message
            byte[] compressedMessage = compressMessage(out);

            // Send the compressed message to the connected client
            dout.writeInt(compressedMessage.length);
            dout.write(compressedMessage);

            // Clear the text field
            text.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void receiveMessage() {
        while (true) {
            try {
                // Read the compressed message length from the client
                int compressedLength = din.readInt();
                byte[] compressedMessage = new byte[compressedLength];

                // Read the compressed message from the client
                din.readFully(compressedMessage);

                // Decompress the message
                String msg = decompressMessage(compressedMessage);

                // Display the decompressed message in the GUI
                textarea.append(msg + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private byte[] compressMessage(String message) throws IOException {
        byte[] input = message.getBytes("UTF-8");

        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int compressedBytes = deflater.deflate(buffer);
            outputStream.write(buffer, 0, compressedBytes);
        }

        deflater.end();
        outputStream.close();

        return outputStream.toByteArray();
    }

    private String decompressMessage(byte[] compressedMessage) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedMessage);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedMessage.length);
        byte[] buffer = new byte[1024];

        while (!inflater.finished()) {
            int decompressedBytes = inflater.inflate(buffer);
            outputStream.write(buffer, 0, decompressedBytes);
        }

        inflater.end();
        outputStream.close();

        return outputStream.toString("UTF-8");
    }
}
