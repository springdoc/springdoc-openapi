/*
 * Copyright (c) 2023. mCruncher Sdn Bhd, Cyberjaya, Malaysia.
 * All rights reserved.
 */
package test.org.springdoc.api.app10;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.nimbusds.jose.jwk.RSAKey;

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
public final class Jwks
{
    private Jwks()
    {
        //final class constructor should be hidden
    }

    public static RSAKey generateRsa()
    {
        KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }
}
