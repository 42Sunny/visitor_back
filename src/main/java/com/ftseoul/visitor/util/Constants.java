package com.ftseoul.visitor.util;

public abstract class Constants {
    public static final String PROFILEPATH = "spring.profiles.active";
    public static final String CURRENTPROFILE = (System.getProperty(PROFILEPATH) != null) ? System.getProperty(PROFILEPATH) : "local";

    public static final String DOMAIN = (CURRENTPROFILE.equals("prod")) ? "https://vstr.kr" : "https://dev.vstr.kr";
    public static final String QRPATH = (CURRENTPROFILE.equals("prod")) ? "https://visitor.42seoul.io/qr/" : "https://visitor.dev.42seoul.io/qr/";
    public static final String LOOKUPPATH = (CURRENTPROFILE.equals("prod")) ? "https://visitor.42seoul.io/reserve-info/": "https://visitor.dev.42seoul.io/reserve-info/";
}
