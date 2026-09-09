package com.acme.banking;

import com.password4j.Password;
import com.password4j.Hash;

public class SecurityUtil {
    public static String hashPassword(String pass) {
        Hash hashedPass = Password.hash(pass)
                .addRandomSalt()
                .addPepper("shared-secret")
                .withArgon2();
        return hashedPass.getResult();
    }

    public static boolean verifyPassword(String pass, String hashedPass) {
        boolean result = Password.check(pass, hashedPass).addPepper("shared-secret")
                .withArgon2();
        return result;
    }
}
