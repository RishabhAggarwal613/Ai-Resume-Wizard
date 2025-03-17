package com.aiResumeWizard.backend.ai_resume_backend.service;

import java.io.IOException;
import java.util.Map;

public interface ResumeService {

    Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException;
}
