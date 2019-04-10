package com.madrat.spaceshooter.utils;

import android.os.Build;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.madrat.spaceshooter.MainGame;

import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// The main rule of cryptography
// Never invent your own ciphers
//  ¯\_(ツ)_/¯

public class Encryptor {

    private static final int magic = 0x8245;
    private static SecureRandom secureRandom;
    private static byte[] privateKey;
    private static byte[] initVector;

    public Encryptor() {
        if (secureRandom == null)
            secureRandom = new SecureRandom();
        if (initVector == null)
            this.initInitVector();
        if (privateKey == null)
            this.initKey();
    }

    // Encrypt string with pre-generate iv and key
    public String encrypt(String plainText) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(privateKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(plainText.getBytes());

            return new String(Base64Coder.encode(encrypted));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Decrypt string with pre-generate iv and key
    public String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(privateKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64Coder.decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public int encryptFile(String path) {
        try {
            FileHandle handle = Gdx.files.local(path);
            String decrypted = handle.readString();
            String encrypted = encrypt(decrypted);
            handle.writeString(encrypted, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
        return 0;
    }

    public int decryptFile(String path) {
        try {
            FileHandle handle = Gdx.files.local(path);
            String encrypted = handle.readString();
            String decrypted = decrypt(encrypted);
            handle.writeString(decrypted, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
        return 0;
    }

    // iv initializer
    private void initInitVector() {
        initVector = new byte[]{0x0f, 0x11, 0x41, 0x48, 0x3f, 0x2f, 0x12, 0x1f, 0x04, 0x38, 0x2d, 0x68, 0x50, 0x25, 0x77, 0x11};
    }

    // Simple xor and bit shifting (nothing special, can be reversed really easy)
    // FIXME create normal key generation for all platforms
    private void initKey() {
        String del = ":";
        byte[] key;
        if (MainGame.applicationType == Application.ApplicationType.Android) { // due to conflict with org.json i deleted android from project (Build.SERIAL + del + Build.ID + del)
            key = StringUtils.rightPad(Build.SERIAL + del + Build.ID + del, 32, "~").getBytes(); // new byte[]{0x5f, 0x5e, 0x74, 0x35, 0x2a, 0x65, 0x24, 0x7a, 0x9, 0x78, 0x31, 0x36, 0x1c, 0x2e, 0x34, 0x68, 0x16, 0x52, 0x14, 0x41, 0x67, 0x62, 0x37, 0x28, 0x11, 0x1, 0x1, 0x39, 0x3d, 0xd, 0x66, 0x20};
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            key = new byte[]{0x12, 0x2d, 0x2f, 0x6c, 0x1f, 0x7a, 0x4f, 0x10, 0x48, 0x56, 0x17, 0x4b, 0x4f, 0x48, 0x3c, 0x17, 0x04, 0x06, 0x4b, 0x6d, 0x1d, 0x68, 0x4b, 0x52, 0x50, 0x50, 0x1f, 0x06, 0x29, 0x68, 0x5c, 0x65};
        } else {
            key = new byte[]{0x77, 0x61, 0x6c, 0x0b, 0x04, 0x5a, 0x4f, 0x4b, 0x65, 0x48, 0x52, 0x68, 0x1f, 0x1d, 0x3c, 0x4a, 0x5c, 0x06, 0x1f, 0x2f, 0x12, 0x32, 0x50, 0x19, 0x3c, 0x52, 0x04, 0x17, 0x48, 0x4f, 0x6d, 0x4b};
        }
        for (int i = 0; i < key.length; ++i) {
            key[i] = (byte) ((key[i] << 2) ^ magic);
        }
        privateKey = key;
    }
}