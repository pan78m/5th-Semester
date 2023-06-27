package SecureTextAndVoiceChat.Client;

import SecureTextAndVoiceChat.SharedClasses.*;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;

// A client graphical interface to interact with a user
public class ClientGraphicalInterface extends JFrame  {
    private JLabel j1 = new JLabel();//create textfield that will be used to read the user name
    private JTextField jtf2 = new JTextField();//create textfield that will be used to the message
    private JTextArea jta = new JTextArea();// create textarea where messages are displayed
    public static JComboBox comboBox = new JComboBox();
    public static JToggleButton callButton;     

    String userName;//String to assign the user initial user name
    Socket socket;
    private SecretKey AESkey;       
    public  ClientGraphicalInterface (String userName,Socket socket, SecretKey AESKey) //it receives the intitial user name from the main method
    {   
        j1.setText(userName);//assign the initial user name to the textfield jtf1( it can be modified by the user  
        this.userName=userName;//assign the parameter of constructor to the instance of class
        this.socket=socket;
        this.AESkey=AESKey;
//Customizing the graphical interface
        JPanel p = new JPanel();
        JPanel p1 = new JPanel();        
        JPanel callPane = new JPanel();
       comboBox.addItem("");
         callButton = new JToggleButton("Call a User");     
        
        p.setLayout(null);
        JLabel uJLabel = new JLabel("User Name ");
        uJLabel.setBounds(0, 0, 100, 20);
        p.add(uJLabel);
        j1.setBounds(100, 0, 575, 20);
        p.add(j1);
        j1.setHorizontalAlignment(JTextField.LEFT);
        p1.setLayout(null);
        JLabel uJLabel2 =new JLabel("Enter Message ");
        uJLabel2.setBounds(0, 0, 100, 20);
        p1.add(uJLabel2);
        jtf2.setBounds(100, 0, 575, 20);
        p1.add(jtf2);
        jtf2.setHorizontalAlignment(JTextField.LEFT);
        callPane.setLayout(null);        
        callButton.setBounds(50,0,150,40);                   
        callPane.add(callButton);
        comboBox.setBounds(400,0,150,40); 
        callPane.add(comboBox);
        JLabel usersList = new JLabel("Available Users");
        usersList.setBounds(300, 0, 150, 40);        
        callPane.add(usersList);
        JScrollPane scrollPane = new JScrollPane(jta);
        setLayout(null);
        p.setBounds(5, 5, 675  , 20);
        add(p);
        p1.setBounds(5, 25, 675  , 20);
        add(p1);
        scrollPane.setBounds(5, 50, 675, 400);              
        add(scrollPane);
        callPane.setBounds(5,455,675,90);
        add(callPane);
        setTitle("Client");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);    
        new Thread(new HandleClient(socket,j1,jtf2,jta,comboBox,callButton, AESKey)).start();     
    }

class HandleClient implements Runnable
{   
    Socket socket ;// socekt to bind with server
    //local instances to be assigned to parameters send by constructor:
    JTextField jtf2;
    JLabel jl;
    JTextArea jta;
    String userName;
    JComboBox comboBox;
    JToggleButton callButton;
    SecretKey AESKey;
    public HandleClient(Socket socket, JLabel jl,JTextField jtf2,JTextArea jta,JComboBox comboBox,JToggleButton callButton,SecretKey AESKey){
        this.socket = socket;//coonect to the server
        //assign the instances of class to constructor parameters
        this.jl=jl;
        this.jtf2=jtf2;
        userName = jl.getText();
        this.jta = jta;
        this.callButton = callButton;
        this.comboBox = comboBox;
        this.AESKey = AESkey;
        
    }    
    public void run()
    {       
        jta.append(userName + " is ready to chat" +'\n');//Annoucing that user is ready chat
        new Thread(new ReceiveMessage(socket,jta,AESKey)).start();//A thread to receive from server
        jtf2.addActionListener(new SendMessage(socket,jl,jtf2,jta,AESKey));// a thread to send to server
        callButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent actionEvent){
            new Thread(new SendAudio(socket,callButton, comboBox,jta,jl,AESKey)).start();
        }
        }
        );       
        
    }
} 
class SendMessage implements ActionListener{
    //local instances of class
    //Graphical interface instances 
    private JTextField jtf2;
    private JLabel jl;
    private JTextArea jta;
    private String textMessage;
    private Socket socket;
    ObjectOutputStream out;//stream to send to server
    SecretKey AESKey;
    
    public  SendMessage(Socket socket,JLabel jl, JTextField jtf2,JTextArea jta,SecretKey AESKey)
    {        
        this.jl = jl;
        this.jtf2 = jtf2;
        this.jta = jta;
        this.socket = socket;    
        this.AESKey=AESKey;
    }
    public void actionPerformed(ActionEvent e)
    {           
        textMessage = jl.getText()+ " >> "+ jtf2.getText();
        Message message = new Message((textMessage + '\n'),false);
        try {            
            MessageSender.send(socket, message,AESKey);
        } 
        catch (IOException ex) {
            Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidKeyException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchPaddingException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        jtf2.setText("");
    }
}
//The method receives data fom server
class ReceiveMessage implements Runnable
{ //intances of class:    
    JTextArea jta;
    ObjectInputStream in; //stream to receive from server 
    Socket socket;//Socket to connect to server (it is the same as the on to send)
    SecretKey AESKey;
    public  ReceiveMessage(Socket socket,JTextArea jta,SecretKey AESKey)
    {   // asssign local instances to parameters     
        this.socket = socket;
        this.jta =jta;
        this.AESKey=AESKey;
    }
    public void run()
    {  String selectedName;
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
       DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
       SourceDataLine sourceLine;
        while(true)
        {
            try {            
                
                sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
                sourceLine.open(format);
                sourceLine.start();                 
                in = new ObjectInputStream(socket.getInputStream());                
                SealedObject secretMessage = (SealedObject) in.readObject();
                Message message = (Message) SecurityTasks.DecryptObjectwithTheSessionKey(AESKey, secretMessage);
                if(message.audio ==true) if(ClientGraphicalInterface.callButton.isSelected()){System.out.println("Intended user at CLIENT side is " + message.IntendedUser);
                    sourceLine.write(message.bytesMessage, 0, message.bytesMessage.length); }
                if(message.audio==false)
                {    selectedName= (String) comboBox.getSelectedItem();
                    System.out.println("The message is broadcast:"+message.broadcast);
                    if(message.broadcast==true)
                    {
                        ClientGraphicalInterface.comboBox.removeAllItems();
                        for(String users: message.connectedUsersList)
                        {   
                            System.out.println(users+" is still connected to server ");
                            ClientGraphicalInterface.comboBox.addItem(users);
                            if(users==selectedName) comboBox.setSelectedItem(users);
                        }
                    }
                    else jta.append( message.textMessage);
                }
                    
                  
            } 
            catch (LineUnavailableException ex) {
            Logger.getLogger(ReceiveMessage.class.getName()).log(Level.SEVERE, null, ex);break;
        }   catch (IOException ex) {
            Logger.getLogger(ReceiveMessage.class.getName()).log(Level.SEVERE, null, ex); System.out.println("Leaving loop");break;
        }   catch (ClassNotFoundException ex) {
            Logger.getLogger(ReceiveMessage.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (InvalidKeyException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (NoSuchPaddingException ex) {
            Logger.getLogger(ClientGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        } 
    }    
}
}