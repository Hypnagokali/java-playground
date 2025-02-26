package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;

public class AesGcmCrypto {

    private final static int TAG_LENGTH = 128;
    private final Key key;

    public AesGcmCrypto(Key key) {
        this.key = key;
    }

    public String encryptToEncodedBase64(String msg) {
        final int IV_LENGTH = 12; // recommend to avoid further GHASH processing

        byte[] iv = new byte[IV_LENGTH];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes( iv );

        Cipher cipher = getCipher();

        // Tag config for authentication
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec( TAG_LENGTH, iv );
        byte[] encrypted;
        try {
            cipher.init( Cipher.ENCRYPT_MODE, key, gcmParameterSpec );
            encrypted = cipher.doFinal( msg.getBytes( StandardCharsets.UTF_8 ) );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        byte[] finalMessage = new byte[encrypted.length + iv.length];
        System.arraycopy( iv, 0, finalMessage, 0, IV_LENGTH );
        System.arraycopy( encrypted, 0, finalMessage, IV_LENGTH, encrypted.length );

        return Base64.getEncoder().encodeToString( finalMessage );
    }

    public String decryptFromBase64(String msg) {
        byte[] encrypted = Base64.getDecoder().decode( msg );
        byte[] iv = new byte[12];
        byte[] encryptedMessage = new byte[encrypted.length - iv.length];

        System.arraycopy( encrypted, 0, iv, 0, 12 );
        System.arraycopy( encrypted, 12, encryptedMessage, 0, encrypted.length - iv.length );

        Cipher cipher = getCipher();

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec( TAG_LENGTH, iv );

        byte[] decrypted;
        try {
            cipher.init( Cipher.DECRYPT_MODE, key, gcmParameterSpec );
            decrypted = cipher.doFinal( encryptedMessage );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        return new String( decrypted, StandardCharsets.UTF_8 );
    }

    private static Cipher getCipher() {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance( "AES/GCM/NoPadding" );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        return cipher;
    }
}
