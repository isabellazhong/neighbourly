package messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SendbirdMessagingService {
    private static final String APP_ID = "83E62ED2-27B5-4D28-AD74-332D4AAB090D";
    private static final String MASTER_API_TOKEN = "11f5af485757cf14428eec98745bf3bfbec22f4f";
    private static final String BASE_URL = "https://api-" + APP_ID + ".sendbird.com/v3";
    
    private final OkHttpClient client;

    public SendbirdMessagingService() {
        this.client = new OkHttpClient();
    }

    public String createChannel(String userA, String userB) throws IOException {
        String url = BASE_URL + "/group_channels";
        
        JSONObject body = new JSONObject();
        JSONArray userIds = new JSONArray();
        userIds.put(userA);
        userIds.put(userB);
        body.put("user_ids", userIds);
        body.put("is_distinct", true);

        RequestBody requestBody = RequestBody.create(
            body.toString(),
            MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to create channel: " + response.code() + " " + response.message());
            }
            
            ResponseBody responseBodyObj = response.body();
            if (responseBodyObj == null) {
                throw new IOException("Response body is null");
            }
            String responseBody = responseBodyObj.string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getString("channel_url");
        }
    }

    public List<MessageDTO> fetchMessages(String channelId) throws IOException {
        String url = BASE_URL + "/group_channels/" + channelId + "/messages?message_ts=0&limit=100";
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch messages: " + response.code() + " " + response.message());
            }
            
            ResponseBody responseBodyObj = response.body();
            if (responseBodyObj == null) {
                throw new IOException("Response body is null");
            }
            String responseBody = responseBodyObj.string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray messages = jsonResponse.getJSONArray("messages");
            
            List<MessageDTO> messageList = new ArrayList<>();
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String userId;
                if (message.has("user") && message.get("user") instanceof JSONObject) {
                    userId = ((JSONObject) message.get("user")).getString("user_id");
                } else {
                    userId = message.getString("user_id");
                }
                messageList.add(new MessageDTO(
                    String.valueOf(message.getLong("message_id")),
                    userId,
                    message.getString("message"),
                    message.getLong("created_at")
                ));
            }
            return messageList;
        }
    }

    public void sendMessage(String channelId, String senderId, String text) throws IOException {
        String url = BASE_URL + "/group_channels/" + channelId + "/messages";
        
        JSONObject body = new JSONObject();
        body.put("message_type", "MESG");
        body.put("user_id", senderId);
        body.put("message", text);

        RequestBody requestBody = RequestBody.create(
            body.toString(),
            MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to send message: " + response.code() + " " + response.message());
            }
        }
    }

    public List<MessageDTO> pollNewMessages(String channelId, long lastTimestamp) throws IOException {
        String url = BASE_URL + "/group_channels/" + channelId + "/messages?message_ts=" + lastTimestamp + "&limit=100";
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Api-Token", MASTER_API_TOKEN)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to poll messages: " + response.code() + " " + response.message());
            }
            
            ResponseBody responseBodyObj = response.body();
            if (responseBodyObj == null) {
                throw new IOException("Response body is null");
            }
            String responseBody = responseBodyObj.string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray messages = jsonResponse.getJSONArray("messages");
            
            List<MessageDTO> messageList = new ArrayList<>();
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                long messageTimestamp = message.getLong("created_at");
                
                // Only include messages after the lastTimestamp
                if (messageTimestamp > lastTimestamp) {
                    String userId;
                    if (message.has("user") && message.get("user") instanceof JSONObject) {
                        userId = ((JSONObject) message.get("user")).getString("user_id");
                    } else {
                        userId = message.getString("user_id");
                    }
                    messageList.add(new MessageDTO(
                        String.valueOf(message.getLong("message_id")),
                        userId,
                        message.getString("message"),
                        messageTimestamp
                    ));
                }
            }
            return messageList;
        }
    }
}
