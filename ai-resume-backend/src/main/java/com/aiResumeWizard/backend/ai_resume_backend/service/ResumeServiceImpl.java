package com.aiResumeWizard.backend.ai_resume_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;



    @Service
    public class ResumeServiceImpl implements ResumeService {

        private ChatClient chatClient;

        public ResumeServiceImpl(ChatClient.Builder builder) {
            this.chatClient = builder.build();
        }

        @Override
        public   Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException {

            String promptString = this.loadPromptFromFile("resume_prompt.txt");
            String promptContent = this.putValuesToTemplate(promptString, Map.of(
                    "userDescription", userResumeDescription
            ));
            Prompt prompt = new Prompt(promptContent);
            String response = chatClient.prompt(prompt).call().content();
            Map<String, Object> stringObjectMap = parseMultipleResponses(response);
            //modify :
            return stringObjectMap;
        }


        String loadPromptFromFile(String filename) throws IOException {
            Path path = new ClassPathResource(filename).getFile().toPath();
            return Files.readString(path);
        }

        String putValuesToTemplate(String template, Map<String, String> values) {
            for (Map.Entry<String, String> entry : values.entrySet()) {

                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());

            }
            return template;
        }


        public static Map<String, Object> parseMultipleResponses(String response) {
            Map<String, Object> jsonResponse = new HashMap<>();


            int jsonStart = response.indexOf("```json") + 7;
            int jsonEnd = response.lastIndexOf("```");
            if (jsonStart > 6 && jsonEnd > jsonStart) {  // Ensure valid indexes
                String jsonContent = response.substring(jsonStart, jsonEnd).trim();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> dataContent = objectMapper.readValue(jsonContent, Map.class);
                    jsonResponse.put("data", dataContent);
                } catch (Exception e) {
                    jsonResponse.put("data", null);
                    System.err.println("Error parsing JSON: " + e.getMessage());
                }
            } else {
                jsonResponse.put("data", null);
                System.err.println("No valid JSON found in response.");
            }

            return jsonResponse;
        }
    }