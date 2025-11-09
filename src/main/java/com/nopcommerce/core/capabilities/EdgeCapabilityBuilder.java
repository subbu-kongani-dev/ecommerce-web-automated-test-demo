package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.edge.EdgeOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Edge-specific capability builder.
 * Implements Strategy pattern for Microsoft Edge browser configuration.
 * 
 * <p>
 * Features:
 * </p>
 * <ul>
 * <li>Supports both local and remote execution</li>
 * <li>Configures headless mode</li>
 * <li>Applies Edge-specific arguments (Chromium-based)</li>
 * <li>Handles preferences and experimental options</li>
 * <li>Supports LambdaTest cloud execution</li>
 * </ul>
 * 
 * <p>
 * Note: Microsoft Edge is Chromium-based, so it supports similar arguments to
 * Chrome.
 * </p>
 * 
 * <p>
 * Common Edge Arguments:
 * </p>
 * <ul>
 * <li>--headless - Headless mode</li>
 * <li>--disable-gpu - Disable GPU acceleration</li>
 * <li>--no-sandbox - Disable sandbox (for Docker/CI)</li>
 * <li>--disable-dev-shm-usage - Overcome limited resource problems</li>
 * <li>--inprivate - InPrivate browsing mode</li>
 * </ul>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
@Slf4j
public class EdgeCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Edge capabilities");

		EdgeOptions options = new EdgeOptions();

		// Apply headless mode
		if (config.isHeadless()) {
			options.addArguments("--headless");
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
			log.debug("Applied {} Edge arguments", config.getArguments().size());
		}

		// Apply preferences
		if (config.getPreferences() != null && !config.getPreferences().isEmpty()) {
			Map<String, Object> prefs = new HashMap<>(config.getPreferences());
			options.setExperimentalOption("prefs", prefs);
			log.debug("Applied {} Edge preferences", prefs.size());
		}

		// Apply general capabilities
		if (config.getCapabilities() != null && !config.getCapabilities().isEmpty()) {
			config.getCapabilities().forEach(options::setCapability);
			log.debug("Applied {} general capabilities", config.getCapabilities().size());
		}

		// Apply remote config if present
		if (config.getRemoteConfig() != null) {
			applyRemoteOptions(options, config);
		}

		log.info("Edge capabilities built successfully");
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, etc.).
	 * 
	 * @param options EdgeOptions to apply remote settings to
	 * @param config  BrowserConfig containing remote configuration
	 */
	private void applyRemoteOptions(EdgeOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());
			options.setCapability("LT:Options", cloudOptions);
			log.debug("Applied {} remote cloud options", cloudOptions.size());
		}
	}

	@Override
	public boolean supports(String browser) {
		return "edge".equalsIgnoreCase(browser) || "msedge".equalsIgnoreCase(browser);
	}

	@Override
	public String getBrowserType() {
		return "edge";
	}
}
