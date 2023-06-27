package SecureTextAndVoiceChat.SharedClasses;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Message implements Serializable {
    public ArrayList<String> connectedUsersList ;
    public String textMessage = null;
    public byte[] bytesMessage = null;
    public boolean audio = false;
    public boolean broadcast = false;
    public String IntendedUser = null;
  
    public Message(String textMessage, boolean audio)
    {
        this.textMessage = textMessage;
        
        
    }
    public Message(byte[] b,String IntendedUser)
    {   
        this.IntendedUser=IntendedUser;
        this.bytesMessage = b;
        this.audio = true;                
    }            
    public Message(boolean broadcast , ArrayList<String> connectedUsersList)
    {
        this.broadcast = broadcast;
        this.connectedUsersList = connectedUsersList;
    }  
       
}
