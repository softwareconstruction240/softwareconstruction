package demo;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;


public class PublicKeyEncryptionDemo {

    public static void main(String[] args) throws Exception {

        // Create and save RSA key pair
        KeyPair keyPair = createRsaKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String pubFormat = publicKey.getFormat();
        String prvFormat = privateKey.getFormat();
        Files.write(Paths.get("public-key"), publicKey.getEncoded());
        Files.write(Paths.get("private-key"), privateKey.getEncoded());

        Key encryptionKey = publicKey;
        Key decryptionKey = privateKey;
//        Key encryptionKey = privateKey;
//        Key decryptionKey = publicKey;

        final String PLAIN_TEXT = "Four score and seven years ago our fathers brought forth, upon this continent," +
                " a new nation, conceived in Liberty, and dedicated to the proposition that all men are created equal.";

        byte[] plainTextBytes = PLAIN_TEXT.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        // Encrypt data
        inputStream = new ByteArrayInputStream(plainTextBytes);
        outputStream = new ByteArrayOutputStream();
        rsaEncrypt(inputStream, outputStream, encryptionKey);

        byte[] cipherTextBytes = outputStream.toByteArray();
        System.out.println(Utils.bytesToHex(cipherTextBytes));
        System.out.println();

        // Decrypt data
        inputStream = new ByteArrayInputStream(cipherTextBytes);
        outputStream = new ByteArrayOutputStream();
        rsaDecrypt(inputStream, outputStream, decryptionKey);

        byte[] decryptedPlainTextBytes = outputStream.toByteArray();
        String decryptedPlainText = new String(decryptedPlainTextBytes, StandardCharsets.UTF_8);
        System.out.println(decryptedPlainText);
        System.out.println();
    }

    private static KeyPair createRsaKeyPair() throws Exception
    {
        var keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    private static void rsaEncrypt(InputStream inputStream, OutputStream outputStream, Key key) throws Exception {

        runRsa(Cipher.ENCRYPT_MODE, inputStream, outputStream, key);
    }

    private static void rsaDecrypt(InputStream inputStream, OutputStream outputStream, Key key) throws Exception {

        runRsa(Cipher.DECRYPT_MODE, inputStream, outputStream, key);
    }

    private static void runRsa(int cipherMode, InputStream inputStream, OutputStream outputStream,
                               Key key) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(cipherMode, key);

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
