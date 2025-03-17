package com.aiResumeWizard.backend.ai_resume_backend.controller;

import com.aiResumeWizard.backend.ai_resume_backend.ResumeRequest;
import com.aiResumeWizard.backend.ai_resume_backend.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }


    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> getResumeData(
            @RequestBody ResumeRequest resumeRequest
    ) throws IOException {

        Map<String, Object> stringObjectMap = resumeService.generateResumeResponse(resumeRequest.userDescription());
        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);

    }
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadResume(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("error", "Please upload a valid file");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Dummy analysis result
            response.put("message", "File uploaded successfully");
            response.put("fileName", fileName);
            response.put("analysis", "Resume contains 5 years of experience, Java skills, and leadership qualities.");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "Error saving file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
