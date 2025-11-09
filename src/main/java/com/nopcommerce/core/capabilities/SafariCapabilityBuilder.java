package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Safari-specific capability builder.
 * Implements Strategy pattern for Safari browser configuration.
 * 
 * <p>
 * Features:
 * </p>
 * <ul>
 * <li>Supports both local and remote execution</li>
 * <li>Applies Safari-specific capabilities</li>
 * <li>Supports LambdaTest cloud execution</li>
 * </ul>
 * 
 * <p>
 * <strong>Important Notes:</strong>
 * </p>
 * <ul>
 * <li>Safari does NOT support headless mode</li>
 * <li>Safari does NOT support custom arguments</li>
 * <li>Safari has limited preference customization</li>
 * <li>Safari only runs on macOS (local) or cloud providers</li>
 * </ul>
 * 
 * <p>
 * Common Safari Capabilities:
 * </p>
 * <ul>
 * <li>safari:automaticInspection (Boolean) - Enables automatic inspection</li>
 * <li>safari:automaticProfiling (Boolean) - Enables automatic profiling</li>
 * <li>safari:diagnose (Boolean) - Enables diagnostic mode</li>
 * </ul>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
@Slf4j
public class SafariCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Safari capabilities");

		SafariOptions options = new SafariOptions();

		// Safari doesn't support headless mode
		if (config.isHeadless()) {
			log.warn("Safari does not support headless mode. Headless configuration will be ignored.");
		}

		// Safari doesn't support custom arguments
		if (config.getArguments() != null && !config.getArguments().isEmpty()) {
			log.warn("Safari does not support custom arguments. {} arguments will be ignored.",
					config.getArguments().size());
		}

		// Safari has limited preference support
		if (config.getPreferences() != null && !config.getPreferences().isEmpty()) {
			log.warn("Safari has limited preference support. {} preferences may be ignored.",
					config.getPreferences().size());
		}

		// Apply general capabilities (Safari-specific capabilities work here)
		if (config.getCapabilities() != null && !config.getCapabilities().isEmpty()) {
			config.getCapabilities().forEach(options::setCapability);
			log.debug("Applied {} Safari capabilities", config.getCapabilities().size());
		}

		// Apply remote config if present
		if (config.getRemoteConfig() != null) {
			applyRemoteOptions(options, config);
		}

		log.info("Safari capabilities built successfully");
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, etc.).
	 * 
	 * @param options SafariOptions to apply remote settings to
	 * @param config  BrowserConfig containing remote configuration
	 */
	private void applyRemoteOptions(SafariOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());
			options.setCapability("LT:Options", cloudOptions);
			log.debug("Applied {} remote cloud options", cloudOptions.size());
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
