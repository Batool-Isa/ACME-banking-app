package com.acme.banking;
import com.password4j.Password;
import com.password4j.Hash;
public class SecurityUtil {
    public static String hashPassword(String pass){
        Hash hashedPass = Password.hash(pass)
                .addRandomSalt()
                .addPepper("shared-secret")
                .withArgon2();
         return hashedPass.getResult();
    }
    public static boolean verifyPassword(String pass, String hashedPass) {

        System.out.println("Entered password: " + pass);
        System.out.println("Hashed password: " + hashedPass);

        boolean result = Password.check(pass, hashedPass)  .addPepper("shared-secret")
                .withArgon2();

        System.out.println("Verification result: " + result);

        return result;
    }
}
