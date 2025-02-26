package org.example.crypto;

import java.security.Key;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AesGcmCryptoTest {

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