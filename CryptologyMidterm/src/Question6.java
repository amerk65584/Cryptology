import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 * Alex Merk
 */
public class Question6 {
    public static long time;
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException,
            FileNotFoundException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        //read file
        File f = new File("pg10.txt");
        Scanner s = new Scanner(f);
        StringBuilder sb = new StringBuilder();
        while (s.hasNextLine()) {
            sb.append(s.nextLine());
        }
        byte[] input = sb.toString().getBytes();

        //Setup IV, Keys
        byte[] IV = new byte[16];
        byte[] IV2 = new byte[8];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(IV);
        sr.nextBytes(IV2);
        IvParameterSpec iv = new IvParameterSpec(IV);
        IvParameterSpec iv2 = new IvParameterSpec(IV2);
        SecretKeySpec keyAES = new SecretKeySpec("1234123412341234".getBytes(), "AES");
        SecretKeySpec keyDES = new SecretKeySpec("12345678".getBytes(), "DES");
        SecretKeySpec keyDESede = new SecretKeySpec("123456781234567812345678".getBytes(), "DESede");

        //AES CBC
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyAES, iv);
        time = System.currentTimeMillis();
        cipher.doFinal(input);
        System.out.println("AES in CBC: " + (System.currentTimeMillis() - time));

        //AES CTR
        cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyAES, iv);
        time = System.currentTimeMillis();
        cipher.doFinal(input);
        System.out.println("AES in CTR: " + (System.currentTimeMillis() - time));

        //DES CBC
        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyDES, iv2);
        time = System.currentTimeMillis();
        cipher.doFinal(input);
        System.out.println("DES in CBC: " + (System.currentTimeMillis() - time));

        //DES CTR
        cipher = Cipher.getInstance("DES/CTR/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyDES, iv2);
        time = System.currentTimeMillis();
        cipher.doFinal(input);
        System.out.println("DES in CTR: " + (System.currentTimeMillis() - time));

        //3DES CBC
        cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyDESede, iv2);
        time = System.currentTimeMillis();
        cipher.doFinal(input);
        System.out.println("3DES in CBC: " + (System.currentTimeMillis() - time));

        //3DES CTR
        cipher = Cipher.getInstance("DESede/CTR/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyDESede, iv2);
        time = System.currentTimeMillis();
        cipher.doFinal(input);
        System.out.println("3DES in CTR: " + (System.currentTimeMillis() - time));
    }

}
