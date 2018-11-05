/**
 * Alex Merk
 */
public class Question4 {

    //Permutation of bytes
    private static int[] S = new int[256];
    private static byte[] key = new byte[256];

    public static void main(String[] args) {
        key = "Key".getBytes();
        KSA(key);
        PRGA("Plaintext".getBytes());

    }

    //Key Scheduling Algorithm
    private static void KSA(byte[] key) {
        for (int i = 0; i < 256; i++) {
            S[i] = i;
        }

        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + key[i % key.length]) % 256;
            int temp = S[i];
            S[i] = S[j];
            S[j] = temp;
        }
    }

    //Encrypt/Decrypt
    private static byte[] PRGA(byte[] message) {
        byte[] c = new byte[message.length];
        int i = 0;
        int j = 0;
        for (int l = 0; l < message.length; l++) {
            i = (i + 1) % 256;
            j = (j + S[i]) % 256;
            int temp = S[j];
            S[j] = S[i];
            S[i] = temp;
            int K = S[(S[i] + S[j]) % 256];
            c[l] = (byte) (message[l] ^ K);
        }
        return c;
    }
}
