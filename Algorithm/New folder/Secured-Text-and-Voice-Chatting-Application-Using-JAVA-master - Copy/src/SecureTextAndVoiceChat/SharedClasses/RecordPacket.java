package SecureTextAndVoiceChat.SharedClasses;

import java.io.Serializable;
public class RecordPacket implements Serializable {
    public Record record;
    public boolean login ;
    public RecordPacket(Record record,boolean login)
    {
        this.record = record;
        this.login = login;
    }
}
