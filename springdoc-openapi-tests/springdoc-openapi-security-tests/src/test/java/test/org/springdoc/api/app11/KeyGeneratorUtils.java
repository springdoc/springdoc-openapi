package test.org.springdoc.api.app11;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * @author yuta.saito
 */
public final class KeyGeneratorUtils {
	private KeyGeneratorUtils() {
		//final class constructor should be hidden
	}

	static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}
}
