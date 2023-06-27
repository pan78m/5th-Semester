package SecureTextAndVoiceChat.SharedClasses;


import java.io.Serializable;

public class Record implements Serializable
{
    public  String userName;
    public  String password;
    public Record(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }     
}
