/**
 * 
 */
package ordermypizza;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Obscure User ID using hash function with salt
 * @author Geurney
 *
 */
public class UserIDObscure {

	/**
	 * Salt String
	 */
	private static final String SECRET_STRING = "order my pizza";

	/** 
	 * Hash Function
	 */
	private static final String HASH_FUNCTION = "SHA-256";

	/**
	 * Obscure the User ID using hash function with salt
	 * 
	 * @param uid
	 *            User ID
	 * @return Obscured UID
	 * @throws NoSuchAlgorithmException
	 *             Hash function not available.
	 */
	public static String obsecure(String uid) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(HASH_FUNCTION);
		String message = SECRET_STRING + uid;
		byte[] result = digest.digest(message.getBytes());
		return toHex(result);
	}

	/**
	 * Convert byte[] to Hex string.
	 * 
	 * @param bytes
	 *            byte array
	 * @return Hex string
	 */
	public static String toHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}
}
