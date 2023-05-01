package com.example.accessingdatamysql;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Component
public class OpenAIService {
    Logger logger = LoggerFactory.getLogger(OpenAIService.class);
    @Value("${openai.api.key}")
    private String apiKey;
    @Autowired
    ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    public OpenAIService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public String fortuneTell(String person) {
        String fortune_teller_prompt = "Act as a fortune teller, I will describe a person, please give me his prophecy. " +
                "limit the prophecy within 50 characters. The person is: " + person;
        return generateText(fortune_teller_prompt);
    }

    private String generateText(String prompt) {
        String text = "None";
        logger.info("prompt: ", prompt);
        try {
            String url = "https://api.openai.com/v1/completions";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> request = new HashMap<>();
            request.put("model", "text-davinci-003");
            request.put("prompt", prompt);
            request.put("max_tokens", 50);
            request.put("temperature", 0);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            String id = jsonNode.get("id").asText();
            String model = jsonNode.get("model").asText();
            text = jsonNode.get("choices").get(0).get("text").asText();

            logger.info("ID: ", id);
            logger.info("Model: ", model);
            logger.info("Text: ", text);
        } catch (Exception e) {
            e.printStackTrace();
            return "None";
        }

//        Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
//        String text = ((Map<String, Object>) responseMap.get("choices").get(0)).get("text").toString();
//        System.out.println(text);
//        String result = response.getBody();
//        System.out.println(response.getBody());
        return text;

//        String url = "https://api.openai.com/v1/completions";
//        System.out.printf("hey001");

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(apiKey);
//
//        String payload = String.format("{\"model\": \"text-davinci-003\",\"prompt\": \"%s\", \"max_tokens\": 60, \"n\": 1, \"stop\": [\"\\n\"]}", prompt);
//
//        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
//
//        String response = restTemplate.postForObject(url, entity, String.class);
//
//        return response;
    }
}
