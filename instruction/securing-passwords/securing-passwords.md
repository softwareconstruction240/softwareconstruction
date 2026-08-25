# Securing Passwords

### 🔑 Key points

- The importance of protecting user credentials
- Securely storing passwords using the BCrypt hashing algorithm
- Implementing password hashing and verification in Java

---

Whenever you accept personal information from a user, you become responsible for securing that data. One of the most critical pieces of information to protect is the user's password. If a password is exposed, you are essentially granting an attacker the ability to impersonate that user.

While we will explore the mathematical details of how cryptographic hash functions work in a later topic, this guide demonstrates how to use the BCrypt algorithm to securely hash and compare user passwords.

It is vital to use a secure storage method for passwords as part of your application's data persistence strategy.

### Securely Storing Passwords with BCrypt

The BCrypt algorithm allows you to take a plaintext password and irreversibly hash it. This process creates a "one-way" representation of the password. Because the process is irreversible, even if a nefarious party gains access to your database, they cannot easily retrieve the original plaintext passwords.

When a user attempts to log in, BCrypt allows you to compare the new password attempt against the stored hash. If the hash of the attempt matches the stored hash, you know the password is correct, even though you never stored the password itself.

By hashing passwords, your application never stores sensitive credentials in a readable format. You can still verify a user's identity while significantly reducing the risk of a data breach.

## Implementing BCrypt

You can use BCrypt in your application by including the following library in your project dependencies:

```
org.mindrot:jbcrypt:0.4
```

This implementation of BCrypt allows you to hash a password with a single line of code and later compare a candidate password against that hash with another. The following example hashes the password `toomanysecrets` and then compares it to three different candidates to check for a match.

```java
import org.mindrot.jbcrypt.BCrypt;

public class PasswordExample {

    public static void main(String[] args) {
        String secret = "toomanysecrets";
        
        // Hash a password for the first time
        String hash = BCrypt.hashpw(secret, BCrypt.gensalt());

        String[] candidates = {"cow", "toomanysecrets", "password"};
        
        for (var candidate : candidates) {
            // Check if a plaintext candidate matches the stored hash
            var match = BCrypt.checkpw(candidate, hash) ? "==" : "!=";

            System.out.printf("Candidate: %-15s %s Stored Secret%n", candidate, match);
        }
    }
}
```

### Expected Output

When you run the program, the output demonstrates that only the correct plaintext password matches the generated hash:

```txt
Candidate: cow             != Stored Secret
Candidate: toomanysecrets  == Stored Secret
Candidate: password        != Stored Secret
```

Using this approach, you can now securely store and verify passwords within your application, ensuring that user credentials remain protected even if your database is compromised.

## ☑ Exercise



```masteryls
{"id":"7ea13f64-a461-4c6e-89ae-86a2da0e6a52","title":"The Purpose of Password Hashing","type":"multiple-choice"}
When a system stores user credentials, why is it considered a security best practice to hash the passwords instead of encrypting them or storing them in plaintext?

- [x] To store a one-way cryptographic representation of the password so that the original plaintext is never exposed, even if the database is compromised.
- [ ] To scramble the password during transmission from the user's browser to the web server to prevent man-in-the-middle attacks.
- [ ] To compress the password into a smaller fixed-length string to reduce the storage space required by the database.
- [ ] To transform the password into a format that can be easily decrypted by a system administrator if the user forgets their login credentials.
```

```masteryls
{"id":"92b925c6-95ec-4402-939b-ce07d0623105","title":"Guarding the Trust of Others","type":"essay"}
How does securely storing passwords protect the people who trust you with their information, and how does honoring that trust reflect Christlike service and personal integrity?
```
