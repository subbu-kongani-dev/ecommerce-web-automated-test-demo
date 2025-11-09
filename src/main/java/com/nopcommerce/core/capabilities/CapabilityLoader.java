package com.nopcommerce.core.capabilities;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.nopcommerce.core.config.ConfigurationManager;
import com.nopcommerce.exceptions.ConfigurationException;
import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Loads browser configurations from YAML files.
 * Uses SnakeYAML for parsing and supports environment variable substitution.
 * 
 * @author NopCommerce Automation Team
 * @version 2.0
 */
@Slf4j
public class CapabilityLoader {

	private static final String CAPABILITIES_PATH = "capabilities/%s_%s.yaml";
	private final Yaml yaml;
	private final ConfigurationManager config;

	public CapabilityLoader() {
		this.yaml = new Yaml();
		this.config = ConfigurationManager.getInstance();
	}

	public BrowserConfig load(String browser, String platform) {
		String fileName = String.format(CAPABILITIES_PATH, browser.toLowerCase(), platform.toLowerCase());

		log.info("Loading capabilities from: {}", fileName);

		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
			if (is == null) {
				throw new ConfigurationException("Capability file not found: " + fileName);
			}

			Map<String, Object> yamlData = yaml.load(is);
			return mapToConfig(yamlData);

		} catch (Exception e) {
			throw new ConfigurationException("Failed to load capabilities: " + fileName, e);
		}
	}

	@SuppressWarnings("unchecked")
	private BrowserConfig mapToConfig(Map<String, Object> data) {
		BrowserConfig.BrowserConfigBuilder builder = BrowserConfig.builder()
				.browser((String) data.get("browser"))
				.platform((String) data.get("platform"))
				.headless((Boolean) data.getOrDefault("headless", false));

		// Map capabilities
		if (data.containsKey("capabilities")) {
			Map<String, Object> caps = (Map<String, Object>) data.get("capabilities");
			caps.forEach((key, value) -> builder.capability(key, value));
		}

		// Map options
		if (data.containsKey("options")) {
			Map<String, Object> options = (Map<String, Object>) data.get("options");
			if (options.containsKey("args")) {
				List<String> args = (List<String>) options.get("args");
				if (args != null) {
					args.forEach(builder::argument);
				}
			}
			if (options.containsKey("prefs")) {
				Map<String, Object> prefs = (Map<String, Object>) options.get("prefs");
				if (prefs != null) {
					prefs.forEach((key, value) -> builder.preference(key, value));
				}
			}
		}

		// Map timeouts (with default)
		if (data.containsKey("timeouts")) {
			builder.timeouts(mapTimeouts((Map<String, Object>) data.get("timeouts")));
		} else {
			builder.timeouts(BrowserConfig.TimeoutConfig.builder().build());
		}

		// Map window (with default)
		if (data.containsKey("window")) {
			builder.window(mapWindow((Map<String, Object>) data.get("window")));
		} else {
			builder.window(BrowserConfig.WindowConfig.builder().build());
		}

		// Map remote config
		if (data.containsKey("lambdatest")) {
			builder.remoteConfig(mapRemoteConfig((Map<String, Object>) data.get("lambdatest")));
		}

		return builder.build();
	}

	private BrowserConfig.TimeoutConfig mapTimeouts(Map<String, Object> data) {
		return BrowserConfig.TimeoutConfig.builder().implicit((Integer) data.getOrDefault("implicit", 10))
				.pageLoad((Integer) data.getOrDefault("pageLoad", 30)).script((Integer) data.getOrDefault("script", 30))
				.build();
	}

	private BrowserConfig.WindowConfig mapWindow(Map<String, Object> data) {
		return BrowserConfig.WindowConfig.builder().maximize((Boolean) data.getOrDefault("maximize", true))
				.width((Integer) data.getOrDefault("width", 1920)).height((Integer) data.getOrDefault("height", 1080))
				.build();
	}

	@SuppressWarnings("unchecked")
	private BrowserConfig.RemoteConfig mapRemoteConfig(Map<String, Object> data) {
		String username = resolveEnvVar((String) data.get("user"));
		String accessKey = resolveEnvVar((String) data.get("accessKey"));

		BrowserConfig.RemoteConfig.RemoteConfigBuilder builder = BrowserConfig.RemoteConfig.builder()
				.hubUrl((String) data.get("hubUrl"))
				.username(username)
				.accessKey(accessKey);

		// Map capabilities/LT:Options
		if (data.containsKey("capabilities")) {
			Map<String, Object> caps = (Map<String, Object>) data.get("capabilities");
			if (caps.containsKey("LT:Options")) {
				Map<String, Object> ltOptions = (Map<String, Object>) caps.get("LT:Options");
				ltOptions.forEach((key, value) -> builder.option(key, value));
			}
		}

		return builder.build();
	}

	private String resolveEnvVar(String value) {
		if (value != null && value.startsWith("${") && value.endsWith("}")) {
			String envVar = value.substring(2, value.length() - 1);

			// Try environment variable first
			String resolved = System.getenv(envVar);
			if (resolved != null) {
				log.debug("Resolved ${} from environment variable", envVar);
				return resolved;
			}

			// Fallback to configuration manager
			if ("LT_USERNAME".equals(envVar)) {
				resolved = config.getLambdaTestUsername();
			} else if ("LT_ACCESS_KEY".equals(envVar)) {
				resolved = config.getLambdaTestAccessKey();
			}

			if (resolved != null) {
				log.debug("Resolved ${} from configuration", envVar);
				return resolved;
			}

			log.warn("Could not resolve environment variable: {}", envVar);
		}
		return value;
	}
}