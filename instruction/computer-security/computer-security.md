# Computer Security

🖥️ [Slides](https://docs.google.com/presentation/d/1pUU4DDACUndgj_ij7bbKOUY8cxmGinZq/edit#slide=id.p1)

📖 **Required Reading**: None

Software system conduct trillions of dollars in daily transactions, and manage access to billions personal records. This makes these systems a valuable target for attack. Bad actors try to compromise systems in a variety of ways. They may try to gain unauthorized access to data and computers for the purposes of stealing, monitoring, damaging, or otherwise misusing these assets. In order to mitigate their efforts you must include security as a primary design criteria, understand historical and current attack vectors, vigilantly monitor for intrusion, and continually enhance your systems as new threats evolve.

This topic focuses on the core concepts and technologies necessary to securely store and transmit data. The core concepts of computer security include the following.

- **Authentication**: Verifying the identity of an actor (e.g. user or system)
- **Authorization**: Enforcing the rights that an actor has to access data or perform restricted operations
- **Data Integrity**: Verifying that data has not been modified from its original form
- **Non-Repudiation**: Verifying, or not rejecting, the origin or authorship of data

Cryptography plays a key technological role in supporting these security concepts. Cryptography is used to authenticate users, represent their rights and identity, encrypt their data, and digitally sign messages. Without cryptography it would be very difficult to securely exchange money or information in digital form.

## Cryptographic Hash Functions

Let's take a look at our first cryptographic tool, hash functions. A hash function is a mathematical function that converts data of arbitrary size into a fixed-size value. The value returned by a hash function are often called a hash value, hash code, digest, or simply a hash.

Desirable features of a hash function include:

- **Fixed-Size**: The digest (or output) is always the same size (e.g., 160 bits), regardless of the input size
- **Deterministic**: Given the same input, it produces the same digest
- **One-Way**: Given the digest, you cannot recover the original text
- **Resistance to collisions**: It should be difficult to find two different input values that produce the same digest
- **Preimage resistance:** It should be difficult to find an input value that produces a given digest

There are many algorithms for computing digests. Here is a list of some of the more common ones along with their limitations and benefits.

| Hash function | Benefits                                                                               | Limitations                                                                           |
| ------------- | -------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------- |
| **MD5**       | Simple, fast, widely used, widely available                                            | Collision attacks have been found, considered insecure for cryptographic applications |
| **SHA-1**     | More secure than MD5, widely used, widely available                                    | Collision attacks have been found, considered insecure for cryptographic applications |
| **SHA-256**   | Secure against known attacks, widely used, widely available                            | Slower than MD5 and SHA-1, not as widely supported as MD5 and SHA-1                   |
| **Bcrypt**    | Password hashing function, secure against known attacks, widely used, widely available | Slower than SHA-256                                                                   |

If you are using macOS or linux you can use the `shasum` command console utility to generate a hash using the **SHA-256** algorithm.

```sh
➜ echo -n "Fox" | shasum -a 256
f55bd2cdfae7972827638f3691a5bc189199d7cff7188d5ead489afdea0e5403
```

The following demonstrates doing the same thing with java code.

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
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digestBytes = md.digest(inputBytes);

            System.out.println(input);
            System.out.println(bytesToHex(digestBytes));
            System.out.println();
        }
    }

    public static String bytesToHex(byte[] bytes) {
      byte[] hexChars = new byte[bytes.length * 2];
      for (int j = 0; j < bytes.length; j++) {
          int v = bytes[j] & 0xFF;
          hexChars[j * 2] = HEX_ARRAY[v >>> 4];
          hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
      }
      return new String(hexChars, StandardCharsets.UTF_8);
    }
}
```

Using either method should generate the same output for the word `Fox`. You can experiment with either the code or console command to see what happens with different inputs. Notice that changing the case of the text or including whitespace will produce different results.

### Creating Signatures

One primary use for a hash function is to create a unique, short, fixed size, signature that can represent data blocks of arbitrary length. With a signature you can use it as a compact representation of the data, and to determine equality to other data blocks.

The key properties of a hash function that is used for generating signatures include speed, fixed size, determinism, and resistance to collisions. Therefore algorithms such as MD5 and SHA-1 work well for this purpose. As an example, Git uses SHA-1 to generate a unique signature for all the data in a given commit.

### Securing Passwords

If a hash function is one-way and preimage resistant, then it is a good candidate for representing passwords. Under this model you would hash user's password when they register and then store it in the database. When you want to authenticate a user's password you simply hash the password at login, and compare it to the previously stored hashed value.

In order to understand why hashing passwords is valuable, consider the case where you simply store plain text representations of passwords in a database column.

| User  | Password       |
| ----- | -------------- |
| sally | toomanysecrets |
| juan  | p@33w0r6       |
| pat   | qwerty1        |

> Plain text passwords

If your database is every compromised then the attacker now has all of the credentials for all of your users. That might allow them access to valuable monetary, proprietary, or confidential data. Even if your application has little value to compromise, it is common for passwords to be reused on different website. For example, if Juan reused his super secret password `p@33w0r6` on his school account, bank application, and his shopping websites.

By hashing the passwords the attacker cannot simply read the password from the database and use it to log in to an account.

| User  | HashedPassword                           |
| ----- | ---------------------------------------- |
| sally | fc80b22ff203a1a88470b19ab19228044d066d66 |
| juan  | adb4c36db0466b9750a7a298ef39f98159eb219d |
| pat   | ad70ab97ae1376e656002641cfb067c9c94906a2 |

> Hashed passwords using SHA-1

However, even with a hashed version of the passwords a determined attacker can still succeed in revealing the credentials for your users by using what is known as a `rainbow table attack`. A `rainbow` table is a large database with an index of precomputed hash codes for common passwords and a specific hashing algorithm such as `SHA-256`. With such a table the hacker simply searches for the hashed password, and if there is a match then they know what the original password was and that `SHA-256` was used to hash it.

In order to combat a rainbow table attack, it is common to combine the password with a random sequence of characters, called a `salt`, for each hashed password. By combining the salt with the password plain text the combination becomes unique and therefore no longer vulnerable to a rainbow table attack because the attacker would have to compute a database for every possible password, salt value, and algorithm.

| User  | SaltedHashedPassword                           |
| ----- | ---------------------------------------------- |
| sally | 37581:a67d83d2f75f240fe223ea899757a6980dee7d50 |
| juan  | 92734:79241300a93bdda742159f1902db461b55be3982 |
| pat   | 84723:fdfa32ac48e82803daa5b2a0849fb3c080b09cec |

> Hashed passwords using SHA-1 and salt

Note that the salt is not encrypted. It can be simply stored in your database along with the hashed password. The idea is to simply make it difficult for the attacker to precompute the hashed value. With the salt you are making each hash unique.

| Representation    | Benefit                                                                           |
| ----------------- | --------------------------------------------------------------------------------- |
| Plain text        | The password is kept safe in a non-public database                                |
| Hashed            | The password in not immediately usable if the database is compromised             |
| Hashed and salted | Each individual password must be analyzed in order to be successfully compromised |

As a final measure to protect our user's passwords we want to use a hash algorithm that is expensive to calculate. That way it is difficult to create a table of precomputed passwords. For this reason algorithms such as `Bcrypt` were created to make it intentionally difficult to compute while still maintaining all of the other desirable characteristics of a password hashing algorithm

With modern hardware that utilizes graphical processing units (GPUs), it is possible to try millions of possible values per second for the `SHA-256` algorithm. By contrast the `Bcrypt` algorithm would only compute a few thousand results.

You can experiment with `Bcrypt` using the following library.

```
org.springframework.security:spring-security-core:5.7.1
```

This makes it easy to write code that hashes a password, with salt automatically included, and then later compares the hash to a candidate password. The following example first hashes a password and then compares it to three possible candidates.

```java
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

