package demo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class SymmetricKeyEncryptionDemo {

    public static void main(String[] args) throws Exception {

        // Create AES key and initialization vector
        SecretKey aesKey = createAesKey();
        IvParameterSpec aesInitVector = createAesInitVector();

        // Encrypt file
        try (FileInputStream inputStream = new FileInputStream(args[0]);
             FileOutputStream outputStream = new FileOutputStream("encrypted-file")) {

            aesEncrypt(inputStream, outputStream, aesKey, aesInitVector);
        }

        // Decrypt file
        try (FileInputStream inputStream = new FileInputStream("encrypted-file");
             FileOutputStream outputStream = new FileOutputStream("decrypted-file")) {

            aesDecrypt(inputStream, outputStream, aesKey, aesInitVector);
        }
    }

    private static SecretKey createAesKey() throws Exception
    {
        // Generate a pseudo-random AES encryption key (here we use a 256-bit key)
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();
        return key;
    }

    private static IvParameterSpec createAesInitVector() throws Exception
    {
        // Generate a pseudo-random AES "initialization vector" (128 bits)
        var ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }

    private static void aesEncrypt(InputStream inputStream, OutputStream outputStream,
                                   SecretKey key, IvParameterSpec initVector) throws Exception {

        runAes(Cipher.ENCRYPT_MODE, inputStream, outputStream, key, initVector);
    }

    private static void aesDecrypt(InputStream inputStream, OutputStream outputStream,
                                   SecretKey key, IvParameterSpec initVector) throws Exception {

        runAes(Cipher.DECRYPT_MODE, inputStream, outputStream, key, initVector);
    }

    private static void runAes(int cipherMode, InputStream inputStream, OutputStream outputStream,
                                SecretKey key, IvParameterSpec initVector) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(cipherMode, key, initVector);

        byte[] inputBytes = new byte[64];
        byte[] outputBytes = null;

        int bytesRead;
        while ((bytesRead = inputStream.read(inputBytes)) != -1) {
            outputBytes = cipher.update(inputBytes, 0, bytesRead);
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
        }

        outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }

        outputStream.flush();
    }
}
