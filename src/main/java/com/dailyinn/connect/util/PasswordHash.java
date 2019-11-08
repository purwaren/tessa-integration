package com.dailyinn.connect.util;

import com.dailyinn.connect.constant.HashPrefix;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by purwa on 5/9/17.
 */
public class PasswordHash {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String alg = "SHA-512";
    private MessageDigest md;

    public PasswordHash(String alg) throws NoSuchAlgorithmException {
        this.alg = alg;
        this.init();
    }

    private void init() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance(this.alg);
        logger.debug("Init password hash");
    }

    public String hashPassword(String plain) throws UnsupportedEncodingException {
        String salt = new SimpleRandomString(8).nextString();
        logger.debug("SecureRandom for salt: "+salt);
        return hashPassword(plain, salt);
    }

    private String hashPassword(String plain, String salt) throws UnsupportedEncodingException {
        byte[] saltb = salt.getBytes("UTF-8");
        this.md.update(plain.getBytes("UTF-8"));
        byte[] cipher = this.md.digest(saltb);
        String prefix;
        if (this.alg.equalsIgnoreCase(HashPrefix.SSHA512.getAlg())) {
            prefix = HashPrefix.SSHA512.getPrefix();
        }
        else if(this.alg.equalsIgnoreCase(HashPrefix.BCRYPT.getAlg())) {
            prefix = HashPrefix.BCRYPT.getPrefix();
        }
        else prefix = HashPrefix.CRYPT.getPrefix();

        return prefix+ Base64.encodeBase64String(combine(cipher, saltb));
    }

    public boolean verifyPassword(String hash, String plain) throws UnsupportedEncodingException {
        byte[] bhash = Base64.decodeBase64(hash);
        if(alg.equalsIgnoreCase(HashPrefix.SSHA512.getAlg()) && bhash.length == HashPrefix.SSHA512.getLength()) {
            byte[] cipher = Arrays.copyOfRange(bhash, 0, 64);
            byte[] salt = Arrays.copyOfRange(bhash, 64, 72);

            String computed = hashPassword(plain, new String(salt));
            hash = HashPrefix.SSHA512.getPrefix()+hash;
            return computed.equals(hash);

        }
        return false;
    }

    public String randomString(int length) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(length, random).toString(64);
    }

    private byte[] combine(byte[] first, byte[] second) {
        byte[] concate = new byte[first.length+second.length];
        System.arraycopy(first, 0, concate, 0, first.length);
        System.arraycopy(second, 0, concate, first.length, second.length);
        return concate;
    }
}
