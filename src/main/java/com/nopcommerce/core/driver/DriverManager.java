package com.nopcommerce.core.driver;

import org.openqa.selenium.WebDriver;

import com.nopcommerce.core.config.ConfigurationManager;

import lombok.extern.slf4j.Slf4j;

/**
 * Manages WebDriver lifecycle using ThreadLocal for parallel test execution.
 * 
 * <p>
 * <strong>Design Patterns:</strong>
 * </p>
 * <ul>
 * <li><strong>ThreadLocal Pattern</strong> - Ensures thread-safety for parallel
 * execution</li>
 * <li><strong>Factory Pattern</strong> - Delegates driver creation to
 * DriverFactory</li>
 * <li><strong>Singleton Pattern</strong> - ConfigurationManager for centralized
 * config</li>
 * </ul>
 * 
 * <p>
 * <strong>Features:</strong>
 * </p>
 * <ul>
 * <li>Thread-safe driver management for parallel test execution</li>
 * <li>Lazy initialization - driver created only when needed</li>
 * <li>Automatic cleanup with proper resource disposal</li>
 * <li>Integrated with ConfigurationManager for dynamic configuration</li>
 * <li>Support for browser override via parameter</li>
 * </ul>
 * 
 * <p>
 * <strong>Usage in TestNG:</strong>
 * </p>
 * 
 * <pre>
 * &#64;BeforeMethod
 * public void setUp() {
 * 	WebDriver driver = DriverManager.getDriver();
 * 	driver.get("https://demo.nopcommerce.com");
 * }
 * 
 * &#64;AfterMethod
 * public void tearDown() {
 * 	DriverManager.quitDriver();
 * }
 * </pre>
 * 
 * <p>
 * <strong>Thread Safety:</strong>
 * </p>
 * 
 * <pre>
 * ThreadLocal ensures each thread gets its own WebDriver instance.
 * This is critical for parallel test execution where multiple tests
 * run simultaneously across different browsers/threads.
 * </pre>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 1.0
 */
@Slf4j
public class DriverManager {

	/**
	 * ThreadLocal storage for WebDriver instances.
	 * Each thread gets its own isolated driver instance.
	 */
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	/**
	 * Shared DriverFactory instance for creating drivers.
	 */
	private static final DriverFactory driverFactory = new DriverFactory();

	/**
	 * Shared ConfigurationManager instance for accessing configuration.
	 */
	private static final ConfigurationManager config = ConfigurationManager.getInstance();

	/**
	 * Static initializer to suppress excessive Selenium logging.
	 * Reduces console noise from WebDriver internals.
	 */
	static {
		try {
			java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.SEVERE);
			java.util.logging.Logger.getLogger("io.github.bonigarcia").setLevel(java.util.logging.Level.WARNING);
		} catch (Exception e) {
			// Ignore if logging configuration fails
			log.debug("Could not configure Selenium logging: {}", e.getMessage());
		}
	}

	/**
	 * Gets WebDriver for current thread using default browser from configuration.
	 * Creates new instance if none exists (lazy initialization).
	 * 
	 * <p>
	 * <strong>Configuration Sources (precedence):</strong>
	 * </p>
	 * <ol>
	 * <li>System Property: -Dbrowser=chrome</li>
	 * <li>Environment Variable: BROWSER=chrome</li>
	 * <li>Config File: browser=chrome</li>
	 * </ol>
	 * 
	 * @return WebDriver instance for current thread
	 * @throws RuntimeException if driver creation fails
	 */
	public static WebDriver getDriver() {
		return getDriver(null);
	}

	/**
	 * Gets WebDriver for current thread with specified browser.
	 * Creates new instance if none exists (lazy initialization).
	 * 
	 * <p>
	 * <strong>Example Usage:</strong>
	 * </p>
	 * 
	 * <pre>
	 * // Use default browser from config
	 * WebDriver driver = DriverManager.getDriver();
	 * 
	 * // Override with specific browser
	 * WebDriver firefoxDriver = DriverManager.getDriver("firefox");
	 * </pre>
	 * 
	 * @param browserName Browser to use (chrome, firefox, edge, safari).
	 *                    If null or empty, uses browser from configuration.
	 * @return WebDriver instance for current thread
	 * @throws RuntimeException if driver creation fails
	 */
	public static WebDriver getDriver(String browserName) {
		if (driver.get() == null) {
			// Determine browser: parameter > config
			String browser = (browserName != null && !browserName.isEmpty())
					? browserName
					: config.getBrowser();

			// Get platform from configuration
			String platform = config.getPlatform();

			log.info("🚀 Initializing {} browser on {} platform (Thread: {})",
					browser, platform, Thread.currentThread().getName());

			try {
				// Create driver using factory
				WebDriver webDriver = driverFactory.createDriver(browser, platform);
				driver.set(webDriver);

				log.info("✅ Driver initialized successfully for thread: {}",
						Thread.currentThread().getName());

			} catch (Exception e) {
				log.error("❌ Failed to initialize driver for thread: {}",
						Thread.currentThread().getName(), e);
				throw new RuntimeException("Failed to create WebDriver", e);
			}
		}

		return driver.get();
	}

	/**
	 * Quits driver for current thread and removes it from ThreadLocal.
	 * Safe to call multiple times (idempotent operation).
	 * 
	 * <p>
	 * <strong>Cleanup Process:</strong>
	 * </p>
	 * <ol>
	 * <li>Quit the browser (closes all windows)</li>
	 * <li>Wait briefly for process cleanup</li>
	 * <li>Remove driver from ThreadLocal</li>
	 * </ol>
	 * 
	 * <p>
	 * <strong>Note:</strong> Always call this in &#64;AfterMethod or
	 * &#64;AfterClass
	 * to prevent resource leaks and zombie browser processes.
	 * </p>
	 */
	public static void quitDriver() {
		WebDriver webDriver = driver.get();
		if (webDriver != null) {
			log.info("🔚 Closing browser session (Thread: {})", Thread.currentThread().getName());

			try {
				webDriver.quit();
				Thread.sleep(500); // Allow browser process cleanup
				log.info("✅ Browser closed successfully for thread: {}",
						Thread.currentThread().getName());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.warn("⚠️ Thread interrupted during driver quit: {}", e.getMessage());
			} catch (Exception e) {
				log.warn("⚠️ Exception during driver quit: {}", e.getMessage());
			} finally {
				driver.remove();
				log.debug("Driver reference removed from ThreadLocal");
			}
		} else {
			log.debug("No driver to quit for thread: {}", Thread.currentThread().getName());
		}
	}

	/**
	 * Checks if driver exists for current thread.
	 * Useful for conditional logic in test frameworks.
	 * 
	 * @return true if driver exists for current thread, false otherwise
	 */
	public static boolean hasDriver() {
		return driver.get() != null;
	}

	/**
	 * Sets an existing WebDriver instance for current thread.
	 * Used for advanced scenarios where driver is created externally.
	 * 
	 * <p>
	 * <strong>Warning:</strong> This bypasses the factory pattern.
	 * Use only when you need full control over driver creation.
	 * </p>
	 * 
	 * @param webDriver WebDriver instance to set
	 * @deprecated Use {@link #getDriver(String)} instead for standard usage
	 */
	@Deprecated(since = "2.0", forRemoval = false)
	public static void setDriver(WebDriver webDriver) {
		driver.set(webDriver);
		log.info("WebDriver manually set for thread: {}", Thread.currentThread().getName());
	}
}
