package com.nopcommerce.core.driver;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.nopcommerce.core.capabilities.CapabilityBuilder;
import com.nopcommerce.core.capabilities.CapabilityLoader;
import com.nopcommerce.core.capabilities.ChromeCapabilityBuilder;
import com.nopcommerce.core.capabilities.EdgeCapabilityBuilder;
import com.nopcommerce.core.capabilities.FirefoxCapabilityBuilder;
import com.nopcommerce.core.capabilities.SafariCapabilityBuilder;
import com.nopcommerce.exceptions.DriverException;
import com.nopcommerce.models.BrowserConfig;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory for creating WebDriver instances using Strategy and Factory patterns.
 * 
 * <p>
 * <strong>Design Patterns Applied:</strong>
 * </p>
 * <ul>
 * <li><strong>Factory Pattern</strong> - Encapsulates driver creation
 * logic</li>
 * <li><strong>Strategy Pattern</strong> - Pluggable capability builders per
 * browser</li>
 * <li><strong>Builder Pattern</strong> - Configuration via BrowserConfig</li>
 * </ul>
 * 
 * <p>
 * <strong>Features:</strong>
 * </p>
 * <ul>
 * <li>Automatic driver management via WebDriverManager</li>
 * <li>Support for local and remote (LambdaTest) execution</li>
 * <li>Pluggable capability builders for extensibility</li>
 * <li>Centralized timeout and window configuration</li>
 * <li>Comprehensive error handling with clear messages</li>
 * </ul>
 * 
 * <p>
 * <strong>Usage Example:</strong>
 * </p>
 * 
 * <pre>
 * DriverFactory factory = new DriverFactory();
 * WebDriver driver = factory.createDriver("chrome", "LOCAL");
 * </pre>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
@Slf4j
public class DriverFactory {

	private final CapabilityLoader capabilityLoader;
	private final List<CapabilityBuilder> capabilityBuilders;

	/**
	 * Initializes the factory with capability loader and builders.
	 * Registers all available capability builders.
	 */
	public DriverFactory() {
		this.capabilityLoader = new CapabilityLoader();
		this.capabilityBuilders = loadCapabilityBuilders();
	}

	/**
	 * Loads all available capability builders.
	 * In the future, this can use ServiceLoader for plugin architecture.
	 * 
	 * @return List of registered capability builders
	 */
	private List<CapabilityBuilder> loadCapabilityBuilders() {
		List<CapabilityBuilder> builders = new ArrayList<>();

		// Register all browser capability builders
		builders.add(new ChromeCapabilityBuilder());
		builders.add(new FirefoxCapabilityBuilder());
		builders.add(new EdgeCapabilityBuilder());
		builders.add(new SafariCapabilityBuilder());

		log.debug("Loaded {} capability builders: {}",
				builders.size(),
				builders.stream().map(CapabilityBuilder::getBrowserType).toList());

		return builders;
	}

	/**
	 * Creates WebDriver instance based on browser and platform.
	 * 
	 * <p>
	 * <strong>Execution Flow:</strong>
	 * </p>
	 * <ol>
	 * <li>Load configuration from YAML file</li>
	 * <li>Find appropriate capability builder</li>
	 * <li>Create driver (local or remote)</li>
	 * <li>Configure timeouts and window</li>
	 * </ol>
	 * 
	 * @param browser  Browser name (chrome, firefox, edge, safari)
	 * @param platform Platform type (local, lambdatest)
	 * @return Configured WebDriver instance
	 * @throws DriverException if driver creation fails
	 */
	public WebDriver createDriver(String browser, String platform) {
		log.info("Creating {} driver for {} platform", browser, platform);

		try {
			// Load configuration from YAML
			BrowserConfig config = capabilityLoader.load(browser, platform);

			// Create driver based on platform
			WebDriver driver = "LAMBDATEST".equalsIgnoreCase(platform)
					? createRemoteDriver(config)
					: createLocalDriver(config);

			// Configure driver (timeouts, window)
			configureDriver(driver, config);

			log.info("✅ {} driver created successfully for {} platform", browser, platform);
			return driver;

		} catch (Exception e) {
			log.error("❌ Failed to create driver for {} on {}", browser, platform, e);
			throw new DriverException("Failed to create driver: " + browser + " on " + platform, e);
		}
	}

	/**
	 * Creates local WebDriver instance.
	 * Uses WebDriverManager for automatic driver binary management.
	 * 
	 * @param config Browser configuration
	 * @return Local WebDriver instance
	 * @throws DriverException if browser is not supported
	 */
	private WebDriver createLocalDriver(BrowserConfig config) {
		String browser = config.getBrowser().toLowerCase();
		log.debug("Creating local {} driver", browser);

		// Find appropriate capability builder
		CapabilityBuilder builder = findCapabilityBuilder(browser);

		// Setup driver binary automatically
		setupDriverBinary(browser);

		// Create driver based on browser type
		return switch (browser) {
			case "chrome" -> {
				ChromeOptions options = (ChromeOptions) builder.build(config);
				yield new ChromeDriver(options);
			}
			case "firefox" -> {
				FirefoxOptions options = (FirefoxOptions) builder.build(config);
				yield new FirefoxDriver(options);
			}
			case "edge", "msedge" -> {
				EdgeOptions options = (EdgeOptions) builder.build(config);
				yield new EdgeDriver(options);
			}
			case "safari" -> {
				SafariOptions options = (SafariOptions) builder.build(config);
				yield new SafariDriver(options);
			}
			default -> throw new DriverException("Unsupported browser: " + browser);
		};
	}

