package com.ftseoul.visitor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

@Service
@Slf4j
@Transactional
public class QRcodeService {

    public static String serialize(Object object) {
        return Base64.getEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(String code, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getDecoder().decode(code)
        ));
    }

}
