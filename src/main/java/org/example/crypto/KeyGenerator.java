package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {

    public Key getSecureRandomKey(String passphrase) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance( "SHA-256" );
        }
        catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e );
        }
        byte[] randomKeyBytes = digest.digest( passphrase.getBytes( StandardCharsets.UTF_8 ) );

        return new SecretKeySpec( randomKeyBytes, "AES" );
    }

}
