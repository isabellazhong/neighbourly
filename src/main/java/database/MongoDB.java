package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.concurrent.TimeUnit;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB {
    private static final String URI_KEY = "mongodbURI";
    private static final List<Path> DOT_ENV_LOCATIONS = List.of(
            Paths.get(".env"),
            Paths.get("src/main/java/.env"));

    private String uri;
    private String databaseName;
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDB() {
        uri = resolveConnectionString();
        databaseName = "Neighbourly";

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(codecRegistry)
                .applyToConnectionPoolSettings(builder -> builder.maxConnectionLifeTime(30, TimeUnit.SECONDS)
                        .maxConnectionIdleTime(15, TimeUnit.SECONDS))
                .applyToSocketSettings(builder -> builder.connectTimeout(2, TimeUnit.SECONDS)
                        .readTimeout(2, TimeUnit.SECONDS))
                .applyToClusterSettings(builder -> builder.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry);
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    private String resolveConnectionString() {
        String envValue = System.getenv(URI_KEY);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        for (Path path : DOT_ENV_LOCATIONS) {
            if (!Files.exists(path)) {
                continue;
            }

            try {
                for (String line : Files.readAllLines(path)) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                        continue;
                    }

                    int separatorIndex = trimmedLine.indexOf('=');
                    if (separatorIndex == -1) {
                        continue;
                    }

                    String key = trimmedLine.substring(0, separatorIndex).trim();
                    String value = trimmedLine.substring(separatorIndex + 1).trim();
                    if (URI_KEY.equals(key) && !value.isEmpty()) {
                        return value;
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read .env file at " + path, e);
            }
        }

        throw new IllegalStateException("mongodbURI not configured. Set the env var or add it to a .env file.");
    }
}
