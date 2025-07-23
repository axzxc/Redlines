import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.Key;
import java.util.Base64;

public class EncryptionDecryption {

    public static byte[] loadKeyFromFile(String filename) throws Exception {
        return Files.readAllBytes(new File(filename).toPath());
    }

    public static String encrypt(String plainText, byte[] key) throws Exception {
    	if (plainText.equals(""))
    		return "";
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    public static void encryptFile(String inputFilePath, String outputFilePath, byte[] key) throws Exception {
        File inputFile = new File(inputFilePath);
        byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
        
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(inputBytes);
        
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(encryptedBytes);
        }
    }

    public static void decryptFile(String inputFilePath, String outputFilePath, byte[] key) throws Exception {
        File inputFile = new File(inputFilePath);
        byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
        
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(inputBytes);
        
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(decryptedBytes);
        }
    }
}
