package gea.framework;

import gea.utils.Exception.Error500Exception;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Aes {
    private final int keySize = 128;
    private final int iterationCount = 500;
    private final Cipher cipher;
    private String IV;
    private String SALT;
    SecretKey key;
    
    public Aes(String passphrase) throws Error500Exception {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            this.IV = Hex.encodeHexString(md5.digest(passphrase.getBytes()));
            this.SALT = Hex.encodeHexString(sha1.digest(passphrase.getBytes()));
            this.key = generateKey(this.SALT, passphrase);
        }
        catch (Exception e) {
        	throw new Error500Exception("Error al establecer el cifrador - Aes.java");
        }
    }
    public String encrypt(String plaintext) throws Error500Exception {
        try {
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, this.key, this.IV, plaintext.getBytes("UTF-8"));
            return base64(encrypted);
        }
        catch (UnsupportedEncodingException e) {
        	throw new Error500Exception("Error al encriptar el codigo paquete - Aes.java");
        }
    }
    
    public String decrypt(String ciphertext) throws Error500Exception {
        try {
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, this.key, this.IV, Base64.decodeBase64(ciphertext));
            return new String(decrypted, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
        	System.out.println(e.getMessage());
        	throw new Error500Exception("Error al desencriptar el codigo cifrado - Aes.java");
        }
    }
    
    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) throws Error500Exception {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        	throw new Error500Exception("Error al establecer el cifrador - Aes.java");
        }
    }
    
    private SecretKey generateKey(String salt, String passphrase) throws Error500Exception {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (Exception e) {
        	throw new Error500Exception("Error al establecer la llave - Aes.java");
        }
    }
    
    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }
    
    public static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }
    
    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }
    
    public static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }
    
    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}
