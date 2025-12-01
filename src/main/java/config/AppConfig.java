package config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.util.function.Function;

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
        return load(() -> Dotenv.configure().ignoreIfMissing().load());
    }

    static AppConfig load(DotenvLoader loader) {
        Function<String, String> dotenvLookup = key -> null;
        try {
            Dotenv dotenv = loader.load();
            if (dotenv != null) {
                dotenvLookup = dotenv::get;
            }
        } catch (DotenvException ignored) {
        }
        return from(System::getenv, dotenvLookup);
    }

    static AppConfig from(Function<String, String> envLookup,
                          Function<String, String> dotenvLookup) {
        String mapbox = resolve("MAPBOX_TOKEN", envLookup, dotenvLookup);
        String mongo = resolve("MONDODB_URI", envLookup, dotenvLookup);
        String sbApp = resolve("SENDBIRD_APP_ID", envLookup, dotenvLookup);
        String sbToken = resolve("SENDBIRD_MASTER_TOKEN", envLookup, dotenvLookup);
        return new AppConfig(mapbox, mongo, sbApp, sbToken);
    }

    private static String resolve(String key,
                                  Function<String, String> envLookup,
                                  Function<String, String> dotenvLookup) {
        String env = envLookup != null ? envLookup.apply(key) : null;
        if (env != null && !env.isBlank()) {
            return env;
        }
        String dv = dotenvLookup != null ? dotenvLookup.apply(key) : null;
        if (dv != null && !dv.isBlank()) {
            return dv;
        }
        return null;
    }

    @FunctionalInterface
    interface DotenvLoader {
        Dotenv load() throws DotenvException;
    }

    public String mapboxToken() { return mapboxToken; }
    public String mongodbUri() { return mongodbUri; }
    public String sendbirdAppId() { return sendbirdAppId; }
    public String sendbirdMasterToken() { return sendbirdMasterToken; }
}
