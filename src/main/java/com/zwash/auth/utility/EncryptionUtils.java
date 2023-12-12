package com.zwash.auth.utility;

import org.jasypt.util.text.AES256TextEncryptor;

import io.github.cdimascio.dotenv.Dotenv;

public class EncryptionUtils {
    public static String encrypt(String plaintext) {
        // Load environment variables
        Dotenv dotenv = Dotenv.configure().load();

        // Retrieve encryption key from environment variable
        String encryptionKey = dotenv.get("ENCRYPTION_KEY");

        // Create AES256TextEncryptor instance
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encryptionKey);

        // Encrypt the plaintext
        return encryptor.encrypt(plaintext);
    }

    public static String decrypt(String ciphertext) {
        // Load environment variables
        Dotenv dotenv = Dotenv.configure().load();

        // Retrieve encryption key from environment variable
        String encryptionKey = dotenv.get("ENCRYPTION_KEY");

        // Create AES256TextEncryptor instance
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encryptionKey);

        // Decrypt the ciphertext
        return encryptor.decrypt(ciphertext);
    }
}
