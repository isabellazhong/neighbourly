package entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendbirdMessagingService {

    private static final String APP_ID;
    private static final String MASTER_API_TOKEN;
    private static final String BASE_URL;

    static {
        // Load .env file
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        
        // Get values from environment variables (from .env or system env)
        String appId = dotenv.get("SENDBIRD_APP_ID", System.getenv("SENDBIRD_APP_ID"));
        String masterToken = dotenv.get("SENDBIRD_MASTER_TOKEN", System.getenv("SENDBIRD_MASTER_TOKEN"));
        
        if (appId == null || appId.isEmpty()) {
            throw new IllegalStateException("SENDBIRD_APP_ID environment variable is not set. Please check your .env file.");
        }
        if (masterToken == null || masterToken.isEmpty()) {
            throw new IllegalStateException("SENDBIRD_MASTER_TOKEN environment variable is not set. Please check your .env file.");
        }
        
        APP_ID = appId;
        MASTER_API_TOKEN = masterToken;
        BASE_URL = "https://api-" + APP_ID + ".sendbird.com/v3";
    }

    private final OkHttpClient client;

    public SendbirdMessagingService() {
        this.client = new OkHttpClient();
    }

    // ----------------------------------------------------------
    // Create user (v3 API requires nickname + user_id)
    // ----------------------------------------------------------
    public void createUser(String userId) throws IOException {
        String url = BASE_URL + "/users";

        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        body.put("nickname", userId);
        body.put("profile_url", "https://example.com/default-profile.png"); // REQUIRED by your app

        RequestBody requestBody = RequestBody.create(
                body.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Api-Token", MASTER_API_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body() != null ? response.body().string() : "";
                throw new IOException("Failed to create user (" + response.code() + "): " + err);
            }
        }
    }

    // ----------------------------------------------------------
    // Create 1–1 distinct channel
    // ----------------------------------------------------------
    public String createChannel(String userA, String userB) throws IOException {
        String url = BASE_URL + "/group_channels";

        JSONArray userIds = new JSONArray();
        userIds.put(userA);
        userIds.put(userB);

        JSONObject body = new JSONObject();
        body.put("user_ids", userIds);
        body.put("is_distinct", true); // ensures same 1-1 channel reused

        RequestBody requestBody = RequestBody.create(
            body.toString(),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body() != null ? response.body().string() : "";
                throw new IOException("Failed to create channel (" + response.code() + "): " + err);
            }

            String json = response.body().string();
            JSONObject parsed = new JSONObject(json);
            return parsed.getString("channel_url");
        }
    }

    // ----------------------------------------------------------
    // Fetch ALL messages (v3)
    // ----------------------------------------------------------
    public List<MessageDTO> fetchMessages(String channelId) throws IOException {
        String url = BASE_URL + "/group_channels/" + channelId + "/messages?message_ts=0&limit=50";

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body() != null ? response.body().string() : "";
                throw new IOException("Failed to fetch messages (" + response.code() + "): " + err);
            }

            String json = response.body().string();
            JSONObject parsed = new JSONObject(json);
            JSONArray arr = parsed.getJSONArray("messages");

            List<MessageDTO> result = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject msg = arr.getJSONObject(i);

                String sender;
                if (msg.has("user") && msg.get("user") instanceof JSONObject) {
                    sender = msg.getJSONObject("user").optString("user_id", "unknown");
                } else {
                    sender = msg.optString("user_id", "unknown");
                }

                result.add(new MessageDTO(
                    String.valueOf(msg.getLong("message_id")),
                    sender,
                    msg.optString("message", ""),
                    msg.getLong("created_at")
                ));
            }

            return result;
        }
    }

    // ----------------------------------------------------------
    // Send a message to channel
    // ----------------------------------------------------------
    public void sendMessage(String channelId, String senderId, String text) throws IOException {
        String url = BASE_URL + "/group_channels/" + channelId + "/messages";

        JSONObject body = new JSONObject();
        body.put("message_type", "MESG");
        body.put("user_id", senderId);
        body.put("message", text);

        RequestBody requestBody = RequestBody.create(
            body.toString(),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body() != null ? response.body().string() : "";
                throw new IOException("Failed to send message (" + response.code() + "): " + err);
            }
        }
    }

    // ----------------------------------------------------------
    // Poll NEW messages > lastTimestamp
    // ----------------------------------------------------------
    public List<MessageDTO> pollNewMessages(String channelId, long lastTimestamp) throws IOException {
        String url = BASE_URL +
                "/group_channels/" + channelId +
                "/messages?message_ts=" + lastTimestamp +
                "&limit=50";

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body() != null ? response.body().string() : "";
                throw new IOException("Failed to poll messages (" + response.code() + "): " + err);
            }

            String json = response.body().string();
            JSONObject parsed = new JSONObject(json);
            JSONArray arr = parsed.getJSONArray("messages");

            List<MessageDTO> result = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject msg = arr.getJSONObject(i);
                long ts = msg.getLong("created_at");

                if (ts > lastTimestamp) {
                    String sender;
                    if (msg.has("user") && msg.get("user") instanceof JSONObject) {
                        sender = msg.getJSONObject("user").optString("user_id", "unknown");
                    } else {
                        sender = msg.optString("user_id", "unknown");
                    }

                    result.add(new MessageDTO(
                        String.valueOf(msg.getLong("message_id")),
                        sender,
                        msg.optString("message", ""),
                        ts
                    ));
                }
            }

            return result;
        }
    }
}