package com.dailyinn.connect.constant;

/**
 * Created by purwa on 5/9/17.
 */
public enum HashPrefix {
    SSHA512("SHA-512", "{SSHA512}", 72),
    CRYPT("CRYPT", "{CRYPT}", 32),
    BCRYPT("BCRYPT", "{BCRYPT}", 32);

    private final String alg;
    private final String prefix;
    private final int length;

    HashPrefix(String alg, String prefix, int length) {
        this.alg = alg;
        this.prefix = prefix;
        this.length = length;
    }

    public String getAlg() {
        return alg;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getLength() {
        return length;
    }
}
