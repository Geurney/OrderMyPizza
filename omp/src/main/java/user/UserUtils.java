/**
 * 
 */
package user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Obscure User ID using hash function with salt
 * 
 * @author Geurney
 *
 */
public class UserUtils {

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
	public static String obsecure(String uid) {
		if (uid != null) {
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance(HASH_FUNCTION);
				String message = SECRET_STRING + uid;
				byte[] result = digest.digest(message.getBytes());
				return toHex(result);
			} catch (NoSuchAlgorithmException e) {
				System.out.println("UID Obscure: No such hash algorithm!");
			}
		}
		return null;
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
	
	/**
	 * Return current user obscure id
	 * 
	 * @return Obscured user ID
	 */
	public static String getCurrentUserObscureID() {
		User user = UserServiceFactory.getUserService()
				.getCurrentUser();
		if (user == null) {
			return null;
		}
		return UserUtils.obsecure(user.getUserId());
	}
}
