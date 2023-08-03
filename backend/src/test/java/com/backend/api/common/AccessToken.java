package com.backend.api.common;

public class AccessToken {
    private static final String token =
"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTEwNDQ2NTIsImV4cCI6MTY5MTA0NjQ1MiwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.r-VhIHl0TrIsqUAdSns2EOR1SfsRFovPg-JSk9Q_W4k8D8l1ZIyim3L-psNSCFVW6uUQBnx-jRZ8dHEjz_idRg";

    private static final String testEmail =
            "hoilday5303@naver.com";
    public static String getToken() {
        return token;
    }

    public static String getTestEmail() {
        return testEmail;
    }
}
