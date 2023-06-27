package SecureTextAndVoiceChat.Client;


import SecureTextAndVoiceChat.SharedClasses.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import javax.sound.sampled.TargetDataLine;
import javax.swing.JLabel;
import javax.swing.JTextField;

class SendAudio implements Runnable{    
    private JToggleButton callButton;
    private JComboBox comboBox;
    private JTextArea jta;
    private String textMessage;
    private Socket socket;
    private JLabel jtf1;
    ObjectOutputStream out;//stream to send to server
    SecretKey AESKey;
    
   public  SendAudio(Socket socket,JToggleButton callButton, JComboBox comboBox,JTextArea jta,JLabel jtf1,SecretKey AESKey)
    {
        // assigning parameters to local instances
        this.callButton= callButton;
        this.comboBox = comboBox;
        this.jta = jta;
        this.socket = socket;
        this.jtf1 = jtf1;     
        this.AESKey=AESKey;
        
    }    
   
    public void run() {        
        try{ System.out.println("audio started");
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
	    TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);            
	    targetLine.open(format);
	    targetLine.start();           
            byte[] targetData = new byte[targetLine.getBufferSize() ];
            Message message;
            //callButton.isSelected())
            if(callButton.isSelected()){   System.out.println("call button is selected  loop");               
               {                  
                   textMessage = jtf1.getText()+" is Calling " + comboBox.getSelectedItem(); 
                   message = new Message((textMessage + '\n'),false);
                try {               
                    MessageSender.send(socket, message,AESKey);
                } catch (IOException ex) {
                    Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalBlockSizeException ex) {
                    Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeyException ex) {
                    Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchPaddingException ex) {
                    Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
                }
                   while(callButton.isSelected()){ 
                
                targetLine.read(targetData, 0, targetData.length);             	
                message = new Message(targetData,comboBox.getSelectedItem().toString());
                
                MessageSender.send(socket, message,AESKey);               
                System.out.print("Sended an audio to ");
                System.out.println(comboBox.getSelectedItem().toString());
               }
                 
            }            
            targetLine.close();//targetLine.
        }} catch (LineUnavailableException ex) {
            Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(SendAudio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}



