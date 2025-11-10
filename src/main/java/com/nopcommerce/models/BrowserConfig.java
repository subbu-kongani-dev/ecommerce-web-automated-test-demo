package com.nopcommerce.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Immutable configuration model for browser capabilities.
 * Uses manual builder pattern for flexible construction and Lombok @Data for getters/setters.
 * 
 * <p>Example usage:</p>
 * <pre>
 * BrowserConfig config = BrowserConfig.builder()
 *     .browser("chrome")
 *     .platform("LOCAL")
 *     .headless(true)
 *     .argument("--disable-gpu")
 *     .argument("--no-sandbox")
 *     .preference("download.default_directory", "/tmp")
 *     .build();
 * </pre>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
@Data
public class BrowserConfig {
	private final String browser;
	private final String platform;
	private final boolean headless;
	private final Map<String, Object> capabilities;
	private final List<String> arguments;
	private final Map<String, Object> preferences;
	private final TimeoutConfig timeouts;
	private final WindowConfig window;
	private final RemoteConfig remoteConfig;

	private BrowserConfig(BrowserConfigBuilder builder) {
		this.browser = builder.browser;
		this.platform = builder.platform;
		this.headless = builder.headless;
		this.capabilities = builder.capabilities;
		this.arguments = builder.arguments;
		this.preferences = builder.preferences;
		this.timeouts = builder.timeouts;
		this.window = builder.window;
		this.remoteConfig = builder.remoteConfig;
	}

	public static BrowserConfigBuilder builder() {
		return new BrowserConfigBuilder();
	}
	
	/**
	 * Creates a builder initialized with this config's values.
	 * Useful for creating modified copies.
	 */
	public BrowserConfigBuilder toBuilder() {
		return new BrowserConfigBuilder()
				.browser(this.browser)
				.platform(this.platform)
				.headless(this.headless)
				.capabilities(this.capabilities)
				.arguments(this.arguments)
				.preferences(this.preferences)
				.timeouts(this.timeouts)
				.window(this.window)
				.remoteConfig(this.remoteConfig);
	}

	public static class BrowserConfigBuilder {
		private String browser;
		private String platform;
		private boolean headless = false;
		private Map<String, Object> capabilities = new HashMap<>();
		private List<String> arguments = new ArrayList<>();
		private Map<String, Object> preferences = new HashMap<>();
		private TimeoutConfig timeouts;
		private WindowConfig window;
		private RemoteConfig remoteConfig;

		public BrowserConfigBuilder browser(String browser) {
			this.browser = browser;
			return this;
		}

		public BrowserConfigBuilder platform(String platform) {
			this.platform = platform;
			return this;
		}

		public BrowserConfigBuilder headless(boolean headless) {
			this.headless = headless;
			return this;
		}

		public BrowserConfigBuilder capability(String key, Object value) {
			this.capabilities.put(key, value);
			return this;
		}

		public BrowserConfigBuilder capabilities(Map<String, Object> capabilities) {
			if (capabilities != null) {
				this.capabilities.putAll(capabilities);
			}
			return this;
		}

		public BrowserConfigBuilder argument(String argument) {
			this.arguments.add(argument);
			return this;
		}

		public BrowserConfigBuilder arguments(List<String> arguments) {
			if (arguments != null) {
				this.arguments.addAll(arguments);
			}
			return this;
		}

		public BrowserConfigBuilder preference(String key, Object value) {
			this.preferences.put(key, value);
			return this;
		}

		public BrowserConfigBuilder preferences(Map<String, Object> preferences) {
			if (preferences != null) {
				this.preferences.putAll(preferences);
			}
			return this;
		}

		public BrowserConfigBuilder timeouts(TimeoutConfig timeouts) {
			this.timeouts = timeouts;
			return this;
		}

		public BrowserConfigBuilder window(WindowConfig window) {
			this.window = window;
			return this;
		}

		public BrowserConfigBuilder remoteConfig(RemoteConfig remoteConfig) {
			this.remoteConfig = remoteConfig;
			return this;
		}

