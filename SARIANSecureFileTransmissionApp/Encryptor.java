
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private static final String ALGORITHM = "AES";


    public static byte[] encrypt(byte[] data, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new IllegalArgumentException("Key must be 16 characters long");
        }
        SecretKeySpec k = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }


    public static byte[] decrypt(byte[] encryptedData, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new IllegalArgumentException("Key must be 16 characters long");
        }
        SecretKeySpec k = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(encryptedData);
    }
}