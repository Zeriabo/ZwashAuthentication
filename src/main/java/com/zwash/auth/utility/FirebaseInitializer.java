package com.zwash.auth.utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseInitializer {

	private static final String JSON_FILE_PATH = "config/zwash-385807-firebase-adminsdk-3fuy0-71a94dfd15.json";

	private static final String ENCRYPTED_FILE_PATH = "config/encrypted-json.txt";

	public static void initializeFirebaseApp() {
		try {

			// Check if the encrypted file exists
			if (!isEncryptedFileExists()) {
				// Encrypt the JSON file and create the encrypted file
				encryptJsonFile(JSON_FILE_PATH, ENCRYPTED_FILE_PATH);

			}
			// Decrypt the JSON file content
			String decryptedContent = decryptJsonFile();

			// Create Firebase options from the decrypted content
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(decryptedContent.getBytes())))
					.build();

			FirebaseApp.initializeApp(options);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isEncryptedFileExists() {
		File file = new File(ENCRYPTED_FILE_PATH);
		return file.exists();
	}

	private static void encryptJsonFile(String jsonFilePath, String encryptedFilePath) throws IOException {
		File jsonFile = new File(jsonFilePath);
		FileInputStream fileInputStream = new FileInputStream(jsonFile);

		// Read the JSON file content
		byte[] jsonBytes = fileInputStream.readAllBytes();
		String jsonContent = new String(jsonBytes);

		// Encrypt the JSON content
		String encryptedContent = EncryptionUtils.encrypt(jsonContent);

		// Close the input stream
		fileInputStream.close();

		// Delete the original JSON file
		jsonFile.delete();

		// Create the new JSON file
		File newJsonFile = new File(jsonFilePath);

		// Write the encrypted content to the new JSON file
		FileOutputStream fileOutputStream = new FileOutputStream(newJsonFile);
		fileOutputStream.write(encryptedContent.getBytes());

		fileOutputStream.close();

		// Create the encrypted file
		FileOutputStream encryptedOutputStream = new FileOutputStream(encryptedFilePath);
		encryptedOutputStream.write(encryptedContent.getBytes());

		encryptedOutputStream.close();
		newJsonFile.delete();
	}

	private static String decryptJsonFile() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(ENCRYPTED_FILE_PATH);

		// Read the encrypted file content
		byte[] encryptedBytes = fileInputStream.readAllBytes();
		String encryptedContent = new String(encryptedBytes);

		// Decrypt the content
		String decryptedContent = EncryptionUtils.decrypt(encryptedContent);

		fileInputStream.close();
		return decryptedContent;
	}

}
