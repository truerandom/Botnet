package ircbot;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Cipherx {
    
    public static String cifraBinRot(String f, int k) throws IOException, InterruptedException {
        try{
            File file = new File(f);
            byte[] bytes = Archivos.readBytes(file);
            byte[] bytesrotados = Utils.rotaBits(f, k);
            Archivos.writeBytes(bytesrotados,f);
            return "file: "+f+" cifrado con cifraBinRot = "+k;
        }catch(Exception e){
            System.out.println("Error@Cipherx.cifraBinRot file ["+f+"]");
        }
        return "Error@Archivos.cifraBinRot con f: "+f;
    }

    /* 
     */
    public static String descifraBinRot(String f, int k) throws IOException, InterruptedException {
        try{
        File file = new File(f);
        byte[] bytes = Archivos.readBytes(file);
        byte[] bytesrotados = Utils.desrotaBits(f, k);
        Archivos.writeBytes(bytesrotados, f);
            return "file: "+f+" descifrado con descifraBinRot = "+k;
        }catch(Exception e){
            return "Error@Archivos.cifraBinRot con f: "+f;
        }
    }

    /* 
     Toma un archivo y una rotacion 
     Escribe un archivo con la info del archivo rotada k
     */
    public static String cifraByteRot(String f, int k) throws IOException {
        try{
            File file = new File(f);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;
            byte[] bytesn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                int pos = (i + (k)) % tam;
                if (pos < 0) {
                    pos += tam;
                }
                bytesn[pos] = bytes[i];
            }
            Archivos.writeBytes(bytesn, f);
            return "file: "+f+" cifrado con cifraByteRot = "+k;
        }catch(Exception e){
            return "Error@Archivos.cifraByteRot con f: "+f+" "+e.getMessage();
        }
    }

    /* 
     Toma un archivo y una rotacion 
     Escribe un archivo con la info del archivo rotada k bytes a la derecha
     */
    public static String descifraByteRot(String f, int k) throws IOException {
        try{
            File file = new File(f);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;         
            byte[] bytesn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                int pos = (i - (k)) % tam;
                if (pos < 0) {
                    pos += tam;
                }
                bytesn[pos] = bytes[i];
            }
            Archivos.writeBytes(bytesn, f);
            return "file: "+f+" cifrado con descifraByteRot = "+k;
        }catch(Exception e){
            return "Error@descifraByteRot file: "+f+" rot = "+k;
        }
    }

    /*
     Toma una ruta a un archivo
     y un byte y guarda un archivo con la info del archivo 
     parametro XOR el byte
     */
    public static String cifraByteXOR(String f, int k) throws IOException {
        try{
            File file = new File(f);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;
            byte[] bytesn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                bytesn[i] = (byte) (bytes[i] ^ (byte) k);
            }
            Archivos.writeBytes(bytesn, f);
            return "file: "+f+" cifrado con cifraByteXOR = "+k;
        }catch(Exception e){
            return "Error@cifraByteXOR file: "+f+" rot = "+k;
        }
    }

    /*
     Toma una ruta a un archivo
     y un byte y guarda un archivo con la info del archivo 
     parametro XOR el byte
     */
    public static String descifraByteXOR(String f, int k) throws IOException {
        try{
            File file = new File(f);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;
            byte[] bytesn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                bytesn[i] = (byte) (bytes[i] ^ (byte) k);
            }
            Archivos.writeBytes(bytesn, f);
            return "file: "+f+" descifrado con descifraByteXOR = "+k;
        }catch(Exception e){
            return "Error@descifraByteXOR file: "+f+" rot = "+k;
        }
    }

    public static String cifraByteRotXOR(String f, int rot, int bytte) throws IOException {
        try{
            File file = new File(f);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;
            byte[] bytesn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                int pos = (i + (rot)) % tam;
                if (pos < 0) {
                    pos += tam;
                }
                bytesn[pos] = bytes[i];
            }
            tam = bytesn.length;
            byte[] bytesnn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                bytesnn[i] = (byte) (bytesn[i] ^ (byte) bytte);
            }
            Archivos.writeBytes(bytesnn, f);
            return "file: "+f+" cifrado con cifraByteRotXOR = "+rot+" b = "+bytte;
        }catch(Exception e){
            return "Error@cifraByteRotXOR = "+rot+" b = "+bytte+" "+e.getMessage();
        }
    }

    public static String descifraByteRotXOR(String f, int rot, int bytte) throws IOException {
        try{
            File file = new File(f);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;
            byte[] bytesn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                bytesn[i] = (byte) (bytes[i] ^ (byte) bytte);
            }
            tam = bytesn.length;
            byte[] bytesnn = new byte[tam];
            for (int i = 0; i < bytes.length; i++) {
                int pos = (i - (rot)) % tam;
                if (pos < 0) {
                    pos += tam;
                }
                bytesnn[pos] = bytesn[i];
            }
            Archivos.writeBytes(bytesnn, f);
            return "file: "+f+" descifrado con descifraByteRotXOR = "+rot+" b = "+bytte;
        }catch(Exception e){
            return "Error@descifraByteRotXOR = "+rot+" b = "+bytte+" "+e.getMessage();
        }
    }

    public static String cifraTransposicionInversa(String archivo) throws IOException {
        try{
            File file = new File(archivo);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;
            byte[] binversos = new byte[tam];
            for (int i = bytes.length - 1; i != -1; i--) {
                binversos[tam - (i + 1)] = bytes[i];
            }
            Archivos.writeBytes(binversos, archivo);
            return "file: "+archivo+" cifrado con cifraTransposicionInversa";
        }catch(Exception e){
            return "Error@cifraTransposicionInversa = "+e.getMessage();
        }
    }

    public static String descifraTransposicionInversa(String archivo) throws IOException {
        try{
            File file = new File(archivo);
            byte[] bytes = Archivos.readBytes(file);
            int tam = bytes.length;
            byte[] binversos = new byte[tam];
            for (int i = bytes.length - 1; i != -1; i--) {
                binversos[tam - (i + 1)] = bytes[i];
            }
            Archivos.writeBytes(binversos, archivo);
            return "file: "+archivo+" descifrado con descifraTransposicionInversa";
        }catch(Exception e){
            return "Error@descifraTransposicionInversa = "+e.getMessage();
        }
    }

    /* 
     Regresa una llave de DES
     */
    public static SecretKey getDESKey() throws NoSuchAlgorithmException {
        try{
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey desKey = keygenerator.generateKey();
            return desKey;
        }catch(Exception e){
            System.out.println("Error@getDESKey "+e.getMessage());
        }
        return null;
    }

    public static String cifraArchivosDES(String archivo, SecretKey key) throws IOException {        
        try {
            Cipher desCipher;
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, key);
            System.out.println("archio en cifraArchivosDES es "+archivo);
            byte[] bytes = Archivos.readBytes(new File(archivo));
            String bytesab64 = Archivos.bytesToBase64(bytes);
            byte[] bytesde64 = Archivos.base64ToBytes(bytesab64);
            byte[] textEncrypted = desCipher.doFinal(bytesde64);
            Archivos.writeBytes(textEncrypted, archivo);
            return "file: "+archivo+" cifrado con cifraArchivosDES";
        } catch (Exception e) {
            return "Error@cifraArchivoDES file: "+archivo+" "+e.getMessage();
        }
    }
   
    public static String descifraArchivoDES(String archivo, SecretKey key) {
        try {
            Cipher desCipher;
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] fromFile = Archivos.readBytes(new File(archivo));
            byte[] decrypted = desCipher.doFinal(fromFile);
            Archivos.writeBytes(decrypted, archivo);
            return "file: "+archivo+" descifrado con descifraArchivosDES";
        } catch (Exception e) {
            return "Error@descifraArchivoDES file: "+archivo+" "+e.getMessage();
        }
    }
    
    /* returns the first 8 chars from the String */
    public static String fixPass(String pwd){
        if(pwd.length()>=8)
            return pwd.substring(0,8);
        return "";
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        System.out.println(descifraTransposicionInversa("C:\\Users\\Dell\\Desktop\\Prueba.java"));       
    }
}