	/**
	 * Creates remote WebDriver instance for cloud execution.
	 * Supports LambdaTest, BrowserStack, Sauce Labs, etc.
	 * 
	 * @param config Browser configuration with remote settings
	 * @return Remote WebDriver instance
	 * @throws DriverException if remote configuration is invalid or connection
	 *                         fails
	 */
	private WebDriver createRemoteDriver(BrowserConfig config) {
		log.debug("Creating remote driver for {}", config.getBrowser());

		try {
			BrowserConfig.RemoteConfig remote = config.getRemoteConfig();
			validateRemoteConfig(remote);

			// Get capability builder
			CapabilityBuilder builder = findCapabilityBuilder(config.getBrowser());

			// Build hub URL with credentials
			String hubUrlString = String.format("https://%s:%s@%s",
					remote.getUsername(),
					remote.getAccessKey(),
					remote.getHubUrl().replace("https://", ""));

			URL hubUrl = new URL(hubUrlString);

			log.info("Connecting to remote grid: {}", remote.getHubUrl());
			return new RemoteWebDriver(hubUrl, builder.build(config));

		} catch (Exception e) {
			throw new DriverException("Failed to create remote driver", e);
		}
	}

	/**
	 * Finds appropriate capability builder for browser.
	 * 
	 * @param browser Browser name
	 * @return CapabilityBuilder that supports the browser
	 * @throws DriverException if no builder found
	 */
	private CapabilityBuilder findCapabilityBuilder(String browser) {
		return capabilityBuilders.stream()
				.filter(builder -> builder.supports(browser))
				.findFirst()
				.orElseThrow(() -> new DriverException(
						"No capability builder found for browser: " + browser +
								". Supported browsers: " +
								capabilityBuilders.stream().map(CapabilityBuilder::getBrowserType).toList()));
	}

	/**
	 * Sets up driver binary using WebDriverManager.
	 * Automatically downloads and configures the correct driver version.
	 * Optimized for both local and CI/CD environments.
	 * 
	 * @param browser Browser name
	 */
	private void setupDriverBinary(String browser) {
		log.debug("Setting up driver binary for {}", browser);

		try {
			switch (browser.toLowerCase()) {
				case "chrome" -> {
					WebDriverManager.chromedriver()
						.cachePath("~/.cache/selenium")
						.setup();
					log.info("Chrome driver setup completed");
				}
				case "firefox" -> {
					WebDriverManager.firefoxdriver()
						.cachePath("~/.cache/selenium")
						.setup();
					log.info("Firefox driver setup completed");
				}
				case "edge", "msedge" -> {
					WebDriverManager.edgedriver()
						.cachePath("~/.cache/selenium")
						.setup();
					log.info("Edge driver setup completed");
				}
				case "safari" -> {
					// Safari driver comes with macOS, no setup needed
					log.debug("Safari driver is built into macOS, no setup required");
				}
				default -> log.warn("Unknown browser for WebDriverManager: {}", browser);
			}
		} catch (Exception e) {
			log.error("WebDriverManager setup failed for {}: {}. This may cause driver initialization to fail.",
					browser, e.getMessage(), e);
			throw new DriverException("Failed to setup driver binary for " + browser, e);
		}
	}

	/**
	 * Validates remote configuration.
	 * Ensures all required fields are present and not empty.
	 * 
	 * @param config Remote configuration to validate
	 * @throws DriverException if configuration is invalid
	 */
	private void validateRemoteConfig(BrowserConfig.RemoteConfig config) {
		if (config == null) {
			throw new DriverException("Remote configuration is null");
		}

		if (config.getUsername() == null || config.getUsername().isEmpty() ||
				config.getAccessKey() == null || config.getAccessKey().isEmpty()) {

			throw new DriverException(String.format("""
					%n================================================================================================
					❌ ERROR: Cloud provider credentials not configured!
					================================================================================================
					Please set the following environment variables:

					For LambdaTest:
					  export LT_USERNAME="your_username"
					  export LT_ACCESS_KEY="your_access_key"

					Or add them to config.local.properties (not committed to Git):
					  lt.username=your_username
					  lt.accesskey=your_access_key

					Alternatively, run tests locally: -Dexecution.platform=LOCAL
					================================================================================================%n
					"""));
		}

		log.debug("Remote configuration validated successfully");
	}

	/**
	 * Configures driver with timeouts and window settings.
	 * Applied to both local and remote drivers.
	 * 
	 * @param driver WebDriver instance to configure
	 * @param config Browser configuration
	 */
	private void configureDriver(WebDriver driver, BrowserConfig config) {
		log.debug("Configuring driver timeouts and window");

		// Configure timeouts
		BrowserConfig.TimeoutConfig timeouts = config.getTimeouts();
		driver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(timeouts.getImplicit()))
				.pageLoadTimeout(Duration.ofSeconds(timeouts.getPageLoad()))
				.scriptTimeout(Duration.ofSeconds(timeouts.getScript()));

		log.debug("✅ Timeouts configured: implicit={}s, pageLoad={}s, script={}s",
				timeouts.getImplicit(), timeouts.getPageLoad(), timeouts.getScript());

		// Configure window (skip for remote/headless)
		if (!"LAMBDATEST".equalsIgnoreCase(config.getPlatform()) && !config.isHeadless()) {
			BrowserConfig.WindowConfig window = config.getWindow();

			if (window.isMaximize()) {
				driver.manage().window().maximize();
				log.debug("✅ Window maximized");
			} else if (window.getWidth() > 0 && window.getHeight() > 0) {
				driver.manage().window().setSize(
						new Dimension(window.getWidth(), window.getHeight()));
				log.debug("✅ Window size set to: {}x{}", window.getWidth(), window.getHeight());
			}
		} else {
			log.debug("Window configuration skipped (remote or headless mode)");
		}
	}
}
