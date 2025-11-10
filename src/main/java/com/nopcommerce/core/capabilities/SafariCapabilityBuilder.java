package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Safari-specific capability builder - YAML-driven approach.
 * 
 * This builder is now purely configuration-driven. All Safari-specific settings
 * should be defined in safari_local.yaml or safari_lambdatest.yaml files.
 * 
 * NO HARDCODING - All capabilities come from YAML configuration files.
 * 
 * Note: Safari has limitations - no headless mode, limited arguments support.
 * 
 * @author NopCommerce Team
 * @version 3.0 - Refactored to eliminate hardcoding
 * @since 2.0
 */
@Slf4j
public class SafariCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Safari capabilities from YAML configuration");

		SafariOptions options = new SafariOptions();

		// Safari limitations warnings
		if (config.isHeadless()) {
			log.warn("Safari does not support headless mode. Headless flag will be ignored.");
		}
		
		if (config.getArguments() != null && !config.getArguments().isEmpty()) {
			log.warn("Safari does not support custom arguments. {} arguments from YAML will be ignored.", 
					config.getArguments().size());
		}

		// Apply capabilities from YAML (Safari-specific capabilities)
		if (config.getCapabilities() != null && !config.getCapabilities().isEmpty()) {
			config.getCapabilities().forEach(options::setCapability);
			log.debug("Applied {} Safari capabilities from YAML", config.getCapabilities().size());
		}

		// Apply remote config if present
		if (config.getRemoteConfig() != null) {
			applyRemoteOptions(options, config);
		}

		log.info("✅ Safari capabilities built successfully from YAML (capabilities={})",
				config.getCapabilities() != null ? config.getCapabilities().size() : 0);
		
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, etc.).
	 */
	private void applyRemoteOptions(SafariOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());
			options.setCapability("LT:Options", cloudOptions);
			log.debug("Applied {} remote cloud options from YAML", cloudOptions.size());
		}
	}

	@Override
	public boolean supports(String browser) {
		return "safari".equalsIgnoreCase(browser);
	}

	@Override
	public String getBrowserType() {
		return "safari";
	}
}