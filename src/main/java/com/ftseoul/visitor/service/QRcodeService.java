package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class QRcodeService {

    private final VisitorRepository visitorRepository;

    public static String serialize(Object object) {
        return Base64.getEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(String code, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getDecoder().decode(code)
        ));
    }

    public boolean checkQRCode(String text) {
        return visitorRepository.existsById(Long.parseLong(text));
    }
}