		public BrowserConfig build() {
			return new BrowserConfig(this);
		}
	}

	/**
	 * Timeout configuration for WebDriver.
	 * 
	 * <p>Default values:</p>
	 * <ul>
	 *   <li>implicit: 10 seconds</li>
	 *   <li>pageLoad: 30 seconds</li>
	 *   <li>script: 30 seconds</li>
	 * </ul>
	 */
	@Data
	public static class TimeoutConfig {
		private final int implicit;
		private final int pageLoad;
		private final int script;

		private TimeoutConfig(TimeoutConfigBuilder builder) {
			this.implicit = builder.implicit;
			this.pageLoad = builder.pageLoad;
			this.script = builder.script;
		}

		public static TimeoutConfigBuilder builder() {
			return new TimeoutConfigBuilder();
		}

		public static class TimeoutConfigBuilder {
			private int implicit = 10;
			private int pageLoad = 30;
			private int script = 30;

			public TimeoutConfigBuilder implicit(int implicit) {
				this.implicit = implicit;
				return this;
			}

			public TimeoutConfigBuilder pageLoad(int pageLoad) {
				this.pageLoad = pageLoad;
				return this;
			}

			public TimeoutConfigBuilder script(int script) {
				this.script = script;
				return this;
			}

			public TimeoutConfig build() {
				return new TimeoutConfig(this);
			}
		}
	}

	/**
	 * Window size and behavior configuration.
	 * 
	 * <p>Default values:</p>
	 * <ul>
	 *   <li>maximize: true</li>
	 *   <li>width: 1920</li>
	 *   <li>height: 1080</li>
	 * </ul>
	 */
	@Data
	public static class WindowConfig {
		private final boolean maximize;
		private final int width;
		private final int height;

		private WindowConfig(WindowConfigBuilder builder) {
			this.maximize = builder.maximize;
			this.width = builder.width;
			this.height = builder.height;
		}

		public static WindowConfigBuilder builder() {
			return new WindowConfigBuilder();
		}

		public static class WindowConfigBuilder {
			private boolean maximize = true;
			private int width = 1920;
			private int height = 1080;

			public WindowConfigBuilder maximize(boolean maximize) {
				this.maximize = maximize;
				return this;
			}

			public WindowConfigBuilder width(int width) {
				this.width = width;
				return this;
			}

			public WindowConfigBuilder height(int height) {
				this.height = height;
				return this;
			}

			public WindowConfig build() {
				return new WindowConfig(this);
			}
		}
	}

	/**
	 * Remote execution configuration for cloud providers (LambdaTest, BrowserStack, etc.).
	 * 
	 * <p>Environment variables are automatically resolved from ${VAR_NAME} format.</p>
	 */
	@Data
	public static class RemoteConfig {
		private final String hubUrl;
		private final String username;
		private final String accessKey;
		private final Map<String, Object> options;

		private RemoteConfig(RemoteConfigBuilder builder) {
			this.hubUrl = builder.hubUrl;
			this.username = builder.username;
			this.accessKey = builder.accessKey;
			this.options = builder.options;
		}

		public static RemoteConfigBuilder builder() {
			return new RemoteConfigBuilder();
		}

		public static class RemoteConfigBuilder {
			private String hubUrl;
			private String username;
			private String accessKey;
			private Map<String, Object> options = new HashMap<>();

			public RemoteConfigBuilder hubUrl(String hubUrl) {
				this.hubUrl = hubUrl;
				return this;
			}

			public RemoteConfigBuilder username(String username) {
				this.username = username;
				return this;
			}

			public RemoteConfigBuilder accessKey(String accessKey) {
				this.accessKey = accessKey;
				return this;
			}

			public RemoteConfigBuilder option(String key, Object value) {
				this.options.put(key, value);
				return this;
			}

			public RemoteConfigBuilder options(Map<String, Object> options) {
				if (options != null) {
					this.options.putAll(options);
				}
				return this;
			}

			public RemoteConfig build() {
				return new RemoteConfig(this);
			}
		}
	}
}