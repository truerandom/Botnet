
package ircbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

public class Archivos {

    /*
     Toma la direccion de un archivo y devuelve el contenido de este
     */
    public static String read(File f) {
        BufferedReader br = null;
        String content = "";
        try {
            String linea;
            br = new BufferedReader(new FileReader(f));
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("error en Archivos.read " + e.getMessage());
        }
        return content;
    }

    /*
     Toma la direccion de un archivo
     y regresa los bytes de este
     WORKS
     */
    //Agregada compatibilidad Java 1.6
    public static byte[] readBytes(File file) throws IOException {
        byte[] fileContent=null;
      try{
         FileInputStream fin = new FileInputStream(file);
         fileContent= new byte[(int)file.length()];
         fin.read(fileContent);
         return fileContent;
      }catch(Exception e){
        System.out.println("Error@readBytes");
      }
      return fileContent;
    }
    

    /*
     Toma un arreglo de bytes y regresa su representacion en base 64
     */
    public static String bytesToBase64(byte[] bytes) {
        Base64 base64 = new Base64();
        return base64.encodeToString(bytes);
    }

    /*
     Toma una cadena en base64 y regresa el arreglo de bytes 
     */
    public static byte[] base64ToBytes(String b64) {
        Base64 base64 = new Base64();
        return base64.decode(b64);
    }

    /*
     Toma un nombre de archivo (ruta) y un texto
     Crea un archivo (o lo sobreescbribe) con el texto parametro
     */
    public static void write(String filename, String content) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println(content);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error @Archivos.write " + e.getMessage());
        }
    }

    /*
     Toma un arreglo de bytes (representando a un archivo)
     un nombre de archivo
     y escribe el archivo con el nombre pasado como parametro
     */
    public static void writeBytes(byte[] bytes, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            System.out.println("Error @Archivos.writeBytes " + e.getMessage());
        }
    }

    /*
     Toma una direccion de internet url,
     Un nombre de archivo (direccion)
     Y descarga el contenido de url con el nombre de archivo
     */
    public static String download(String url, String file) {
        try {
            File f = new File(file);
            URL dir = new URL(url);
            InputStream in = dir.openStream();
            Files.copy(in, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return url+" downloaded";
        } catch (Exception e) {
            return "Error @Archivos.download" + e.getMessage();
        }
    }

    /*
     Metodo Interno (Auxiliar) para list
     */
    public static List<String> listInternal(List<String> files, File dir) {
        if (files == null) {
            files = new LinkedList<String>();
        }
        for (File file : dir.listFiles()) {
            if(file.isDirectory())
                files.add("DIR:\\"+file.toString().replace(dir.toString(),""));
            else{
                files.add(file.toString().replace(dir.toString(),""));
            }
        }
        return files;
    }

    /*
     Toma la ruta de un directorio
     Y regresa el listado de todos los archivos y los subdirectorios
     */
    public static List<String> list(String ruta) {
        try{
            File dir = new File(ruta);
            return listInternal(null, dir);
        }catch(Exception e){
            System.out.println("Error@Archivos.list "+e.getMessage());
        }
        return new ArrayList<String>();
    }

    /*
     Toma la ruta de un archivo y lo borra
     */
    public static String delete(String f) {
        try{
            File file = new File(f);
            file.delete();
            return file+" deleted";
        }catch(Exception e){
            return "Error@Archivos.delete file "+f+" "+e.getMessage();
        }
    }

    public static void main(String[] args) throws IOException {
        String ruta="C:\\Users\\Dell\\Desktop\\Proyecto3\\";
        System.out.println(list(ruta));
    }

}
