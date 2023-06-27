package SecureTextAndVoiceChat.Server;

import SecureTextAndVoiceChat.SharedClasses.Message;
import SecureTextAndVoiceChat.SharedClasses.MessageSender;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ServerTools {
    
    public static synchronized void broadcastUsers() 
    { 
        ArrayList<String> names = new ArrayList<String>();          
        names.clear();
        for(ConnectedUser users:ServerGraphicalInterface.connectedUsersList)
            names.add(users.name);
        Message broadcastMessage = new Message(true,names);
        if(ServerGraphicalInterface.connectedUsersList !=null)
            for(ConnectedUser users:ServerGraphicalInterface.connectedUsersList)
            {
                try {
                    MessageSender.sendFromServer(users.socket, broadcastMessage,users.AESKey);
                } 
                catch (IOException ex) {
                    ServerGraphicalInterface.connectedUsersList.remove(users); 
                    broadcastUsers();
                    Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
                    Logger.getLogger(ServerGraphicalInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(ConcurrentModificationException ex)
                            {
                                Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
                            }
                
            }                
    }
    public static synchronized void displayUsers() 
    {
        System.out.print("List of Available Connected Users: ");
                    for(ConnectedUser users:ServerGraphicalInterface.connectedUsersList) System.out.print(users.name + ", ");System.out.println();
    }
    
    public static synchronized void addUser(ConnectedUser connectedUser){
       
        ServerGraphicalInterface.connectedUsersList.add(connectedUser);
    }
    public static synchronized void removeUser(ConnectedUser connectedUser){
       
        ServerGraphicalInterface.connectedUsersList.remove(connectedUser);
    }
    public static synchronized void sendAllUsers(Message message) throws  IllegalBlockSizeException, InvalidKeyException
    {   
        for(ConnectedUser users:ServerGraphicalInterface.connectedUsersList)
            {
                if(message.audio==true) 
                {   
                    System.out.println("Intended use is " + message.IntendedUser);
                    System.out.println(" user in loop is " + users.name);
                    if(users.name.equals(message.IntendedUser ))
                        {
                            System.out.println("Matched Intended Users");
                            try {
                                    MessageSender.sendFromServer( users.socket, message,users.AESKey);
                                } 
                            catch (NoSuchAlgorithmException ex) {
                                    Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            catch (NoSuchPaddingException ex) {
                                    Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                            catch (IOException ex) {                                     
                                System.err.println(ex);
                                ServerTools.removeUser(users);
                                 System.out.println("removed a disconnected user");
                                 ServerTools.broadcastUsers();  
                                }
                            catch(ConcurrentModificationException ex)
                            {
                                Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }      
                }
                else{
                        try {                            
                            MessageSender.sendFromServer( users.socket, message,users.AESKey);
                            } 
                        catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
                            Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        catch(IOException e){
                            ServerTools.removeUser(users);
                            ServerTools.broadcastUsers();
                        }
                        catch(ConcurrentModificationException ex)
                            {
                                Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
                            }
                }                    
            }
        
    }
    public static class DisplayThread implements Runnable
{
    public DisplayThread(){}
        @Override
        public void run() {
            ServerTools.displayUsers();
        }
}
    public static class BroadcastThread implements Runnable
    {
        public BroadcastThread()
        {
            
        }
        public void run()
        {
            ServerTools.broadcastUsers();
        }
    }      
public static class AddUserThread implements Runnable
{
    ConnectedUser connectedUser;
    public AddUserThread(ConnectedUser connectedUser)
    {
        this.connectedUser=connectedUser;
    }

        @Override
        public void run() {
            ServerTools.addUser(connectedUser);
        }
   
}
public static class RemoveUserThread implements Runnable
{
    ConnectedUser connectedUser;
    public RemoveUserThread(ConnectedUser connectedUser)
    {
        this.connectedUser=connectedUser;
    }

        @Override
        public void run() {
            ServerTools.removeUser(connectedUser);
        }
   
}
public static class SendAllUsersThread implements Runnable
{
    Message message;
    public SendAllUsersThread(Message message)
    {
        this.message=message;
    }

        @Override
        public void run() {
        try {
            ServerTools.sendAllUsers(message);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
   
}
}