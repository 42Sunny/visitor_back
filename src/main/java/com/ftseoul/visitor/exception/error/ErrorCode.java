package com.ftseoul.visitor.exception.error;


public enum ErrorCode {

    UNAUTHORIZED_CODE("오늘 예약된 방문자가 아닙니다. 예약날자 확인바랍니다",4030),
    INVALID_DEVICE("허용되지 않은 디바이스 입니다.",4010),
    NOT_FOUND_QRCODE("유효하지 않은 QR코드 입니다.",4040),
    DUPLICATE_PHONE_NUMBER("중복된 휴대폰 번호는 입력불가 합니다.",4030),
    RESOURCE_NOT_FOUND("해당 내용을 찾을 수 없습니다.",4040),
    INTERNAL_SERVER_ERROR("서버에 문제가 있습니다.",500),
    FRONT_ERRLOG_RECIEVE("프론트엔드 에러를 로그로 남겼습니다.",2000),
    WAUTH_AUTHORIZED_FAIL("wauth토큰인증을 실패하였습니다.",4040),
    DIFFERNT_STATUS_VALUE("다른상태값을 입력해주세요.",4090);

    private final String message;
    private int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
