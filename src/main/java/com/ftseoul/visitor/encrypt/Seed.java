package com.ftseoul.visitor.encrypt;

import com.ftseoul.visitor.config.EncryptConfig;

import java.nio.charset.StandardCharsets;

public class Seed {

    private final EncryptConfig encrypt = new EncryptConfig();

    private final String key = encrypt.getKey();
    private final String IV = encrypt.getIV();

    private final byte[] pbszUserKey = key.getBytes(StandardCharsets.UTF_8);
    private final byte[] pbszIV = IV.getBytes(StandardCharsets.UTF_8);

    public byte[] encrypt(String rawMsg) {
        byte[] message = rawMsg.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMsg = KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUserKey, pbszIV,
                message, 0, message.length);
        return encryptedMsg;
    }

    public String decrypt(byte[] encryptedMsg) {
        byte[] decryptedMsg = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUserKey, pbszIV,
                encryptedMsg, 0, encryptedMsg.length);
        return new String(decryptedMsg, StandardCharsets.UTF_8);
    }

}
