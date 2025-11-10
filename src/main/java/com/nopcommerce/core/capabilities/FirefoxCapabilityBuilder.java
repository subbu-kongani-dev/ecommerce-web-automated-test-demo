package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Firefox-specific capability builder - YAML-driven approach.
 * 
 * This builder is now purely configuration-driven. All Firefox-specific settings
 * should be defined in firefox_local.yaml or firefox_lambdatest.yaml files.
 * 
 * NO HARDCODING - All capabilities come from YAML configuration files.
 * 
 * @author NopCommerce Team
 * @version 3.0 - Refactored to eliminate hardcoding
 * @since 2.0
 */
@Slf4j
public class FirefoxCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Firefox capabilities from YAML configuration");

		FirefoxOptions options = new FirefoxOptions();

		// Apply arguments from YAML (if any)
		if (config.getArguments() != null && !config.getArguments().isEmpty()) {
			config.getArguments().forEach(arg -> {
				if (arg != null && !arg.trim().isEmpty()) {
					options.addArguments(arg);
				}
			});
			log.debug("Applied {} Firefox arguments from YAML", config.getArguments().size());
		}

		// Apply preferences from YAML (Firefox uses different preference types)
		if (config.getPreferences() != null && !config.getPreferences().isEmpty()) {
			config.getPreferences().forEach((key, value) -> {
				if (key != null && value != null) {
					if (value instanceof Boolean) {
						options.addPreference(key, (Boolean) value);
					} else if (value instanceof Integer) {
						options.addPreference(key, (Integer) value);
					} else {
						options.addPreference(key, value.toString());
					}
				}
			});
			log.debug("Applied {} Firefox preferences from YAML", config.getPreferences().size());
		}

		// Apply capabilities from YAML
		if (config.getCapabilities() != null && !config.getCapabilities().isEmpty()) {
			config.getCapabilities().forEach(options::setCapability);
			log.debug("Applied {} general capabilities from YAML", config.getCapabilities().size());
		}

		// Apply remote config if present
		if (config.getRemoteConfig() != null) {
			applyRemoteOptions(options, config);
		}

		log.info("✅ Firefox capabilities built successfully from YAML (headless={}, args={}, prefs={})",
				config.isHeadless(),
				config.getArguments() != null ? config.getArguments().size() : 0,
				config.getPreferences() != null ? config.getPreferences().size() : 0);
		
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, etc.).
	 */
	private void applyRemoteOptions(FirefoxOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());
			options.setCapability("LT:Options", cloudOptions);
			log.debug("Applied {} remote cloud options from YAML", cloudOptions.size());
		}
	}

	@Override
	public boolean supports(String browser) {
		return "firefox".equalsIgnoreCase(browser);
	}

	@Override
	public String getBrowserType() {
		return "firefox";
	}
}
