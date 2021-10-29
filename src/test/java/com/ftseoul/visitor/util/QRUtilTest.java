package com.ftseoul.visitor.util;

import com.ftseoul.visitor.exception.InvalidDateException;
import com.ftseoul.visitor.exception.InvalidQRCodeException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QRUtilTest {

    @Test
    @DisplayName("QR생성시 내용과 날짜가 구분자로 나뉜다")
    void makeQR() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        String expected = "1" + "." + tomorrow.toString();
        String qrCode = QRUtil.make("1", tomorrow);
        assertEquals(expected, qrCode);
    }

    @Test
    @DisplayName("오늘 예약자가 아니면 예외를 던진다")
    void checkTomorrowQR() {
        assertThrows(InvalidDateException.class, () -> QRUtil.withinToday(LocalDateTime.now().plusDays(1)));
    }

    @Test
    @DisplayName("오늘 예약자이면 예외를 던지지 않는다")
    void checkTodayQR() {
        assertDoesNotThrow(() -> QRUtil.withinToday(LocalDateTime.now()));
    }

    @Test
    @DisplayName("QR text가 올바른 형식이 아닐시 에러를 던진다")
    void invalidQRText() {
        assertThrows(InvalidQRCodeException.class, () -> QRUtil.validateFormat(null));
        assertThrows(InvalidQRCodeException.class, () -> QRUtil.validateFormat("abcd"));
    }
}
