package com.ftseoul.visitor.util;

import com.ftseoul.visitor.encrypt.Seed;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class QRUtil {

    public static final String SEPARATOR = ".";

    public static String make(String text, LocalDateTime timestamp, Seed seed) {
        StringBuilder qrText = new StringBuilder(text);
        qrText.append(SEPARATOR);
        qrText.append(timestamp.toString());
        return seed.encryptUrl(qrText.toString());
    }

    public static boolean withinToday(LocalDateTime timestamp) {
        LocalDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime()
            .with(LocalTime.MIN);
        LocalDateTime tomorrow = today.plusDays(1);
        return (timestamp.isBefore(tomorrow) && timestamp.isAfter(today));

    }

    public static boolean validFormat(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        // 구분자가 없는 경우
        int result = text.indexOf(SEPARATOR);
        return (result != -1);
    }
}
