# Cryptographic Hashing

🖥️ [Slides](https://docs.google.com/presentation/d/1pUU4DDACUndgj_ij7bbKOUY8cxmGinZq/edit#slide=id.p1)

🖥️ [Lecture Videos](#videos)

📖 **Required Reading**: None

### 🔑 Key points

- Cryptographic hash functions
- Secure password storage and verification
- Bcrypt

---

Cryptographic hashing is a cornerstone of modern computer security, providing a robust mechanism for ensuring data integrity and verifying authenticity. By applying a mathematical algorithm to input data of any size, a hash function produces a unique, fixed-length character string known as a "digest" or "fingerprint." This process is vital for protecting sensitive information because it allows systems to confirm that data has not been tampered with; even a single-bit change in the original input results in a vastly different output. From securing password databases by storing hashes instead of plain text to validating the legitimacy of software downloads through digital signatures, cryptographic hashing serves as an essential safeguard against unauthorized modification and data breaches.

## Cryptographic Hash Functions

A hash function is a mathematical function that converts data of arbitrary size into a fixed-size value. The value returned by a hash function is often called a hash value, hash code, digest, or simply a hash.

Desirable features of a cryptographic hash function include:

- **Fixed-Size**: The digest (output) is always the same size (e.g., 256 bits), regardless of the input size.
- **Deterministic**: Given the same input, it always produces the same digest.
- **One-Way**: Given a digest, it is computationally infeasible to recover the original input.
- **Collision Resistance**: It should be extremely difficult to find two different input values that produce the same digest.
- **Preimage Resistance**: Given a digest, it should be difficult to find any input value that produces that specific digest.

There are many algorithms for computing digests. The following table lists common ones along with their limitations and benefits.

| Hash function | Benefits                                                               | Limitations                                                                |
| ------------- | ---------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| **MD5**       | Simple, fast, widely available                                         | Vulnerable to collision attacks; considered insecure for cryptographic use |
| **SHA-1**     | Historically significant, widely available                             | Vulnerable to collision attacks; considered insecure for cryptographic use |
| **SHA-256**   | Secure against known attacks, industry standard                        | Slower than MD5 and SHA-1                                                  |
| **Bcrypt**    | Specifically designed for password hashing; secure against brute force | Intentionally slow; not suitable for general data integrity checks         |

If you are using macOS or Linux, you can use the `shasum` command-line utility to generate a hash using the **SHA-256** algorithm.

```sh
➜ echo -n "Fox" | shasum -a 256
f55bd2cdfae7972827638f3691a5bc189199d7cff7188d5ead489afdea0e5403
```

The following Java code demonstrates how to achieve the same result.

```java
package demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class CryptoHashFunctionDemo {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static void main(String[] args) throws NoSuchAlgorithmException {

        String[] allInputs = new String[]{
                "Fox",
                "Fox",
                "The red fox jumps over the blue dog",
                "The red fox jumps oѵer the blue dog",
        };

        for (String input : allInputs) {
            // Convert character string to array of bytes
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // Calculate message digest
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digestBytes = md.digest(inputBytes);

            System.out.println("Input: " + input);
            System.out.println("Hash:  " + bytesToHex(digestBytes));
            System.out.println();
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
```

Both methods generate the same output for the word `Fox`. Notice that changing the case of the text or including whitespace will produce entirely different results.

### Creating Signatures

One primary use for a hash function is to create a unique, short, fixed-size signature that represents data blocks of arbitrary length. A signature acts as a compact representation of the data, allowing you to quickly determine if two blocks of data are identical.

Key properties for generating signatures include speed, fixed size, determinism, and collision resistance. While MD5 and SHA-1 are insecure for sensitive data, they are sometimes still used for non-security tasks like file identification. For example, Git uses SHA-1 to generate a unique signature for all data in a given commit.

### Securing Passwords

Because hash functions are one-way and preimage resistant, they are ideal for storing passwords. Under this model, you hash the user's password when they register and store only the hash in your database. To authenticate a user later, you hash the password they provide at login and compare it to the stored hash.

Storing hashes instead of plain text is vital. Consider a database storing passwords in plain text:

| User  | Password       |
| ----- | -------------- |
| sally | toomanysecrets |
| juan  | p@33w0r6       |
| pat   | qwerty1        |

If this database is compromised, the attacker gains every user's credentials. Because users often reuse passwords across different sites, a breach in your application could expose their bank accounts or email.

Hashing prevents attackers from simply reading passwords:

| User  | HashedPassword (SHA-1)                   |
| ----- | ---------------------------------------- |
| sally | fc80b22ff203a1a88470b19ab19228044d066d66 |
| juan  | adb4c36db0466b9750a7a298ef39f98159eb219d |
| pat   | ad70ab97ae1376e656002641cfb067c9c94906a2 |

However, an attacker can still use a **rainbow table attack**. A rainbow table is a large database of precomputed hash codes for millions of common passwords. If an attacker finds your stored hash in their table, they immediately know the original password.

To combat rainbow table attacks, we use a **salt**—a random sequence of characters added to the password before hashing. A unique salt for every user ensures that even if two users have the same password, their hashes will be different.

| User  | SaltedHashedPassword                           |
| ----- | ---------------------------------------------- |
| sally | 37581:a67d83d2f75f240fe223ea899757a6980dee7d50 |
| juan  | 92734:79241300a93bdda742159f1902db461b55be3982 |
| pat   | 84723:fdfa32ac48e82803daa5b2a0849fb3c080b09cec |

The following diagram demonstrates the flow for using salted passwords:

![Salting](salting.png)

The salt does not need to be encrypted; it is stored in the database alongside the hash. It simply ensures that an attacker cannot use a generic rainbow table. They would have to compute a new table for every individual salt, which is computationally impractical.

| Representation    | Benefit                                                               |
| ----------------- | --------------------------------------------------------------------- |
| Plain text        | None (dangerous)                                                      |
| Hashed            | Passwords are not immediately readable if the database is compromised |
| Hashed and salted | Protects against precomputed rainbow table attacks                    |

### Bcrypt

To further protect passwords, we use hashing algorithms that are "expensive" to calculate. Modern hardware with Graphics Processing Units (GPUs) can compute millions of SHA-256 hashes per second. **Bcrypt** was created to be intentionally slow. While SHA-256 might check millions of passwords per second, Bcrypt might only check a few thousand on the same hardware. This "work factor" makes brute-force attacks much more difficult.

You can use the following library for Bcrypt in Java:

```
org.mindrot:jbcrypt:0.4
```

Bcrypt handles salting automatically. The following example hashes a password and then verifies it against candidates.

```java
import org.mindrot.jbcrypt.BCrypt;

public class PasswordExample {

    public static void main(String[] args) {
        String secret = "toomanysecrets";
        // gensalt() generates a random salt and includes the work factor
        String hash = BCrypt.hashpw(secret, BCrypt.gensalt());

        String[] passwords = {"cow", "toomanysecrets", "password"};
        for (var pw : passwords) {
            var match = BCrypt.checkpw(pw, hash) ? "==" : "!=";
            System.out.printf("%s %s %s%n", pw, match, secret);
        }
    }
}
```


## The Mathematical Foundations of Hash Functions

Cryptographic hash functions are mathematical algorithms that transform an arbitrary amount of data into a fixed-size bitstring, known as the "hash" or "digest." To be useful in security, these functions must satisfy several rigorous mathematical properties. At their core, these functions rely on **modular arithmetic** and **bitwise operations** (like XOR, AND, and bit-rotations) to create a "mixing" effect that is computationally easy to perform in one direction but virtually impossible to reverse.

One of the defining mathematical characteristics is the **Pigeonhole Principle**. Because the input space is infinite (you can hash a file of any size) but the output space is finite (e.g., exactly 256 bits for SHA-256), mathematical "collisions"—where two different inputs produce the same output—must exist. However, the math is designed so that finding such a collision is statistically improbable, requiring a "brute force" effort of `2^{n/2}` operations, where `n` is the bit length.

### Key Mathematical Properties

The security of a hash function is measured by its resistance to three types of attacks:

1.  **Pre-image Resistance (One-wayness):** Given a hash `h`, it should be computationally infeasible to find any input `m` such that `hash(m) = h`.
2.  **Second Pre-image Resistance:** Given an input `m_1`, it should be infeasible to find a different input `m_2` such that `hash(m_1) = hash(m_2)`.
3.  **Collision Resistance:** It should be infeasible to find *any* two distinct inputs `m_1` and `m_2` such that `hash(m_1) = hash(m_2)`.

### The Avalanche Effect

A mathematically sound hash function must exhibit the **Avalanche Effect**. This means that a tiny change in the input (like flipping a single bit) must result in a significantly different output, typically affecting at least half of the output bits. This is achieved through multiple "rounds" of complex bitwise transformations.

```mermaid
%%{init: { 'theme': 'neutral', 'themeVariables': { 'mainBkg': '#ffffff', 'lineColor': '#000000', 'primaryTextColor': '#000000', 'actorBorder': '#000000', 'participantBorder': '#000000', 'noteBorderColor': '#000000' } }}%%
flowchart TD
    Input[Input Data: 'Cat'] --> Pad[Padding & Pre-processing]
    Pad --> Round1[Round 1: Bitwise XOR & Rotations]
    Round1 --> Round2[Round 2: Non-linear Functions]
    Round2 --> RoundN[Round ...N]
    RoundN --> Digest[Final Hash: 0x77e1...]
    
    Input2[Input Data: 'Car'] --> Pad2[Padding & Pre-processing]
    Pad2 --> R1[Round 1]
    R1 --> R2[Round 2]
    R2 --> RN[Round ...N]
    RN --> Digest2[Final Hash: 0x3a4f...]
    
    style Digest fill:#f9f,stroke:#333,stroke-width:2px
    style Digest2 fill:#f9f,stroke:#333,stroke-width:2px
```

### Simplified Mathematical Representation

While real-world algorithms like SHA-256 are complex, we can represent the logic of a "one-way" bitwise operation using a simple Python-like logic. Note how the information is "lost" or compressed during the operation, making it hard to reverse.

```java
public static String simpleToyHash(String data) {
    long hashVal = 0xDEADBEEFL;
    for (char c : data.toCharArray()) {
        // Use bitwise XOR and left-shift to diffuse information
        hashVal = (hashVal ^ c) << 5;
        // Keep a fixed-length output of 32-bit
        hashVal = hashVal & 0xFFFFFFFFL;
    }
    return "0x" + Long.toHexString(hashVal);
}

// "A" -> 0xbd57dd00
// "B" -> 0xbd57df00 (Small change in input leads to change in output)
```

```masteryls
{"id":"ae1d11ef-44e6-4c1f-9a61-cb7ee6bd9dda","title":"Identifying Collision Resistance","type":"multiple-choice"}
A security researcher is trying to find two different PDF files that result in the exact same SHA-256 hash value. Which mathematical property is the researcher attempting to break?

- [ ] Pre-image Resistance
  You're thinking of the right area, but pre-image resistance specifically refers to finding the original input from a known hash. Here, the researcher is looking for any two inputs that match, which is a different challenge.
- [ ] The Avalanche Effect
  Not quite. The avalanche effect describes how a small change in input changes the output significantly. While related to hash quality, it doesn't describe the difficulty of finding two identical outputs.
- [x] Collision Resistance
  Correct! Collision resistance is the property that makes it computationally infeasible to find any two distinct inputs that produce the same hash output. This is a vital property for digital signatures and file integrity.
- [ ] Determinism
  Actually, determinism is a requirement that the same input *always* produces the same output. If a hash function wasn't deterministic, it wouldn't be useful for verification at all!
```


## Videos

- 🎥 [Computer Security Overview (7:49)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f60bd2e6-9aec-44ba-8e48-b1a8014a2efe) - [[transcript]](https://github.com/user-attachments/files/17736619/CS_240_Computer_Security_Overview_Transcript.pdf)
- 🎥 [Cryptographic Hash Functions (10:05)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e54b188b-89a4-4f51-be13-b1a8014d0a7b) - [[transcript]](https://github.com/user-attachments/files/17736652/CS_240_Cryptographic_Hash_Functions_Transcript.pdf)
- 🎥 [Cryptographic Hashing Applications (5:29)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=44f59163-ab0a-410b-a8bb-b1a8015012bf) - [[transcript]](https://github.com/user-attachments/files/17736656/CS_240_Cryptographic_Hashing_Applications_Transcript.pdf)
- 🎥 [Secure Password Storage (15:06)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f977577d-6fec-45a9-8e80-b1a80151efc1) - [[transcript]](https://github.com/user-attachments/files/17736661/CS_240_Secure_Password_Storage_Transcript.pdf)
