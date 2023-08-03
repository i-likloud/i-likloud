package com.backend.api.common;

public class AccessToken {
    private static final String token =
"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTEwNTAyMjcsImV4cCI6MTY5MTA1MjAyNywiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.BFLF3W8A_Wumy8uIQamjUZZo8je2CCQZGaSFDlBjR5nDL15xCgcKdyHZCi1gDjMlWSTmA4AdtN_11imbCeZrcQ";

    private static final String testEmail =
            "hoilday5303@naver.com";
    public static String getToken() {
        return token;
    }

    public static String getTestEmail() {
        return testEmail;
    }
}
