package com.backend.api.common;

public class AccessToken {
    private static final String token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTA5NjYyNDksImV4cCI6MTY5MDk2ODA0OSwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.-Bz9EN1uubisajaCATX7ds7gR5_h5a5ht5ljVMk2QLkrOV_P4fmuFSLBU6bxVEofG7VxRSGgltxdXi7Fy2SNuQ";

    public static String getToken() {
        return token;
    }
}
