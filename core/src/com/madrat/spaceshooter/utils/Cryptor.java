package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.utils.Base64Coder;

// import android.os.Build;

import java.util.Random;

public class Cryptor {

    Random random;

    public Cryptor() {
        this.random = new Random();
    }

/*    public char[] encrypt(char [] inpData) {
        // encrypt

        return Base64Coder.encode(encrypted);
    }

    public char[] decrypt(char [] inpData) {
        // decrypt

        return Base64Coder.decode(decrypted);
    }*/

    public char[] generateKey() {
        char[] key = new char[256];

        // System.out.println(Build.SERIAL);

        System.out.println("[+] Key: ");
        return key;
    }
}
