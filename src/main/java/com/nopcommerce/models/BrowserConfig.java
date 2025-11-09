package com.nopcommerce.models;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * Immutable configuration model for browser capabilities.
 * Uses Lombok @Builder for flexible construction and @Data for getters/setters.
 * 
 * <p>
 * Benefits:
 * </p>
 * <ul>
 * <li>Immutable objects are thread-safe</li>
 * <li>Builder pattern provides readable construction</li>
 * <li>No boilerplate getter/setter code</li>
 * <li>@Singular allows adding items one by one</li>
 * </ul>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * BrowserConfig config = BrowserConfig.builder()
 * 		.browser("chrome")
 * 		.platform("LOCAL")
 * 		.headless(true)
 * 		.argument("--disable-gpu")
 * 		.argument("--no-sandbox")
 * 		.preference("download.default_directory", "/tmp")
 * 		.build();
 * </pre>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
@Data
@Builder
public class BrowserConfig {
	private final String browser;
	private final String platform;

	@Builder.Default
	private final boolean headless = false;

	@Singular // Allows adding capabilities one by one: .capability("key", "value")
	private final Map<String, Object> capabilities;

	@Singular // Allows adding arguments one by one: .argument("--headless")
	private final List<String> arguments;

	@Singular // Allows adding preferences one by one: .preference("key", "value")
	private final Map<String, Object> preferences;

	@Builder.Default
	private final TimeoutConfig timeouts = TimeoutConfig.builder().build();

	@Builder.Default
	private final WindowConfig window = WindowConfig.builder().build();

	private final RemoteConfig remoteConfig;

	/**
	 * Timeout configuration for WebDriver.
	 * 
	 * <p>
	 * Default values:
	 * </p>
	 * <ul>
	 * <li>implicit: 10 seconds</li>
	 * <li>pageLoad: 30 seconds</li>
	 * <li>script: 30 seconds</li>
	 * </ul>
	 */
	@Data
	@Builder
	public static class TimeoutConfig {
		@Builder.Default
		private final int implicit = 10;

		@Builder.Default
		private final int pageLoad = 30;

		@Builder.Default
		private final int script = 30;
	}

	/**
	 * Window size and behavior configuration.
	 * 
	 * <p>
	 * Default values:
	 * </p>
	 * <ul>
	 * <li>maximize: true</li>
	 * <li>width: 1920</li>
	 * <li>height: 1080</li>
	 * </ul>
	 */
	@Data
	@Builder
	public static class WindowConfig {
		@Builder.Default
		private final boolean maximize = true;

		@Builder.Default
		private final int width = 1920;

		@Builder.Default
		private final int height = 1080;
	}

	/**
	 * Remote execution configuration for cloud providers (LambdaTest, BrowserStack,
	 * etc.).
	 * 
	 * <p>
	 * Environment variables are automatically resolved from ${VAR_NAME} format.
	 * </p>
	 */
	@Data
	@Builder
	public static class RemoteConfig {
		private final String hubUrl;
		private final String username;
		private final String accessKey;

		@Singular // Allows adding options one by one: .option("key", "value")
		private final Map<String, Object> options;
	}
}