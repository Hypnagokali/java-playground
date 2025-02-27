package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AesGcmCryptoTest {

    @Test
    void changesShouldNotBeAccepted() {
        String myMessage = "Rabbits running across the meadow";
        KeyGenerator keyGenerator = new KeyGenerator();
        Key key = keyGenerator.getSecureRandomKey( "SomeSecretPass" );

        AesGcmCrypto aesGcmCrypto = new AesGcmCrypto(key);
        String encryptedBase64Message = aesGcmCrypto.encryptToEncodedBase64( myMessage );
        byte[] encryptedBytes = Base64.getDecoder().decode( encryptedBase64Message );
        encryptedBytes[14] = 0x41;
        String manipulatedMessage = Base64.getEncoder().encodeToString( encryptedBytes );

        assertThatThrownBy( () -> aesGcmCrypto.decryptFromBase64( manipulatedMessage ) )
            .hasMessageContaining( "Tag mismatch" );
    }


    @Test
    void aesGcmTest() {
        String myMessage = "Rabbits running across the meadow";
        KeyGenerator keyGenerator = new KeyGenerator();
        Key key = keyGenerator.getSecureRandomKey( "SomeSecretPass" );

        AesGcmCrypto aesGcmCrypto = new AesGcmCrypto(key);
        String encryptedBase64Message = aesGcmCrypto.encryptToEncodedBase64( myMessage );

        String decryptedMessage = aesGcmCrypto.decryptFromBase64( encryptedBase64Message );

        System.out.println( "Encrypted message: " + encryptedBase64Message );
        assertThat( encryptedBase64Message ).isNotEqualTo( myMessage );
        assertThat(decryptedMessage).isEqualTo( myMessage );
    }
}