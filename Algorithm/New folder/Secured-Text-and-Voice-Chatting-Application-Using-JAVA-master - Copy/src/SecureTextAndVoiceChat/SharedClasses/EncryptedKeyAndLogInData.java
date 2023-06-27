package SecureTextAndVoiceChat.SharedClasses;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class EncryptedKeyAndLogInData implements Serializable {
    public byte[] encryptedSessionKey;
    public SealedObject secretObject;
    
    public EncryptedKeyAndLogInData(SecretKey AESKey,RSAPublicKey pub,RecordPacket recordPacket) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, InvalidKeyException, BadPaddingException
    {        
        encryptedSessionKey = SecurityTasks.EncryptAESwithPublicKey(AESKey, pub);
        secretObject = SecurityTasks.EncryptRecordPacketwithTheSessionKey(AESKey,recordPacket);               
    }
}
