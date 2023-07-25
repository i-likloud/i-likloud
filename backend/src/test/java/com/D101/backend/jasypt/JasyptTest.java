package com.D101.backend.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptTest {

    @Test
    public void jasyptTest(){

        String password = "password";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);

        String content = "secterKey";    // 암호화 할 내용
        String encryptedContent = encryptor.encrypt(content); // 암호화
        String decryptedContent = encryptor.decrypt(encryptedContent); // 복호화


        // 여기서 콘솔에 출력되는 값을 yml파일에 적어야 됨
        // 'ENC()' 꼭 써야됨!!
        System.out.println("Enc : " + encryptedContent + ", Dec: " + decryptedContent);

    }
}
