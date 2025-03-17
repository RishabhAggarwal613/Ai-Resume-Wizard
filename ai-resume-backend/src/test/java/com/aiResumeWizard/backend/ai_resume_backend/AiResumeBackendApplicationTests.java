package com.aiResumeWizard.backend.ai_resume_backend;

import com.aiResumeWizard.backend.ai_resume_backend.service.ResumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class AiResumeBackendApplicationTests {

	@Autowired
	private ResumeService resumeService;
	@Test
	void contextLoads() throws IOException {
		resumeService.generateResumeResponse("I am Rishabh Aggarwal Who is a Java Full Stack developer");
	}

}
