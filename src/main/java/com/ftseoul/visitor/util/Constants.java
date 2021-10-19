package com.ftseoul.visitor.util;

public abstract class Constants {
    public static final String PROFILE = "spring.profiles.active";
    public static final String DOMAIN = (System.getProperty(PROFILE).equals("prod")) ? "https://vstr.kr" : "https://dev.vstr.kr";
    public static final String QRPATH = (System.getProperty(PROFILE).equals("prod")) ? "https://visitor.42seoul.io/qr/" : "https://visitor.dev.42seoul.io/qr/";
    public static final String LOOKUPPATH = (System.getProperty(PROFILE).equals("prod")) ? "https://visitor.42seoul.io/reserve-info/": "https://visitor.dev.42seoul.io/reserve-info/";
}
