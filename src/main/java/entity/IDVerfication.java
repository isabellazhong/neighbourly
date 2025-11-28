package entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

public class IDVerfication {
    public Client geminiClient;
    public String model;
    public HashMap<String, String> mimeTypes;

    public IDVerfication() {
        initClient();
        model = "gemini-2.5-flash";
    }

    private void initClient() {
        String apiKey = loadApiKeyFromEnvFile();
        if (apiKey == null) {
            throw new RuntimeException("GEMINI_API key not found. Please check your .env file.");
        }
        geminiClient = Client.builder().apiKey(apiKey).build();
    }

    private String loadApiKeyFromEnvFile() {
        try {
            Path envFile = Paths.get(".env");
            if (!Files.exists(envFile)) {
                // Fallback to environment variable
                return System.getenv("GEMINI_API");
            }

            for (String line : Files.readAllLines(envFile)) {
                line = line.trim();
                if (line.startsWith("GEMINI_API=")) {
                    return line.substring("GEMINI_API=".length());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading .env file: " + e.getMessage());
        }

        // Fallback to environment variable
        return System.getenv("GEMINI_API");
    }

    private String guessMimeType(String file_path) throws IOException {
        Path filePath = Paths.get(file_path);
        try {
            String mimeType = Files.probeContentType(filePath);
            return mimeType;
        } catch (IOException e) {
            return null;
        }
    }

    public String getResponse(String filePath) throws IOException {
        String query = ("You are a fraud dectector. Verify whether this photo is a valid government ID or not." +
                "Return in a JSON format containing only the address and success of validity." +
                "Example of response: {\"success\": <boolean_value_of_success>, \"address\": {\"street\": \"<street>\"," + 
                "\"city\": \"<city>\", \"region\": \"<region>\", \"postal_code\": \"<postal_code>\", \"country\": \"<country>\"}}" +
                "Please do not include any preamble or postamble in your response.");
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        Part textPart = Part.builder().text(query).build();
        String mimeType = guessMimeType(filePath);

        Part imagePart = Part.builder()
                .inlineData(
                        com.google.genai.types.Blob.builder()
                                .mimeType(mimeType != null ? mimeType : "image/jpeg") // Fallback if null
                                .data(fileBytes)
                                .build())
                .build();

        Content content = Content.builder()
                .parts(Arrays.asList(textPart, imagePart))
                .build();
        GenerateContentResponse response = geminiClient.models.generateContent(model, content, null);
        return response.text();
    }
}
