package com.backend.api.common;

public class AccessToken {
    private static final String token =
"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTEwNDgwNzEsImV4cCI6MTY5MTA0OTg3MSwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.TvhylRcZlYBG-I4QdzjdYdgiANF5IyL5GC_lOeZaULUFJ8JUpryQ2x4QYtWGQs39aJHgvXYgb61ye-65tAVhUg";

    private static final String testEmail =
            "hoilday5303@naver.com";
    public static String getToken() {
        return token;
    }

    public static String getTestEmail() {
        return testEmail;
    }
}
