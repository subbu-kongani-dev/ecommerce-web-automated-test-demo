package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.edge.EdgeOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Edge-specific capability builder - YAML-driven approach.
 * 
 * This builder is now purely configuration-driven. All Edge-specific settings
 * should be defined in edge_local.yaml or edge_lambdatest.yaml files.
 * 
 * NO HARDCODING - All capabilities come from YAML configuration files.
 * 
 * @author NopCommerce Team
 * @version 3.0 - Refactored to eliminate hardcoding
 * @since 2.0
 */
@Slf4j
public class EdgeCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Edge capabilities from YAML configuration");

		EdgeOptions options = new EdgeOptions();

		// Apply arguments from YAML
		if (config.getArguments() != null && !config.getArguments().isEmpty()) {
			config.getArguments().forEach(options::addArguments);
			log.debug("Applied {} Edge arguments from YAML", config.getArguments().size());
		}

		// Apply preferences from YAML
		if (config.getPreferences() != null && !config.getPreferences().isEmpty()) {
			Map<String, Object> prefs = new HashMap<>(config.getPreferences());
			options.setExperimentalOption("prefs", prefs);
			log.debug("Applied {} Edge preferences from YAML", prefs.size());
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

		log.info("✅ Edge capabilities built successfully from YAML (args={}, prefs={})",
				config.getArguments() != null ? config.getArguments().size() : 0,
				config.getPreferences() != null ? config.getPreferences().size() : 0);
		
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, etc.).
	 */
	private void applyRemoteOptions(EdgeOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());
			options.setCapability("LT:Options", cloudOptions);
			log.debug("Applied {} remote cloud options from YAML", cloudOptions.size());
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