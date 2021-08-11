package com.ftseoul.visitor.encrypt;

import com.ftseoul.visitor.config.EncryptConfig;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class KISA_SEED_CBCTest {

    private final byte[] pbszUserKey = "testCrypt2021!@#".getBytes(StandardCharsets.UTF_8);
    private final byte[] pbszIV = "1234567890123456".getBytes(StandardCharsets.UTF_8);

    private EncryptConfig config;
    private Seed seed = new Seed(config);

    @Test
    public void 암복호화_테스트() {
        //given
        String rawMsg = "테스트 데이터";

        // when
        byte[] encryptedMsg = KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUserKey, pbszIV,
                rawMsg.getBytes(), 0, rawMsg.getBytes().length);
        System.out.println("암호화된 데이터1 => " + new String(encryptedMsg));
        System.out.println("암호화된 데이터2 => " + encryptedMsg.toString());

        byte[] decryptedMsg = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUserKey, pbszIV, encryptedMsg,
                0, encryptedMsg.length);
        System.out.println("복호화된 데이터 -> " + new String(decryptedMsg));

        assertThat(rawMsg).isEqualTo(new String(decryptedMsg));
        assertThat(rawMsg).isNotEqualTo(new String(encryptedMsg));
    }

//    @Test
//    public void seed_test() {
//        String rawphone = "01012345678";
//        String rawname = "홍길동";
//
//        String encryptedPhone = seed.encrypt(rawphone);
//        String encryptedName = seed.encrypt(rawname);
//        System.out.println("암호화된 데이터1 => " + encryptedPhone);
//        System.out.println("암호화된 데이터2 => " + encryptedName);
//
//        System.out.println("복호화된 데이터1 => " + seed.decrypt(encryptedPhone));
//        System.out.println("복호화된 데이터1 => " + seed.decrypt(encryptedName));
//
//        assertThat(rawphone).isEqualTo(seed.decrypt(encryptedPhone));
//        assertThat(rawname).isEqualTo(seed.decrypt(encryptedName));
//    }
}
