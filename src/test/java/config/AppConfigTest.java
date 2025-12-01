package config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppConfigTest {

    @Test
    void prefersEnvironmentValues() {
        Map<String, String> env = new HashMap<>();
        env.put("MAPBOX_TOKEN", "env-token");
        env.put("MONDODB_URI", "env-mongo");
        env.put("SENDBIRD_APP_ID", "env-app");
        env.put("SENDBIRD_MASTER_TOKEN", "env-token-2");

        AppConfig config = AppConfig.from(env::get, key -> "dotenv-" + key);

        assertEquals("env-token", config.mapboxToken());
        assertEquals("env-mongo", config.mongodbUri());
        assertEquals("env-app", config.sendbirdAppId());
        assertEquals("env-token-2", config.sendbirdMasterToken());
    }

    @Test
    void fallsBackToDotenvWhenEnvBlank() {
        Map<String, String> env = new HashMap<>();
        env.put("MAPBOX_TOKEN", "");
        env.put("MONDODB_URI", "   ");
        env.put("SENDBIRD_APP_ID", null);
        env.put("SENDBIRD_MASTER_TOKEN", "");

        Map<String, String> dotenv = new HashMap<>();
        dotenv.put("MAPBOX_TOKEN", "dotenv-map");
        dotenv.put("MONDODB_URI", "dotenv-mongo");
        dotenv.put("SENDBIRD_APP_ID", "dotenv-app");
        dotenv.put("SENDBIRD_MASTER_TOKEN", "dotenv-master");

        AppConfig config = AppConfig.from(env::get, dotenv::get);

        assertEquals("dotenv-map", config.mapboxToken());
        assertEquals("dotenv-mongo", config.mongodbUri());
        assertEquals("dotenv-app", config.sendbirdAppId());
        assertEquals("dotenv-master", config.sendbirdMasterToken());
    }

    @Test
    void returnsNullWhenUnconfigured() {
        Function<String, String> empty = key -> null;
        AppConfig config = AppConfig.from(empty, empty);
        assertNull(config.mapboxToken());
        assertNull(config.mongodbUri());
        assertNull(config.sendbirdAppId());
        assertNull(config.sendbirdMasterToken());
    }

    @Test
    void loadReadsFromRealEnvironment() {
        assertNotNull(AppConfig.load());
    }

    @Test
    void loadHandlesDotenvFailure() {
        AppConfig config = AppConfig.load(() -> { throw new DotenvException(new RuntimeException("fail")); });
        assertNotNull(config);
    }

    @Test
    void loadUsesProvidedDotenvLookup() throws Exception {
        Dotenv dotenv = mock(Dotenv.class);
        when(dotenv.get("MAPBOX_TOKEN")).thenReturn("dotenv-token");
        AppConfig config = AppConfig.load(() -> dotenv);
        assertEquals("dotenv-token", config.mapboxToken());
    }

    @Test
    void fromHandlesNullLookups() {
        AppConfig config = AppConfig.from(null, null);
        assertNull(config.mapboxToken());
    }
}

