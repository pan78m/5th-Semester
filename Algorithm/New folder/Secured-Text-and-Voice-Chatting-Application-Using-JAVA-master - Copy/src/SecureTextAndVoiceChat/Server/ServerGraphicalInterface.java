package SecureTextAndVoiceChat.Server;

import SecureTextAndVoiceChat.Server.ServerTools.*;
import SecureTextAndVoiceChat.SharedClasses.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


//main class to run The Server with a Graphical Interface
public class ServerGraphicalInterface extends JFrame { 
    private  JTextArea jta = new JTextArea();//Area will inforamtion will be displayed
    public static ArrayList<ConnectedUser> connectedUsersList = new ArrayList<ConnectedUser>();
    private static KeyPair keyPair = null;
    private RSAPublicKey pub;
    RSAPrivateKey priv;
    public static void main(String[] args) throws IOException{      
         new ServerGraphicalInterface();
     }    
    
    public  ServerGraphicalInterface() throws IOException{
        //Creating Graphical Interface for Server
        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);
        setTitle("Two-Users Chat Server");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        try {
        keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        }
        catch (NoSuchAlgorithmException e) 
        {
        throw new RuntimeException(e);
        }
        pub = (RSAPublicKey) keyPair.getPublic();
        priv = (RSAPrivateKey) keyPair.getPrivate();
        System.out.println("privateKey modulus is : "+ priv.getModulus());
        System.out.println("privateKey exponent is : "+priv.getPrivateExponent());
        System.out.println("publicKey modulus is : "+pub.getModulus());
        System.out.println("publickey is : "+pub.toString());
        ServerSocket serverSocket= new ServerSocket(1234);
        // inform that server is established:
        jta.append("Chat Server has started at " + new Date() + '\n' + '\n');
        while(true){
        Socket socket = serverSocket.accept();
        new Thread(new Processing(socket,jta)).start();  
        new Thread(new BroadcastThread()).start();
    }       
}     
public class Processing implements Runnable{
    private Socket socket;
    private JTextArea jta;
    private ObjectInputStream input;
    public Processing(Socket socket, JTextArea jta) throws IOException
    {   
        this.socket = socket;
        this.jta = jta;        
    }
    public void  run ()
    {   System.out.println("Processing a user");
        try {            
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(pub);                 
            input = new ObjectInputStream(socket.getInputStream());System.out.println(" receiving AESKey");
            EncryptedKeyAndLogInData encryptedKeyAndLogInData = (EncryptedKeyAndLogInData) input.readObject();
            byte[] AESKeyEncoded = SecurityTasks.DecryptAESwithPrivateKey(encryptedKeyAndLogInData.encryptedSessionKey, priv);
            SecretKey AESKey = new SecretKeySpec(AESKeyEncoded, 0, AESKeyEncoded.length, "AES");
            RecordPacket recordPacket = SecurityTasks.DecryptRecordPacketwithTheSessionKey(AESKey, encryptedKeyAndLogInData.secretObject); 
            SealedObject secretObject;
            if(recordPacket.login == false)
            {   System.out.println("Started Signing Up");
                if(DataBase.addRecords(recordPacket.record)==true)
                {                           
                    secretObject =SecurityTasks.EncryptObjectwithTheSessionKey(AESKey,"User Added Successfuly" );
                    output.writeObject(secretObject);                        
                } 
            }
            else
            {                 
                boolean authenticator= DataBase.authenticate(recordPacket.record);                    
                if(authenticator==false)
                { 
                    secretObject =SecurityTasks.EncryptObjectwithTheSessionKey(AESKey,"Username or passweord is wrong" );
                    output.writeObject(secretObject);
                    jta.append('\n'+ recordPacket.record.userName+ " has failed to connect " + new Date() + '\n' + '\n');
                }                    
                else
                {
                    secretObject = SecurityTasks.EncryptObjectwithTheSessionKey(AESKey,"User connected Successfuly" );
                    output.writeObject(secretObject);                        
                    ConnectedUser connectedUser =new ConnectedUser(recordPacket.record.userName,socket,AESKey);
                    new Thread(new AddUserThread(connectedUser)).start();
                    
                    jta.append('\n'+ recordPacket.record.userName+ " successfuly connected to the server at " + new Date() + '\n' + '\n');
                    new Thread(new Handle(connectedUser,jta)).start();
                     new Thread(new BroadcastThread()).start();                      
                }                    
            }       
        
        }
        catch (IOException ex) {            
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchPaddingException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidKeyException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (BadPaddingException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
/*public class BroadCast implements Runnable{
    public void run() 
    { 
        ArrayList<String> names = new ArrayList<String>();          
        names.clear();
        for(ConnectedUser users:ServerGraphicalInterface.connectedUsersList)
            names.add(users.name);
        Message broadcastMessage = new Message(true,names);
        if(connectedUsersList !=null)
            for(ConnectedUser users:ServerGraphicalInterface.connectedUsersList)
            {
                try {
                    MessageSender.sendFromServer(users.socket, broadcastMessage,users.AESKey);
                } 
                catch (IOException ex) {
                    connectedUsersList.remove(users); 
                    new Thread(new BroadCast()).start();
                    Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (IllegalBlockSizeException ex) {
                    Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (InvalidKeyException ex) {
                    Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (NoSuchPaddingException ex) {
                    Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }                
    }    
}*/
class Handle implements Runnable
{
    JTextArea jta;   
    ObjectInputStream in;
    ObjectOutputStream out;
    ConnectedUser connectedUser;     
    public Handle(ConnectedUser connectedUser,JTextArea jta)
    {   
        this.jta =jta;   
        this.connectedUser = connectedUser;        
    }
    public void run()
    {                  
        try 
        {
            while(true)
                {   
                    in = new ObjectInputStream(connectedUser.socket.getInputStream());    
                    SealedObject secretMessage = (SealedObject) in.readObject();
                    Message message = (Message) SecurityTasks.DecryptObjectwithTheSessionKey(connectedUser.AESKey, secretMessage);                    
                    System.out.println("Is this message audio?: "+ message.audio)    ;           
                    if(message.audio==false)
                        jta.append("The server received this message: " + message.textMessage);                    
                    new Thread(new SendAllUsersThread(message)).start();
                    
                }
        }
        catch(IOException e)
        { 
            System.err.println(e);
             new Thread(new RemoveUserThread(connectedUser)).start();
            System.out.println("removed a disconnected user");
             new Thread(new BroadcastThread()).start();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}

}