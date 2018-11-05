import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Alex Merk
 */
public class Question2 {

    private static String key1;
    private static String key2;
    static byte[] IV = new byte[16];
    static byte[] c;
    static byte[] t;
    static byte[] m;
    static ArrayList<Byte> temp = new ArrayList<>();

    /**
     * alg/mode/padding or alg
     * Encrypt using AES in CTR mode of operation
     * @param args
     */
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, IOException, BadPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException {

        //User input
        Scanner input = new Scanner(System.in);
        System.out.println("Select an option");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        int choice = input.nextInt();
        System.out.println("Enter file");
        String file = input.next();
        System.out.println("Enter key 1: ");
        key1 = input.next();
        while (key1.length() != 16 && key1.length() != 32) {
            System.out.println("Key length must be 16 or 32.");
            System.out.println("Enter key: ");
            key1 = input.next();
        }
        System.out.println("Enter key 2: ");
        key2 = input.next();
        while (key2.length() != 16 && key2.length() != 32) {
            System.out.println("Key length must be 16 or 32.");
            System.out.println("Enter key: ");
            key2 = input.next();
        }

        //Build cipher and keys
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        SecretKeySpec encK = new SecretKeySpec(key1.getBytes(), "AES");
        SecretKeySpec encM = new SecretKeySpec(key2.getBytes(), "HmacSHA512");

        //Encrypt option
        if (choice == 1) {
            //Encrypt then MAC
            encrypt(encK, file, cipher);
            MAC(c, encM);

            //Write results
            StringBuilder output = new StringBuilder();
            output.append("IV: " + Arrays.toString(IV) + "\n");
            output.append("t: " + Arrays.toString(t) + "\n");
            output.append("c: " + Arrays.toString(c));
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(output.toString());
            writer.close();
        }

        //Decrypt option
        else {
            //Read input
            File f = new File(file);
            Scanner s = new Scanner(f);

            //IV
            String line = s.nextLine();
            line = line.substring(5, line.length() - 1);
            String[] temp = line.split(",");
            IV = new byte[temp.length];
            for (int i = 0; i < IV.length; i++) {
                IV[i] = Byte.parseByte(temp[i].trim());
            }

            //t
            line = s.nextLine();
            line = line.substring(4, line.length() - 1);
            temp = line.split(",");
            t = new byte[temp.length];
            for (int i = 0; i < t.length; i++) {
                t[i] = Byte.parseByte(temp[i].trim());
            }

            //c
            line = s.nextLine();
            line = line.substring(4, line.length() - 1);
            temp = line.split(",");
            c = new byte[temp.length];
            for (int i = 0; i < c.length; i++) {
                c[i] = Byte.parseByte(temp[i].trim());
            }

            //Verify
            if (VRFY(c, t, encM)) {
                decrypt(encK, c, cipher);
                System.out.println(new String(m));
            } else {
                System.out.println("Error: The tags do not match.");
            }
        }
    }

    private static void encrypt(SecretKeySpec key1, String file, Cipher c) throws
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, IOException {

        //Read in file
        File f = new File(file);
        Scanner s = new Scanner(f);
        StringBuilder sb = new StringBuilder();
        while (s.hasNextLine()) {
            sb.append(s.nextLine());
        }
        System.out.println(sb.toString());
        System.out.println(sb.toString());
        byte[] input = sb.toString().getBytes();

        //Initialize encryption variables
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(IV);
        IvParameterSpec iv = new IvParameterSpec(IV);
        long x = System.currentTimeMillis();
        c.init(c.ENCRYPT_MODE, key1, iv);
        Question2.c = c.doFinal(input);
        System.out.println("Time taken: " + (System.currentTimeMillis() - x));
    }

    private static void decrypt(SecretKeySpec key1, byte[] c, Cipher cipher) throws InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        IvParameterSpec iv = new IvParameterSpec(IV);
        cipher.init(cipher.DECRYPT_MODE, key1, iv);
        m = cipher.doFinal(c);
    }

    private static void MAC(byte[] c, SecretKeySpec key2) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(key2);
        t = mac.doFinal(c);
    }

    private static boolean VRFY(byte[] c, byte[] t, SecretKeySpec key2) throws NoSuchAlgorithmException,
            InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(key2);
        byte[] t2 = mac.doFinal(c);
        return Arrays.equals(t, t2);
    }
}
