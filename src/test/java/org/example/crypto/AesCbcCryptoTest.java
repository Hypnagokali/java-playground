package org.example.crypto;

import java.security.Key;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class AesCbcCryptoTest {

    @Test
    void canManipulateMessage() {
        String message = "The rain falls silently";
        KeyGenerator keyGenerator = new KeyGenerator();
        Key key = keyGenerator.getSecureRandomKey( "AnotherSecretPass" );
        AesCbcCrypto aesCbcCrypto = new AesCbcCrypto( key );
        String encrypted = aesCbcCrypto.encryptToEncodedBase64( message );
        byte[] encryptedBytes = Base64.getDecoder().decode( encrypted );
        encryptedBytes[17] = 0x41;
        String manipulatedMessage = Base64.getEncoder().encodeToString( encryptedBytes );

        assertThatCode( () -> aesCbcCrypto.decryptToEncodedBase64( manipulatedMessage ) ).doesNotThrowAnyException();
    }

    @Test
    void aesCbcTest() {
        String message = "The rain falls silently";
        KeyGenerator keyGenerator = new KeyGenerator();
        Key key = keyGenerator.getSecureRandomKey( "AnotherSecretPass" );
        AesCbcCrypto aesCbcCrypto = new AesCbcCrypto( key );

        String encrypted = aesCbcCrypto.encryptToEncodedBase64( message );

        String decrypted = aesCbcCrypto.decryptToEncodedBase64( encrypted );

        assertThat(decrypted).isEqualTo( message );
    }
}