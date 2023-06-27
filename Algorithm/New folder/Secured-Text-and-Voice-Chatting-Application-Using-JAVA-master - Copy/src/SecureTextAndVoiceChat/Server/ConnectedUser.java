package SecureTextAndVoiceChat.Server;

import java.io.Serializable;
import java.net.Socket;
import javax.crypto.SecretKey;
public class ConnectedUser implements Serializable {
    String name;
    Socket socket;
    SecretKey AESKey;
    
    public ConnectedUser(String name,Socket socket,SecretKey AESKey)
    {
        this.name=name;
        this.socket=socket;
        this.AESKey = AESKey;
    }   
}
