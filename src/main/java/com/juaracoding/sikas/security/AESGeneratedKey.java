package com.juaracoding.sikas.security;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/14/2025 10:18 AM
@Last Modified 11/14/2025 10:18 AM
Version 1.0
*/

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class AESGeneratedKey {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
            keyGen.init(128); // You can use 192 or 256 bits as well
            SecretKey aesKey = keyGen.generateKey();
            System.out.println("AES Key: " + bytesToHex(aesKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getKey() {
        SecretKey aesKey = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
            keyGen.init(128);
            aesKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytesToHex(aesKey.getEncoded());
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}