package com.reborn.back.global.utils.encrypt;

import javax.crypto.KeyAgreement;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class EncryptUtil {

    public String encrypt(String plainText, String salt) {
        try {
            var salted = plainText + salt;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());

            StringBuilder builder = new StringBuilder();
            var encrypted = md.digest();
            for (byte b : encrypted) {
                // 바이트 값을 16진수 문자열로 변환하여 StringBuilder에 추가
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (Exception e) {

        }
        return null;
    }

    public String getSalt() {

        //1. Random, byte 객체 생성
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[32];

        //2. 난수 생성
        r.nextBytes(salt);

        //3. byte To String (10진수의 문자열로 변경)
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Generate an X25519 key pair
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("X25519");
        return keyPairGenerator.generateKeyPair();
    }

    // Generate shared key using own private key and other's public key
    public static String generateSharedKey(PrivateKey ownPrivateKey, PublicKey otherPublicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("X25519");
        keyAgreement.init(ownPrivateKey);
        keyAgreement.doPhase(otherPublicKey, true);
        byte[] sharedSecret = keyAgreement.generateSecret();
        return Hex.toHexString(sharedSecret);
    }

    public static String encryptAES(String data, String keyHex) throws Exception {
        Key key = new SecretKeySpec(Hex.decode(keyHex), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Hex.toHexString(encrypted);
    }

    public static String decryptAES(String encryptedData, String keyHex) throws Exception {
        Key key = new SecretKeySpec(Hex.decode(keyHex), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] original = cipher.doFinal(Hex.decode(encryptedData));
        return new String(original);
    }
}