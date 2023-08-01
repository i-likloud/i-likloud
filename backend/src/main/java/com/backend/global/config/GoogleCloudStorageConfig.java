//package com.backend.global.config;
//
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GoogleCloudStorageConfig {
//
//    @Value("${spring.cloud.gcp.project-id}")
//    private String projectId;
//
//    @Bean
//    public Storage storage() {
//        return StorageOptions.newBuilder()
//                .setProjectId(projectId)
//                .build()
//                .getService();
//    }
//}
