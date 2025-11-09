package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Chrome-specific capability builder.
 * Implements Strategy pattern for Chrome browser configuration.
 * 
 * <p>
 * Features:
 * </p>
 * <ul>
 * <li>Supports both local and remote execution</li>
 * <li>Configures headless mode with proper window size</li>
 * <li>Applies Chrome-specific arguments</li>
 * <li>Handles preferences and experimental options</li>
 * <li>Supports LambdaTest cloud execution</li>
 * </ul>
 * 
 * <p>
 * Common Chrome Arguments:
 * </p>
 * <ul>
 * <li>--headless=new - New headless mode (Chrome 109+)</li>
 * <li>--disable-gpu - Disable GPU acceleration</li>
 * <li>--no-sandbox - Disable sandbox (for Docker/CI)</li>
 * <li>--disable-dev-shm-usage - Overcome limited resource problems</li>
 * <li>--disable-extensions - Disable extensions</li>
 * <li>--disable-blink-features=AutomationControlled - Hide automation</li>
 * </ul>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
@Slf4j
public class ChromeCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Chrome capabilities");

		ChromeOptions options = new ChromeOptions();

		// Apply headless mode
		if (config.isHeadless()) {
			options.addArguments("--headless=new");
			options.addArguments(String.format("--window-size=%d,%d",
					config.getWindow().getWidth(),
					config.getWindow().getHeight()));
			log.debug("Headless mode enabled with window size: {}x{}",
					config.getWindow().getWidth(),
					config.getWindow().getHeight());
		}

		// Apply arguments
		if (config.getArguments() != null && !config.getArguments().isEmpty()) {
			config.getArguments().forEach(options::addArguments);
			log.debug("Applied {} Chrome arguments", config.getArguments().size());
		}

		// Apply preferences
		if (config.getPreferences() != null && !config.getPreferences().isEmpty()) {
			Map<String, Object> prefs = new HashMap<>(config.getPreferences());
			options.setExperimentalOption("prefs", prefs);
			log.debug("Applied {} Chrome preferences", prefs.size());
		}

		// Apply general capabilities
		if (config.getCapabilities() != null && !config.getCapabilities().isEmpty()) {
			config.getCapabilities().forEach(options::setCapability);
			log.debug("Applied {} general capabilities", config.getCapabilities().size());
		}

		// Apply remote config if present (LambdaTest, BrowserStack, etc.)
		if (config.getRemoteConfig() != null) {
			applyRemoteOptions(options, config);
		}

		log.info("Chrome capabilities built successfully");
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, Sauce Labs, etc.).
	 * 
	 * @param options ChromeOptions to apply remote settings to
	 * @param config  BrowserConfig containing remote configuration
	 */
	private void applyRemoteOptions(ChromeOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());

			// LambdaTest uses "LT:Options"
			options.setCapability("LT:Options", cloudOptions);

			log.debug("Applied {} remote cloud options", cloudOptions.size());
		}
	}

	@Override
	public boolean supports(String browser) {
		return "chrome".equalsIgnoreCase(browser);
	}

	@Override
	public String getBrowserType() {
		return "chrome";
	}
}
