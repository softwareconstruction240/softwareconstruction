# Securing Passwords

Whenever you accept personal information from a user, you become responsible for securing that information. One of the most critical pieces of information to protect is their password. If a password is exposed then you are exposing the ability to act as the user.

We will describe the details of how cryptographic hash functions work in a later topic. However, for now we will discuss how to use the Bcrypt algorithm to securely hash and compare a user's password.

It is vital that you use a secure method of storing passwords as part of your work to persistently store your application's data.

### Securely Storing Passwords with BCrypt

The Bcrypt algorithm enables you to take clear text password and irreversibly hash it to a deterministic representation. This allows you to hash the same password a second time and compare the result to the first password hash. If they are equal then you know that both hashes originated from the same password.

By hashing the passwords, your application never stores a password in the database. You can still use the hash to verify a user's identity, but if a nefarious party gains access to your database, then still cannot retrieve your user's clear text password.

## Implementing BCrypt

You can using `Bcrypt` in your application by using the following library.

```
org.springframework.security:spring-security-core:5.7.1
```

This implementation of Bcrypt makes it so you can hash a password with one line of code, and then later compare the hash to a candidate password with another line of code. The following example first hashes the password `toomanysecrets` and then compares it to three possible candidates.

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

## Things to Understand

- Securely storing passwords using Bcrypt
