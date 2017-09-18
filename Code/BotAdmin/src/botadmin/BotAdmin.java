package botadmin;

import javax.swing.DefaultListModel;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class BotAdmin implements ActionListener {
    JFrame frame;                                        
    JButton btnexec, btnexecizq, btnconnect,btnupdatebots,btnsavelog,btnclear,btnabout,btnviewlog;
    JPanel psuperior, pcentro, pinferior, pnativo, pizquierdo;
    JTextField jtxtcanal, jtxtnick, jtxtstatus, jtxtin1, jtxtin2, jtxtin3, jtxtcustominput,jtxtserver,jtxtbot;
    JLabel lblcanal, lblnick, lblstatus, lblcommand, lblbots, lblcommands, lblargs,lblserver,lblbot;
    JTextArea jtxtarealogs;
    JList jlbots, jlcommands;
    JScrollPane jscroll;
    JScrollPane jscrolljlbots;
    JScrollPane jscrolljlcommands;
    String cmdchoice = "";
    String file = "";
    String bot = "";
    int rot = 0;
    int bytexor = 0;
    String clavedes = "";
    //
    BufferedWriter writer;
    String server;
    String channel;
    String nick;
    
    public BotAdmin(BufferedWriter writer,String nick,String channel,String server) {
        this.writer=writer;
        this.server=server;
        this.nick=nick;
        this.channel=channel;
        initComponents();
        agregaActionListener();
        setContents();
        frame.add(psuperior, BorderLayout.NORTH);
        frame.add(pinferior, BorderLayout.CENTER);
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    public void initComponents(){
        initFrame();
        initTextArea();
        initPanels();
        initTextFields();
        initLabels();
        initLists();
        initButtons();
    }

    public void initFrame(){
        this.frame = new JFrame("BotAdmin");
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());
    }
    
    public void initTextArea(){
        this.jtxtarealogs = new JTextArea(35, 70);
        this.jtxtarealogs.setLineWrap(true);
        this.jtxtarealogs.setWrapStyleWord(true);
        this.jscroll = new JScrollPane(jtxtarealogs);
        jtxtarealogs.setText("Nick:"+nick+"\nChannel"+channel);
    }
    
    public void initLists() {
        jlbots = new JList();
        jlbots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlbots.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    bot = "" + jlbots.getSelectedValue();
                }
            }
        });
        jlcommands = new JList(new String[]{"cifraBinRot", "descifraBinRot",
            "cifraByteRot", "descifraByteRot",
            "cifraByteXOR", "descifraByteXOR",
            "cifraByteRotXOR", "descifraByteRotXOR",
            "cifraBinTransposicionInversa", "descifraBinTransposicionInversa",
            "cifraBinDes", "descifraBinDes",
            "dir","rm","download","quit"
        });
        jlcommands.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlcommands.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    cmdchoice = "" + jlcommands.getSelectedValue();
                    System.out.println(jlcommands.getSelectedIndex());
                }
            }
        });
    }

    public void initPanels() {
        this.psuperior = new JPanel();
        this.pcentro = new JPanel();
        this.pinferior = new JPanel();
        this.pizquierdo = new JPanel();
    }

    public void initTextFields() {
        jtxtserver = new JTextField("");
        jtxtserver.setText(server);
        jtxtserver.setPreferredSize(new Dimension(250, 20));
        jtxtcanal = new JTextField("");
        jtxtcanal.setText(channel);
        jtxtcanal.setPreferredSize(new Dimension(200, 20));
        jtxtnick = new JTextField("");
        jtxtnick.setText(nick);
        jtxtnick.setPreferredSize(new Dimension(150, 20));
        jtxtstatus = new JTextField("");
        jtxtstatus.setPreferredSize(new Dimension(50, 20));
        jtxtin1 = new JTextField("");
        jtxtin1.setPreferredSize(new Dimension(180, 20));
        jtxtin2 = new JTextField("");
        jtxtin2.setPreferredSize(new Dimension(45, 20));
        jtxtin3 = new JTextField("");
        jtxtin3.setPreferredSize(new Dimension(30, 20));
        jtxtcustominput = new JTextField("");
        jtxtcustominput.setPreferredSize(new Dimension(500, 20));
        jtxtbot=new JTextField("");
        jtxtbot.setPreferredSize(new Dimension(100, 20));
    }

    public void initLabels() {
        lblserver = new JLabel("Server: ");
        lblcanal = new JLabel("Canal: ");
        lblnick = new JLabel("Nick: ");
        lblstatus = new JLabel("Status: ");
        lblcommand = new JLabel("   Command: ");
        lblbots = new JLabel("Bots: ");
        lblcommands = new JLabel("Commands: ");
        lblargs = new JLabel("Args: ");
        lblbot=new JLabel("Bot: ");
    }

    public void initButtons(){
        this.btnexec=new JButton("Exec");
        this.btnexecizq=new JButton("Exec");
        this.btnsavelog=new JButton("Save Log");
        this.btnconnect=new JButton("Connect");
        this.btnupdatebots=new JButton("Update Bots List");
        this.btnclear=new JButton("Clear");
        this.btnabout=new JButton("About");
        this.btnviewlog=new JButton("View Log");
    }
    
    public void setContents() {
        psuperior.setLayout(new FlowLayout());
        psuperior.add(lblserver);
        psuperior.add(jtxtserver);
        psuperior.add(lblcanal);
        psuperior.add(jtxtcanal);
        psuperior.add(lblnick);
        psuperior.add(jtxtnick);
        psuperior.add(btnconnect);
        psuperior.add(lblstatus);
        psuperior.add(jtxtstatus);  
        
        jlbots.setVisibleRowCount(6);
        jscrolljlbots = new JScrollPane(jlbots);
        jlcommands.setVisibleRowCount(10);
        jscrolljlcommands = new JScrollPane(jlcommands);
        
        pcentro.add(lblargs);
        pcentro.add(jtxtin1);
        pcentro.add(jtxtin2);
        pcentro.add(jtxtin3);
        pcentro.add(btnexecizq);

        pizquierdo.setLayout(new BorderLayout());
        pizquierdo.add(jscrolljlcommands, BorderLayout.NORTH);
        pizquierdo.add(jscrolljlbots, BorderLayout.CENTER);
        pizquierdo.add(pcentro, BorderLayout.SOUTH);

        pnativo = new JPanel();
        pnativo.setLayout(new FlowLayout());
        pnativo.add(btnupdatebots);
        pnativo.add(lblcommand);
        pnativo.add(jtxtcustominput);
        pnativo.add(btnexec);
        pnativo.add(btnsavelog);
        pnativo.add(btnviewlog);
        pnativo.add(btnclear);
        pnativo.add(btnabout);

        pinferior.setLayout(new BorderLayout());
        pinferior.add(pnativo, BorderLayout.NORTH);
        pinferior.add(jscroll, BorderLayout.CENTER);
        pinferior.add(pizquierdo, BorderLayout.WEST);
    }

    public void agregaActionListener() {
        this.btnexec.addActionListener(this);
        this.btnexecizq.addActionListener(this);
        this.btnconnect.addActionListener(this);
        this.btnupdatebots.addActionListener(this);
        this.btnsavelog.addActionListener(this);
        this.btnviewlog.addActionListener(this);
        this.btnclear.addActionListener(this);
        this.btnabout.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try{
            if(e.getSource()==btnconnect){
                String server=jtxtserver.getText();
                String channel=jtxtcanal.getText();
                String nick=jtxtnick.getText();
                if(server.length()>0&&channel.length()>0&&nick.length()>0){
                    //no funciona, agregar campo para definir el puerto
                    new IRC(server,nick,channel,6667);
                }
            }
            if(e.getSource() == btnexec){
                String result=writeCustomCommand(jtxtcustominput.getText());
                writer.write("PRIVMSG " + channel +" : <"+nick+"> "+result+"\r\n");
                writer.flush();
            }
            if(e.getSource() == btnexecizq){
                System.out.println("bot es "+bot);
                String result=writeCommand(bot, cmdchoice, jtxtin1.getText(), jtxtin2.getText(), jtxtin3.getText());
                writer.write("PRIVMSG " + channel +" : <"+nick+"> "+result+"\r\n");
                writer.flush();
            }
            if(e.getSource() == btnupdatebots){
                String result="/names";
                String instruction="PRIVMSG " + channel +" : <"+nick+"> "+result+"\r\n";
                writer.write(instruction);
                writer.flush();
                IRC.write(writer,"/names", channel);
                cambiaLista();
            }
            if(e.getSource()==btnsavelog){
                IRC.writeLog();
            }
            if(e.getSource()==btnviewlog){
                JFrame framelog=new JFrame("Log");
                framelog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JTextArea jtxtviewlog=new JTextArea();
                jtxtviewlog = new JTextArea(35, 70);
                jtxtviewlog.setLineWrap(true);
                jtxtviewlog.setWrapStyleWord(true);
                jtxtviewlog.setText(IRC.readLog());
                JScrollPane scrollLog = new JScrollPane(jtxtviewlog);
                framelog.add(scrollLog);
                framelog.pack();
                framelog.setVisible(true);
            }
            if(e.getSource()==btnclear){
                this.jtxtarealogs.setText("\n");
            }
            if(e.getSource()==btnabout){
                JFrame f=new JFrame("About");
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.add(new JTextArea("This program is released under WTFPL License\n"+
                                    "For more infomation visit: http://www.wtfpl.net \n"+
                                    "Contact: masterofdisaster@ciencias.unam.mx"));
                f.pack();
                f.setVisible(true);
            }
        }catch(Exception ex){
            System.out.println("Ocurrio un error en BotAdmin.actionPerformed "+ex.getMessage());
        }
    }

    public void cambiaLista(){
      this.jlbots.setModel(IRC.changeList());
    }
    
    public static String writeCommand(String bot, String cmd, String file,
                                      String p1, String p2) {
        if (bot.equals("All Bots")||bot.length()==0) {
            bot = "";
        } else {
            bot = "<<" + bot + ">>";
        }
        if(cmd.length()!=0)
            return "CustomCommand"+bot + "!@" + cmd + " " + file + " " + p1 + " " + p2;
        return "";
    }

    public static String writeCustomCommand(String cmd) {
        return "CustomCommand"+cmd;
    }
}
