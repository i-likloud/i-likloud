package com.backend.api.common;

public class AccessToken {
    private static final String token =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTEwNDIwMzksImV4cCI6MTY5MTA0MzgzOSwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.I8KXGazARa27iNnTaWSC9Psng03vtys-MhJcs7noxtHNCB9u3c2TX3inGrUtJ2i--2JoN2SpUIQKVLzNpZ2NZg";

    private static final String testEmail =
            "hoilday5303@naver.com";
    public static String getToken() {
        return token;
    }

    public static String getTestEmail() {
        return testEmail;
    }
}
