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
