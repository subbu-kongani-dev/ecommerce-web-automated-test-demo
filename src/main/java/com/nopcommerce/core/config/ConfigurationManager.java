package com.nopcommerce.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import com.nopcommerce.exceptions.ConfigurationException;

import lombok.extern.slf4j.Slf4j;

/**
 * Thread-safe configuration manager using lazy initialization holder pattern.
 * Merges multiple property sources with precedence: System Properties > Env
 * Vars > Local Config > Default Config
 */
@Slf4j
public final class ConfigurationManager {

	private static final String DEFAULT_CONFIG = "config.properties";
	private static final String LOCAL_CONFIG = "config.local.properties";

	private final Properties properties;

	private ConfigurationManager() {
		this.properties = loadConfiguration();
	}

	public static ConfigurationManager getInstance() {
		return Holder.INSTANCE;
	}

	private static class Holder {
		private static final ConfigurationManager INSTANCE = new ConfigurationManager();
	}

	private Properties loadConfiguration() {
		Properties props = new Properties();

		// Load default config
		loadPropertiesFromResource(DEFAULT_CONFIG, props, true);

		// Load local config (optional)
		loadPropertiesFromResource(LOCAL_CONFIG, props, false);

		log.info("Configuration loaded successfully");
		return props;
	}

	private void loadPropertiesFromResource(String resourceName, Properties target, boolean required) {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
			if (is != null) {
				target.load(is);
				log.debug("Loaded properties from: {}", resourceName);
			} else if (required) {
				throw new ConfigurationException("Required config file not found: " + resourceName);
			}
		} catch (IOException e) {
			if (required) {
				throw new ConfigurationException("Failed to load config: " + resourceName, e);
			}
			log.debug("Optional config not found: {}", resourceName);
		}
	}

	/**
	 * Gets property with precedence: System Property > Environment Variable >
	 * Config File
	 */
	public String get(String key) {
		return Optional.ofNullable(System.getProperty(key))
				.or(() -> Optional.ofNullable(System.getenv(key.toUpperCase().replace(".", "_"))))
				.orElseGet(() -> properties.getProperty(key));
	}

	public String get(String key, String defaultValue) {
		return Optional.ofNullable(get(key)).orElse(defaultValue);
	}

	public int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(get(key));
		} catch (NumberFormatException e) {
			log.warn("Invalid integer for key: {}, using default: {}", key, defaultValue);
			return defaultValue;
		}
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
	}

	// ==================== Convenient Getters ====================

	public String getAppUrl() {
		return get("app.url");
	}

	public String getBrowser() {
		return get("browser", "chrome");
	}

	public String getPlatform() {
		return get("execution.platform", "LOCAL");
	}

	public boolean isHeadless() {
		return getBoolean("headless", false);
	}

	public int getImplicitWait() {
		return getInt("implicit.wait", 10);
	}

	public int getExplicitWait() {
		return getInt("explicit.wait", 20);
	}

	public int getPageLoadTimeout() {
		return getInt("page.load.timeout", 30);
	}

	public String getScreenshotPath() {
		return get("screenshot.path", "test-output/screenshots/");
	}

	public String getReportPath() {
		return get("report.path", "test-output/reports/");
	}

	public int getThreadCount() {
		return getInt("thread.count", 1);
	}

	/**
	 * Gets LambdaTest username from environment or config.
	 * Priority: LT_USERNAME env > config.local > config
	 */
	public String getLambdaTestUsername() {
		String ltUsername = System.getenv("LT_USERNAME");
		if (ltUsername != null && !ltUsername.isEmpty()) {
			log.debug("Using LT_USERNAME from environment variable");
			return ltUsername;
		}
		return get("lt.username");
	}

	/**
	 * Gets LambdaTest access key from environment or config.
	 * Priority: LT_ACCESS_KEY env > config.local > config
	 */
	public String getLambdaTestAccessKey() {
		String ltAccessKey = System.getenv("LT_ACCESS_KEY");
		if (ltAccessKey != null && !ltAccessKey.isEmpty()) {
			log.debug("Using LT_ACCESS_KEY from environment variable");
			return ltAccessKey;
		}
		return get("lt.accesskey");
	}
}