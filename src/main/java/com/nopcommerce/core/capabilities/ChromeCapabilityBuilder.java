package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Chrome-specific capability builder - YAML-driven approach.
 * 
 * This builder is now purely configuration-driven. All Chrome-specific settings
 * should be defined in chrome_local.yaml or chrome_lambdatest.yaml files.
 * 
 * NO HARDCODING - All capabilities come from YAML configuration files.
 * 
 * @author NopCommerce Team
 * @version 3.0 - Refactored to eliminate hardcoding
 * @since 2.0
 */
@Slf4j
public class ChromeCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Chrome capabilities from YAML configuration");

		ChromeOptions options = new ChromeOptions();

		// Apply arguments from YAML
		if (config.getArguments() != null && !config.getArguments().isEmpty()) {
			config.getArguments().forEach(options::addArguments);
			log.debug("Applied {} Chrome arguments from YAML", config.getArguments().size());
		}

		// Apply preferences from YAML
		if (config.getPreferences() != null && !config.getPreferences().isEmpty()) {
			Map<String, Object> prefs = new HashMap<>(config.getPreferences());
			options.setExperimentalOption("prefs", prefs);
			log.debug("Applied {} Chrome preferences from YAML", prefs.size());
		}

		// Apply capabilities from YAML
		if (config.getCapabilities() != null && !config.getCapabilities().isEmpty()) {
			config.getCapabilities().forEach(options::setCapability);
			log.debug("Applied {} general capabilities from YAML", config.getCapabilities().size());
		}

		// Apply remote config if present (for LambdaTest, BrowserStack, etc.)
		if (config.getRemoteConfig() != null) {
			applyRemoteOptions(options, config);
		}

		log.info("✅ Chrome capabilities built successfully from YAML (headless={}, args={}, prefs={})",
				config.isHeadless(), 
				config.getArguments() != null ? config.getArguments().size() : 0,
				config.getPreferences() != null ? config.getPreferences().size() : 0);
		
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, etc.).
	 */
	private void applyRemoteOptions(ChromeOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());
			options.setCapability("LT:Options", cloudOptions);
			log.debug("Applied {} remote cloud options from YAML", cloudOptions.size());
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