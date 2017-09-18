package ircbot;
import java.io.IOException;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chaos
 */
public class OSUtils {
    /* This returns the OS's machine */
   public static String getOSName(){
      String os=System.getProperty("os.name").replaceAll(" ","");
      if(os.startsWith("Windows"))
         os="W_"+os.replace("Windows","");
      if(os.startsWith("Linux"))
         os="L_"+os.replace("Linux","");
      if(os.startsWith("Mac"))
         os="Mac"+os.replaceAll("MacOS","");
      return os;
   }

   /* We have to get an unique id for this machine */
   public static String getID() throws IOException {
      //This is working
      if(isWindows())
         return getOSName()+getWindowsID();
      if(isMac())
         return getOSName()+getMacID();
      if(isUnix())
         return getOSName()+getLinuxID();
      return "";
   }

   public static String getWindowsID() throws IOException{
      String output="";
      String command = "REG QUERY HKLM\\SOFTWARE\\Microsoft\\Cryptography /v MachineGuid";
      Process process = Runtime.getRuntime().exec(command);
      Scanner kb = new Scanner(process.getInputStream());
         while (kb.hasNext()) {
            output+="\n"+kb.next();
         }
      return output.substring(output.lastIndexOf("-")+1);
   }

   public static String getMacID() throws IOException{
      String output="";
      String command = "ioreg -rd1 -c IOPlatformExpertDevice";
      Process process = Runtime.getRuntime().exec(command);
      Scanner kb = new Scanner(process.getInputStream());
         while (kb.hasNext()) {
            output+="\n"+kb.next();
         }
      int idx=output.indexOf("IOPlatformUUID");
      return output.substring(idx+20,idx+27);
   }


   public static String getLinuxID() throws IOException{
      String output="";
      String command = "dmidecode -t 4 | grep ID";
      Process process = Runtime.getRuntime().exec(command);
      Scanner kb = new Scanner(process.getInputStream());
         while (kb.hasNext()) {
            output+="\n"+kb.next();
         }
      int idx=output.indexOf("root=");
      output=output.substring(idx+10,idx+18);
      return output;
   }

   public static boolean isWindows(){
      return System.getProperty("os.name").toLowerCase().contains("win");
   }

   public static boolean isMac(){
      return System.getProperty("os.name").toLowerCase().contains("mac");
   }

   public static boolean isUnix(){
      String os=System.getProperty("os.name").toLowerCase();
      return os.contains("unix")||os.contains("nux")||os.contains("aix");
   }

   public static void main(String[] args) throws IOException{
      System.out.println(getOSName());
   }
}
