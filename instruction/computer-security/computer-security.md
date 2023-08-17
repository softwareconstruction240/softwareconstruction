# Computer Security

🖥️ [Slides](https://docs.google.com/presentation/d/1pUU4DDACUndgj_ij7bbKOUY8cxmGinZq/edit#slide=id.p1)

📖 **Required Reading**: None

Software should be developed with security in mind. Bad actors (hackers) try to compromise systems in a variety of ways. They may try to gain unauthorized access to data and computers for the purposes of stealing, monitoring, damaging, or otherwise misusing these assets. Systems should be implmemented to prevent these illicit activities. Additionally, crypto currency is becoming an incresasingly important part of the economy. With financial transactions being conducted in a digital environment, crypto currency systems must be securely implemented so transactions can be trusted and money cannot be stolen. This lecture discusses several core concepts and technologies that form the basis of secure computer systems.

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
