package config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Centralized configuration loader. Reads from environment variables first, then .env.
 */
public class AppConfig {
    private final String mapboxToken;
    private final String mongodbUri;
    private final String sendbirdAppId;
    private final String sendbirdMasterToken;

    private AppConfig(String mapboxToken,
                      String mongodbUri,
                      String sendbirdAppId,
                      String sendbirdMasterToken) {
        this.mapboxToken = mapboxToken;
        this.mongodbUri = mongodbUri;
        this.sendbirdAppId = sendbirdAppId;
        this.sendbirdMasterToken = sendbirdMasterToken;
    }

    public static AppConfig load() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String mapbox = envOrDotenv("MAPBOX_TOKEN", dotenv);
        String mongo = envOrDotenv("MONDODB_URI", dotenv);
        String sbApp = envOrDotenv("SENDBIRD_APP_ID", dotenv);
        String sbToken = envOrDotenv("SENDBIRD_MASTER_TOKEN", dotenv);
        return new AppConfig(mapbox, mongo, sbApp, sbToken);
    }

    private static String envOrDotenv(String key, Dotenv dotenv) {
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) return env;
        String dv = dotenv.get(key);
        if (dv != null && !dv.isBlank()) return dv;
        return null;
    }

    public String mapboxToken() { return mapboxToken; }
    public String mongodbUri() { return mongodbUri; }
    public String sendbirdAppId() { return sendbirdAppId; }
    public String sendbirdMasterToken() { return sendbirdMasterToken; }
}
