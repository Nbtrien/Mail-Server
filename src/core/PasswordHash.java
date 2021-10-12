package core;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {
	private static String password;

	public PasswordHash(String password) {
		super();
		this.password = password;
	}
	public static byte[] getSHA() throws NoSuchAlgorithmException
    { 
        // Static getInstance method is called with hashing SHA 
        MessageDigest md = MessageDigest.getInstance("SHA-256"); 
  
        // digest() method called 
        // to calculate message digest of an input 
        // and return array of byte
        return md.digest(password.getBytes(StandardCharsets.UTF_8)); 
    }
    
    public static String toHexString() throws NoSuchAlgorithmException
    {
        // Convert byte array into signum representation 
        BigInteger number = new BigInteger(1, getSHA()); 
  
        // Convert message digest into hex value 
        StringBuilder hexString = new StringBuilder(number.toString(16)); 
  
        // Pad with leading zeros
        while (hexString.length() < 32) 
        { 
            hexString.insert(0, '0'); 
        } 
  
        return hexString.toString(); 
    }
}
