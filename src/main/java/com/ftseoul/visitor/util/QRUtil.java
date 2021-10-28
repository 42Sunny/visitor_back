package com.ftseoul.visitor.util;

import com.ftseoul.visitor.exception.InvalidDateException;
import com.ftseoul.visitor.exception.InvalidQRCodeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class QRUtil {

    public static final String SEPARATOR = ".";

    public static String make(String text, LocalDateTime timestamp) {
        StringBuilder qrText = new StringBuilder(text);
        qrText.append(SEPARATOR);
        qrText.append(timestamp.toString());
        return qrText.toString();
    }

    public static void withinToday(LocalDateTime timestamp) {
        LocalDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime()
            .with(LocalTime.MIN);
        LocalDateTime tomorrow = today.plusDays(1);
        if (!(timestamp.isBefore(tomorrow) && timestamp.isAfter(today))) {
            throw new InvalidDateException(timestamp.toString());
        }
    }

    public static void validateFormat(String text) {
        if (text == null || !text.contains(SEPARATOR)) {
            throw new InvalidQRCodeException("text", text);
        }
    }
}