Here are the results of running the program.

```txt
cow != toomanysecrets
toomanysecrets == toomanysecrets
password != toomanysecrets
```

## Encryption and Decryption

In Cryptography, `encryption` is the process of encoding data so that it is unreadable. Decryption is the process of decoding data back to its original form.

Unlike hashing passwords, many applications need to both encrypt and decrypt information. For example, when you has to save confidential information such as a user's medical or financial records, you want to encrypt that data so that it is difficult to compromise, but you also need to be able to decrypt it so that it can be returned to the user on request.

In the world of cryptography, the unencrypted test is called plain text. The encrypted text is called cipher text. Algorithms that both encrypt and decrypt data utilize a sequence of bytes, called a key, that enable conversion. Typically, the longer the key size, the more difficult it will be to defeat the encryption.

| Term        | Purpose                                | Example             |
| ----------- | -------------------------------------- | ------------------- |
| Plain text  | unencrypted data                       | toomanysecrets      |
| Key         | value used to encrypt and decrypt data | 9012434289054653828 |
| Key size    | The length of the key                  | 1024 bits           |
| Cipher text | encrypted data                         | 88338012387532      |

Consider a simple encryption algorithm that added a number from one to 16, to each character of text. This would be easy to encrypt and decrypt text and only require a key size of four bits.

