# Computer Security

🖥️ [Slides](https://docs.google.com/presentation/d/1pUU4DDACUndgj_ij7bbKOUY8cxmGinZq/edit#slide=id.p1)

📖 **Required Reading**: None

Software must be developed with security in mind. Bad actors (hackers) try to compromise systems in a variety of ways. They may try to gain unauthorized access to data and computers for the purposes of stealing, monitoring, damaging, or otherwise misusing these assets. Your designs must include measures to prevent these illicit activities. Additionally, crypto currency is becoming an increasingly important part of the economy. With financial transactions being conducted in a digital environment, these systems must be securely implemented so transactions can be trusted and money cannot be stolen. This topic discusses several core concepts and technologies that form the basis of secure computer systems.

## Cryptographic Hash Functions

- **One-Way**: Given the Digest (or output), you cannot recover the Input
- **Deterministic**: Given the same Input, it produces the same Digest (or output)
- **Fixed-Size**: The Digest is always the same size (e.g., 160 bits), regardless of the Input size
- **Pseudo-Random**: The output seems statistically random, even though it is not (small change to Input makes the Digest totally different)

```java
package demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;


public class CryptoHashFunctionDemo {

    public static void main(String[] args) throws NoSuchAlgorithmException {

       String[] allInputs = new String[]{
             "Fox",
             "Fox",
             "The red fox jumps over the blue dog",
             "The red fox jumps ouer the blue dog",
       };

       for (String input : allInputs) {
          // Convert character string to array of bytes
          byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

          // Calculate message digest
          byte[] digestBytes = hashData(inputBytes);

          System.out.println(input);
          System.out.println(Utils.bytesToHex(digestBytes));
          System.out.println();
       }
    }

    public static byte[] hashData(byte[] data) throws NoSuchAlgorithmException {

       MessageDigest md = MessageDigest.getInstance("SHA-256");

       byte[] digest = md.digest(data);

       return digest;
    }
}
```

### Hashing Passwords

org.springframework.security:spring-security-core:5.7.1

````java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordExample {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = "toomanysecrets";
        String hash = encoder.encode(secret);

        String[] passwords = {"cow", "toomanysecrets", "password"};
        for (var pw : passwords) {
            var match = encoder.matches(pw, hash) ? "==" : "!=";

            System.out.printf("%s %s %s%n", pw, match, secret);
        }
    }
}
```

```txt
cow != toomanysecrets
toomanysecrets == toomanysecrets
password != toomanysecrets
```

## Encryption and Decryption

### Symmetric Key Encryption

### Asymmetric Key Encryption

## Secure Key Exchange

## Secure Communication

## Digital Signatures

## Things to Understand

- High-level goals of computer security
  - Data confidentiality
  - Authentication
  - Data integrity
  - Non-Repudiation
- Fundamental security concepts and technologies
  - Cryptographic hash functions
  - Symmetric data encryption
  - Asymmetric (ie, Public Key) data encryption with public and private keys
  - Secure key exchange
  - Digital signatures
  - Public key certificates
- Secure password storage and verification
- Secure network communication using HTTPS (ie, SSL/TLS)

## Demonstration code

📁 [Cryptographic Hash Function Example](example-code/src/demo/CryptoHashFunctionDemo.java)

📁 [Password Hashing and Verification Example](example-code/src/demo/PBKDF2WithHmacSHA1Hashing.java)

📁 [Symmetric Key Encryption Example](example-code/src/demo/SymmetricKeyEncryptionDemo.java)

📁 [Public Key Encryption Example](example-code/src/demo/PublicKeyEncryptionDemo.java)

📁 [Security Utilities](example-code/src/demo/Utils.java)
````
