package com.bhawanisingh.airesume.controller;

import com.bhawanisingh.airesume.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Application is running successfully.")
                .data("AI Resume & Job Matching Platform")
                .build();

        return ResponseEntity.ok(response);
    }

}