| Value       | Value          |
| ----------- | -------------- |
| Plain text  | toomanysecrets |
| Key         | 1              |
| Key size    | 4 bits         |
| Cipher text | uppoboztfdsfut |

You could implement this algorithm for both encryption and decryption with the following code.

```java
public class SimpleExample {
  public static void main(String[] args) {
      var plainText = "toomanysecrets".toCharArray();

      // encrypt
      var cipherText = new char[plainText.length];
      for (var i = 0; i < plainText.length; i++) {
          cipherText[i] = (char) (plainText[i] + 1);
      }

      // decrypt
      for (var i = 0; i < cipherText.length; i++) {
          plainText[i] = (char) (cipherText[i] - 1);
      }

      System.out.println(plainText);
      System.out.println(cipherText);
  }
}
```

However, our simple encryption algorithm would be easy to defeat because you could simply try all `16` possible values of the key to decrypt any cipher text. In order for an algorithm to be viable, it needs to have a large key and an algorithm that exploits complex mathematics. For example, a key size of 1024 bits could require as many as `2^1024` attempts, or:

````
1,797,693,134,862,315,907,729,305,190,789,024,733,617,976,978,942,306,572,734,300,811,577,326,758,055,009,631,327,084,773,224,075,360,211,201,138,798,713,933,576,587,897,688,144,166,224,928,474,306,394,741,243,777,678,934,248,652,763,022,196,012,460,941,194,530,829,520,850,057,688,381,506,823,424,628,814,739,131,105,408,272,371,633,505,106,845,862,982,399,472,459,384,797,163,048,353,563,296,242,241,372,160
``
This number is significantly larger than the estimated number of atoms in the observable universe, which is estimated to be around 10^80.

### Symmetric Key Encryption

The encryption code that was demonstrated above is an example of a symmetric key encryption algorithm because it uses the same key to both encryption and decryption. Symmetric encryption algorithms are attractive because they are very quick to compute and difficult to attack assuming that you have an appropriately sized key.

As we mentioned above a good encryption algorithm will also use complex mathematics to make it difficult to encrypt or decrypt without the proper key. One commonly used symmetric key algorithm is Advanced Encryption Standard (`AES`). This algorithm shifts blocks of characters around, across multiple rounds of manipulation, using a key size of 128, 192, or 256 bits. It also uses
a `initialization vector` to create a unique cipher value for each plain text, initialization vector, combination.

```java
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.io.*;

public class SymmetricKeyExample {
    public static void main(String[] args) throws Exception {
        SecretKey key = createAesKey();
        IvParameterSpec initVector = createAesInitVector();

        var secretMessage = "toomanysecrets";

        var plainTextIn = new ByteArrayInputStream(secretMessage.getBytes());
        var cipherTextOut = new ByteArrayOutputStream();
        runAes(Cipher.ENCRYPT_MODE, plainTextIn, cipherTextOut, key, initVector);

        var cipherTextIn = new ByteArrayInputStream(cipherTextOut.toByteArray());
        var plainTextOut = new ByteArrayOutputStream();
        runAes(Cipher.DECRYPT_MODE, cipherTextIn, plainTextOut, key, initVector);

        System.out.printf("%s == %s%n", secretMessage, plainTextOut);
    }

    static SecretKey createAesKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    static IvParameterSpec createAesInitVector() {
        var ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }

    static void runAes(int cipherMode, InputStream in, OutputStream out, SecretKey key, IvParameterSpec initVector) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(cipherMode, key, initVector);

        byte[] inputBytes = new byte[64];
        int bytesRead;
        while ((bytesRead = in.read(inputBytes)) != -1) {
            out.write(cipher.update(inputBytes, 0, bytesRead));
        }

        out.write(cipher.doFinal());
    }
}
````

### Asymmetric Key Encryption

The two keys must have a certain mathematical relationship, and so must be generated together (called a “key pair”)
Given one of the keys, it is infeasible to calculate the other key

1. Generate a key pair
1. Keep one of the keys secret. This is your “private key”
1. Give the other key to anyone who wants to send you data. This is your “public key”. There is no need to keep it secret.
1. When sending you data, the sender encrypts the data with your public key
1. When you receive the data, you decrypt the data with your private key
1. No one other than you can decrypt the data because only you have your private key
1. For this to work, it is very important that you keep your private key secret

- RSA: RSA is a mature and well-respected algorithm that is widely used in a variety of applications, including secure communication, digital signatures, and certificate authorities.
- Elliptic curve cryptography (ECC): ECC is a newer algorithm that is more efficient than RSA and offers comparable security. ECC is becoming increasingly popular in a variety of applications, including mobile devices and lightweight devices.

```java
import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

