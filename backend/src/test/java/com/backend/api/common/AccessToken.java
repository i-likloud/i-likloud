package com.backend.api.common;

public class AccessToken {
    private static final String refreshToken =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjkxMTI3MDI1LCJleHAiOjE2OTE3MzE4MjUsImVtYWlsIjoiaG9pbGRheTUzMDNAbmF2ZXIuY29tIn0.vjkkBHG81sKGHhkS_4SezOTVsiKgh6xGhf_2tCLGP37RIljbhoPNrm0RktwLJf1y-O2fWDxnWfqm1wazJKXNeQ";


    private static final String secondToken =
"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTExMTE1OTYsImV4cCI6MTY5MTExMzM5NiwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.v_gpxnxirMmncbbO8SL3I3HaRAGU8YVWao5HMYuv_h52se1dJsE8-yc0-5U_fECcxFbo4zgo_di9oH1QA8MvcQ";

    private static final String firstToken =
            "npnrgm6rj_OufxQaFa4EuXjHnfUL2H-a7dsvrVgICj10mAAAAYm-Gsh8";
    private static final String newToken =
"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTExMzE2NjIsImV4cCI6MTY5MTEzMzQ2MiwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.D1hNldGvdYZ2rjnDmZEqoC5OjDFUNrZVtJg9NN9DCsV03D2ahO98Ey8qryrfDB4RrgYH6QPtSqKtbk0paY1_8A";

    private static final String testEmail =
            "hoilday5303@naver.com";
    public static String getNewToken() {
        return newToken;
    }

    public static String getSecondToken() {return secondToken;}

    public static String getTestEmail() {
        return testEmail;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }
}
