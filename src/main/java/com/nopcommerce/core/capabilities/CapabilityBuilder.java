package com.nopcommerce.core.capabilities;

import org.openqa.selenium.MutableCapabilities;

import com.nopcommerce.models.BrowserConfig;

/**
 * Strategy interface for building browser capabilities.
 * Each browser implementation provides its own capability building logic.
 * 
 * <p>
 * This follows the Strategy Pattern, allowing different capability
 * building strategies for different browsers without modifying the factory.
 * </p>
 * 
 * <p>
 * Implementations:
 * </p>
 * <ul>
 * <li>{@link ChromeCapabilityBuilder} - Chrome browser capabilities</li>
 * <li>{@link FirefoxCapabilityBuilder} - Firefox browser capabilities</li>
 * <li>{@link EdgeCapabilityBuilder} - Edge browser capabilities</li>
 * <li>{@link SafariCapabilityBuilder} - Safari browser capabilities</li>
 * </ul>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * CapabilityBuilder builder = new ChromeCapabilityBuilder();
 * BrowserConfig config = BrowserConfig.builder()
 * 		.browser("chrome")
 * 		.headless(true)
 * 		.build();
 * MutableCapabilities capabilities = builder.build(config);
 * </pre>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
public interface CapabilityBuilder {

	/**
	 * Builds browser-specific capabilities from configuration.
	 * 
	 * @param config Browser configuration containing all settings
	 * @return MutableCapabilities for the browser (ChromeOptions, FirefoxOptions,
	 *         etc.)
	 * @throws IllegalArgumentException if configuration is invalid
	 */
	MutableCapabilities build(BrowserConfig config);

	/**
	 * Checks if this builder supports the given browser.
	 * 
	 * @param browser Browser name (case-insensitive)
	 * @return true if this builder supports the browser, false otherwise
	 */
	boolean supports(String browser);

	/**
	 * Gets the browser type this builder supports.
	 * 
	 * @return Browser name in lowercase (chrome, firefox, edge, safari)
	 */
	String getBrowserType();
}