public class AsymmetricKeyExample {
    public static void main(String[] args) throws Exception {
        KeyPair key = createRsaKeyPair();

        final String secretMessage = "toomanysecrets";

        var plainTextIn = new ByteArrayInputStream(secretMessage.getBytes());
        var cipherTextOut = new ByteArrayOutputStream();
        runRsa(Cipher.ENCRYPT_MODE, plainTextIn, cipherTextOut, key.getPublic());

        var cipherTextIn = new ByteArrayInputStream(cipherTextOut.toByteArray());
        var plainTextOut = new ByteArrayOutputStream();
        runRsa(Cipher.DECRYPT_MODE, cipherTextIn, plainTextOut, key.getPrivate());

        System.out.printf("%s == %s%n", secretMessage, plainTextOut);
    }

    private static KeyPair createRsaKeyPair() throws Exception {
        var keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

    private static void runRsa(int cipherMode, InputStream inputStream, OutputStream outputStream, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(cipherMode, key);

        byte[] inputBytes = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(inputBytes)) != -1) {
            outputStream.write(cipher.update(inputBytes, 0, bytesRead));
        }

        outputStream.write(cipher.doFinal());
    }
}
```

Disadvantages of asymmetric key encryption

- Can only encrypt a small amount of data - E.g., RSA can only encrypt data less than the key size (e.g., 2048 bits, 4096 bits, etc.)
- Much slower than symmetric key encryption (e.g., AES). Symmetric key encryption should be used to encrypt large amounts of data
- Private keys must be securely stored and never shared with others

Why would you ever use asymmetric key encryption?

It has a number powerful applications, including:

- Secure Symmetric Key Exchange
- Digital Signatures

Asymmetric key cryptography is one of the most important inventions in the history of computing

## Secure Key Exchange

Encryption of large amounts of data should be done using symmetric key encryption
But, symmetric key encryption requires “Alice” and “Bob” to both use the same secret key. How do they both agree upon and obtain the secret key?
Email?, Text?, Phone Call?, Regular mail? List of secret keys agreed upon in advance?
All of these are insecure and/or inconvenient
Public key encryption can be used to securely and automatically share secret keys between sender and receiver

## Digital Signatures

Hash message. (signer digest)
Encrypt message using private key. (signature)

Hash message (receiver digest).
Decrypt signature.
Compare decrypted signature (signer digest) to receiver digest.

## Web Certificate

## Secure Web Communication (HTTPS)

Secure Key exchange.

Ask for public key.
Create a symmetric key. encode with public key.
Send to server.
Server decodes with private key.
Communication proceeds with symmetric key.

## Things to Understand

- High-level goals of computer security
  - Data confidentiality
  - Authentication
  - Data integrity
  - Non-Repudiation
- Fundamental security concepts and technologies
  - Cryptographic hash functions
  - Symmetric encryption
  - Asymmetric encryption with public and private keys
  - Secure key exchange
  - Digital signatures
  - Public key certificates
- Secure password storage and verification
- Secure network communication using HTTPS

## Demonstration code

📁 [Cryptographic Hash Function Example](example-code/src/demo/CryptoHashFunctionDemo.java)

📁 [Password Hashing and Verification Example](example-code/src/demo/PBKDF2WithHmacSHA1Hashing.java)

📁 [Symmetric Key Encryption Example](example-code/src/demo/SymmetricKeyEncryptionDemo.java)

📁 [Public Key Encryption Example](example-code/src/demo/PublicKeyEncryptionDemo.java)

📁 [Security Utilities](example-code/src/demo/Utils.java)
