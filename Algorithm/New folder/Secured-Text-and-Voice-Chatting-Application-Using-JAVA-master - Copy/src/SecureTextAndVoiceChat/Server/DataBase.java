package SecureTextAndVoiceChat.Server;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import SecureTextAndVoiceChat.SharedClasses.*;
import SecureTextAndVoiceChat.SharedClasses.Record;
public class  DataBase implements Serializable
{  static FileInputStream finput;
   static FileOutputStream foutput; // object used to output text to file    
 public static synchronized void openFile(boolean w) throws FileNotFoundException, IOException 
 {
     if(w== true) foutput =  new FileOutputStream( "ListOfRegisteredUsers.txt",true ) ;
     if (w==false) finput =  new FileInputStream( "ListOfRegisteredUsers.txt");
 }
   public static boolean addRecords(Record record) throws IOException
   {               
       openFile(true);
       System.out.println("Opened File");
       
           ObjectOutputStream output = new ObjectOutputStream (foutput);
           System.out.println("Opened writeStream in addRecords");
           output.writeObject(record);
           output.flush(); 
           //output.close();
       
       
       closeFile(true);System.out.println("closing file in addRecord");
       return  true;
   }
   
   public static synchronized boolean authenticate(Record record) throws FileNotFoundException, ClassNotFoundException 
   {   Record entry=null ;
       
       try 
       {
            openFile(false) ;
                   
       } 
       catch (IOException ex) {
                       
                   }
         while ( finput !=null)
               {        ObjectInputStream input; 
             try { if(finput !=null)
             {input = new ObjectInputStream (finput);
                entry = (Record) input.readObject();
                      
             }
                    System.out.println("entry.userName: " + entry.userName);
                    System.out.println("entry.password: " + entry.password);
                    if((entry.userName).equals(record.userName)  && entry.password.equals(record.password))return true;
                    
             } 
             catch ( EOFException endOfFileException )
             {
                 
           System.out.println("endOfFileException");
                   try {
                       closeFile(false);System.out.println("Closing file"); 
                   } catch (IOException e) {
                       System.out.println("IOException in authenticate method trying to close file");
                   }                                                  
             }
             catch (IOException ex) {
                 System.out.println("IO in authenticate method");
                 
             }
               }
          try {
                       closeFile(false);System.out.println("closefile in case not closed"); 
                   } catch (IOException e) {
                       System.out.println("endig authentication");
                   }
          
          return false;      
      } 
   
  // close file
   public static void closeFile(boolean w) throws IOException
   {
      if(w ==true) if ( foutput != null )
      {foutput.close();foutput=null;}
      if(w ==false)if ( finput != null )
      {finput.close(); finput=null;}
      
   } // end method closeFile
} 
