
package ircbot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class Utils {
    
    public static byte[] rotaBits(String archivo, int nshifts) throws IOException, InterruptedException {
        File file = new File(archivo);
        byte[] bytes = Archivos.readBytes(file);
        BigInteger bigi = new BigInteger(bytes);
        byte[] bytesrotados = bigi.shiftRight(nshifts).toByteArray();
        return bytesrotados;
    }

    public static byte[] desrotaBits(String archivo, int nshifts) throws IOException, InterruptedException {
        File file = new File(archivo);
        byte[] bytes = Archivos.readBytes(file);
        BigInteger bigi = new BigInteger(bytes);
        byte[] bytesrotados = bigi.shiftLeft(nshifts).toByteArray();
        return bytesrotados;
    }

    /* 
        Esto lo utilizo en la clase IRC, sirve para ir mandando en 
        partes es muy largo para IRC,es decir, si este tiene mas de 500 chars;
        y evitar el flooding
    
    */
    public static void sendList(BufferedWriter writer,String result,String channel,String nick) throws IOException{
      int maxlength=500;
      int it=result.length()/maxlength;
      String res="";
      for(int i=0;i<it;i++){
         res=result.substring(i*maxlength,(i*maxlength)+maxlength);
         writer.write("PRIVMSG " + channel +" : <"+nick+"> "+res+"\r\n");
      }
      res=result.substring(it*maxlength);
      writer.write("PRIVMSG " + channel +" : <"+nick+"> "+res+"\r\n");
      writer.flush();
    }
}
