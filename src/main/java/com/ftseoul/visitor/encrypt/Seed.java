package com.ftseoul.visitor.encrypt;

import com.ftseoul.visitor.config.EncryptConfig;
import org.springframework.util.SerializationUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Seed {

    private static final EncryptConfig encrypt = new EncryptConfig();

    private static final String key = encrypt.getKey();
    private static final String IV = encrypt.getIV();

    private static final byte[] pbszUserKey = key.getBytes(StandardCharsets.UTF_8);
    private static final byte[] pbszIV = IV.getBytes(StandardCharsets.UTF_8);

    public static String encrypt(String rawMsg) {
        byte[] message = rawMsg.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMsg = KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUserKey, pbszIV,
                message, 0, message.length);

        return Base64.getEncoder()
                .encodeToString(SerializationUtils.serialize(encryptedMsg));
    }

    private static <T> T deserialize(String code, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getDecoder().decode(code)
        ));
    }

    public static String decrypt(String encodeMsg) {
        byte[] encryptedMsg = deserialize(encodeMsg, byte[].class);
        byte[] decryptedMsg = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUserKey, pbszIV,
                encryptedMsg, 0, encryptedMsg.length);
        return new String(decryptedMsg, StandardCharsets.UTF_8);
    }

}
