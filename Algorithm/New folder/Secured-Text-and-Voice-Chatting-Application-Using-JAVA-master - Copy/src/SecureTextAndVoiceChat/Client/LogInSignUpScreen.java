package SecureTextAndVoiceChat.Client;

import SecureTextAndVoiceChat.SharedClasses.Record;
import SecureTextAndVoiceChat.SharedClasses.*;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.swing.*;
public class LogInSignUpScreen extends JFrame {    
     JTextField jtf1 = new JTextField();
     JTextField jtf2 = new JTextField();
     JTextField serverAddress = new JTextField();     
     JButton button1 = new JButton();
     JButton button2 = new JButton();
    public  LogInSignUpScreen()
    {   serverAddress.setText("127.0.0.1");
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel serverPanel = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(new JLabel(" UserName          "), BorderLayout.WEST);
        p1.add(jtf1, BorderLayout.CENTER);
        jtf1.setHorizontalAlignment(JTextField.LEFT);
        p2.setLayout(new BorderLayout());        
        p2.add(new JLabel(" Password           "), BorderLayout.WEST);
        p2.add(jtf2, BorderLayout.CENTER);
        jtf2.setHorizontalAlignment(JTextField.LEFT);
        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        p3.add(p1,BorderLayout.NORTH);
        p3.add(p2,BorderLayout.SOUTH);
        serverPanel.setLayout(new BorderLayout());
        serverPanel.add(new JLabel(" Server Address "), BorderLayout.WEST);
        serverPanel.add(serverAddress, BorderLayout.CENTER);
        JPanel p5 = new JPanel();
        p5.setLayout(new BorderLayout());
        p5.add(p3,BorderLayout.NORTH);
        p5.add(serverPanel,BorderLayout.SOUTH);
        JPanel p4 = new JPanel();
        p4.setLayout(null);
        button1.setBounds(25, 10, 100, 75);
        button1.setText("LogIn");
        p4.add(button1);
        button2.setBounds(150, 10, 100, 75);
        button2.setText("SignUp");
        p4.add(button2);
        setLayout(null);
        p5.setBounds(25, 25, 300, 75);
        add(p5); 
        p4.setBounds(25, 100, 300, 75);
        add(p4);
        setTitle("LogIn/SignUp");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        button1.addActionListener(new LogIn(jtf1,jtf2));
        button2.addActionListener(new SignUp(jtf1,jtf2));     
    }

     
    public static void main(String[] args) {
        new LogInSignUpScreen();
    }
 class LogIn implements ActionListener
        {RecordPacket recordPacket;
        Record record;
        JTextField jtf1 ,jtf2;
        public LogIn(JTextField jtf1,JTextField jtf2){
             this.jtf1=jtf1;
             this.jtf2=jtf2;
             
        }
        public void actionPerformed(ActionEvent e){
            try {
                Socket socket = new Socket(serverAddress.getText(),1234);
                record = new Record(jtf1.getText(),jtf2.getText());
                recordPacket= new RecordPacket(record,true);  
                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
                RSAPublicKey pub = (RSAPublicKey) objectInput.readObject();
                SecretKey AESKey = SecurityTasks.createAES();
                EncryptedKeyAndLogInData encryptedKeyAndLogInData= new EncryptedKeyAndLogInData(AESKey,pub,recordPacket);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()); 
                output.writeObject(encryptedKeyAndLogInData);    System.out.println("Sending encryptedkey and login data");             
               
               SealedObject secretObject = (SealedObject) objectInput.readObject();System.out.println("received the response from server");
               String a= (String) SecurityTasks.DecryptObjectwithTheSessionKey(AESKey, secretObject);
                //
                JOptionPane.showMessageDialog( null,a );System.out.println("showed the server's response");
                 if("User connected Successfuly".equals(a))
                 { new ClientGraphicalInterface(jtf1.getText(),socket,AESKey);  
                 System.out.println("Start Chatting");
                 setVisible(false);
                 }
                
            } catch (IOException ex) {    
                JOptionPane.showMessageDialog( null,"Server is down" );
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    
}
 class SignUp implements ActionListener
        {RecordPacket recordPacket;
        Record record;
        JTextField jtf1 ,jtf2;
        public SignUp(JTextField jtf1,JTextField jtf2){
             this.jtf1=jtf1;
             this.jtf2=jtf2;
             
        }
        public void actionPerformed(ActionEvent e){
         // String adminstratorPassword = JOptionPane.showInputDialog( "Enter the Adminstrator Password" );
            
                try {
                Socket socket = new Socket(serverAddress.getText(),1234);
                record = new Record(jtf1.getText(),jtf2.getText());
                recordPacket= new RecordPacket(record,false);  
                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
                RSAPublicKey pub = (RSAPublicKey) objectInput.readObject();
                SecretKey AESKey = SecurityTasks.createAES();
                EncryptedKeyAndLogInData encryptedKeyAndLogInData= new EncryptedKeyAndLogInData(AESKey,pub,recordPacket);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()); 
                output.writeObject(encryptedKeyAndLogInData);        
                //ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
               // output.writeObject(recordPacket);                
                //DataInputStream input = new DataInputStream(socket.getInputStream());
                SealedObject secretObject = (SealedObject) objectInput.readObject();
               String a= (String) SecurityTasks.DecryptObjectwithTheSessionKey(AESKey, secretObject);
                //
                JOptionPane.showMessageDialog( null,a );
                //JOptionPane.showMessageDialog( null, input.readUTF());
            } catch (IOException ex) {     
                 JOptionPane.showMessageDialog( null,"Server is down" );
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(LogInSignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            }           
                           
        }    
}
}





