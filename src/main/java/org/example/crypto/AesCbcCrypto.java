package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class AesCbcCrypto {

    private final Key key;

    private byte[] iv = null;

    public AesCbcCrypto(Key key) {
        this.key = key;
    }


    public String encryptToEncodedBase64(String message) {
        final int IV_LENGTH = 16;
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes( iv );
        this.iv = iv;

        Cipher cipher = getCipher();

        IvParameterSpec ivParameterSpec = new IvParameterSpec( iv );
        byte[] encrypted;
        try {
            cipher.init( Cipher.ENCRYPT_MODE, key,  ivParameterSpec);
            encrypted = cipher.doFinal(message.getBytes( StandardCharsets.UTF_8 ));
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        byte[] finalMessage = new byte[encrypted.length + iv.length];
        System.arraycopy( iv, 0, finalMessage, 0, IV_LENGTH );
        System.arraycopy( encrypted, 0, finalMessage, IV_LENGTH, encrypted.length );

        return Base64.getEncoder().encodeToString( finalMessage );
    }

    public String decryptToEncodedBase64(String message) {
        final int IV_LENGTH = 16;
        byte[] completeMessage = Base64.getDecoder().decode(message);
        byte[] iv = new byte[IV_LENGTH];
        byte[] encrypted = new byte[completeMessage.length - IV_LENGTH];

        System.arraycopy( completeMessage, 0, iv, 0, IV_LENGTH );
        System.arraycopy( completeMessage, IV_LENGTH, encrypted, 0, completeMessage.length - IV_LENGTH );

        Cipher cipher = getCipher();

        IvParameterSpec ivParameterSpec = new IvParameterSpec( iv );
        byte[] decrypted;
        try {
            cipher.init( Cipher.DECRYPT_MODE, key, ivParameterSpec);
            decrypted = cipher.doFinal(encrypted);
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        return new String( decrypted, StandardCharsets.UTF_8 );
    }

    private static Cipher getCipher() {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        return cipher;
    }
}
