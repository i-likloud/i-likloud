package com.backend.api.common;

public class AccessToken {
    private static final String token =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTEwMzEzOTUsImV4cCI6MTY5MTAzMzE5NSwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.4zbZR5QkLWcbMOg5fHHD9Bk2E_aD50ckkCcE_-gDqwoSC7w7kxd3g-wwqVkrkNeVfXSLEIBNTmbdyu7VHPQLiQ";
    private static final String testEmail =
            "hoilday5303@naver.com";
    public static String getToken() {
        return token;
    }

    public static String getTestEmail() {
        return testEmail;
    }
}
