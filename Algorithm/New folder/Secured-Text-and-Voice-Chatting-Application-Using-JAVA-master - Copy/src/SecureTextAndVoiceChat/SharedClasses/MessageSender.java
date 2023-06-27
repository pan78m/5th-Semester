package SecureTextAndVoiceChat.SharedClasses;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;


public class MessageSender {
    // I may nead to create another synchronized only for server in  case it is slow
      public static synchronized void  send(Socket socket,Object message,SecretKey AESKey) throws IOException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException 
    {     
        SealedObject secretMessage = SecurityTasks.EncryptObjectwithTheSessionKey(AESKey, message); 
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(secretMessage);                
                          
    }
      public static synchronized void  sendFromServer(Socket socket,Object message,SecretKey AESKey) throws IOException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException 
    {     
        SealedObject secretMessage = SecurityTasks.EncryptObjectwithTheSessionKey(AESKey, message); 
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(secretMessage);                                          
    }
}

