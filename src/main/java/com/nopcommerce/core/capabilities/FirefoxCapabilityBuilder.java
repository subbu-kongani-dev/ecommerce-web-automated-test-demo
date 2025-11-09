package com.nopcommerce.core.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.nopcommerce.models.BrowserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Firefox-specific capability builder.
 * Implements Strategy pattern for Firefox browser configuration.
 * 
 * <p>
 * Features:
 * </p>
 * <ul>
 * <li>Supports both local and remote execution</li>
 * <li>Configures headless mode</li>
 * <li>Handles Firefox preferences (about:config settings)</li>
 * <li>Applies Firefox-specific arguments</li>
 * <li>Supports LambdaTest cloud execution</li>
 * </ul>
 * 
 * <p>
 * Common Firefox Arguments:
 * </p>
 * <ul>
 * <li>-headless - Headless mode</li>
 * <li>-private - Private browsing mode</li>
 * <li>-safe-mode - Safe mode (no extensions)</li>
 * </ul>
 * 
 * <p>
 * Common Firefox Preferences:
 * </p>
 * <ul>
 * <li>browser.download.folderList (2 = custom directory)</li>
 * <li>browser.download.dir (download directory path)</li>
 * <li>browser.helperApps.neverAsk.saveToDisk (MIME types)</li>
 * <li>dom.webdriver.enabled (false to hide automation)</li>
 * </ul>
 * 
 * @author NopCommerce Team
 * @version 2.0
 * @since 2.0
 */
@Slf4j
public class FirefoxCapabilityBuilder implements CapabilityBuilder {

	@Override
	public MutableCapabilities build(BrowserConfig config) {
		log.debug("Building Firefox capabilities");

		FirefoxOptions options = new FirefoxOptions();

		// Apply headless mode
		if (config.isHeadless()) {
			options.addArguments("-headless");
			log.debug("Headless mode enabled");
		}

		// Apply arguments
		if (config.getArguments() != null && !config.getArguments().isEmpty()) {
			config.getArguments().forEach(options::addArguments);
			log.debug("Applied {} Firefox arguments", config.getArguments().size());
		}

		// Apply preferences (Firefox uses different preference types)
		if (config.getPreferences() != null && !config.getPreferences().isEmpty()) {
			config.getPreferences().forEach((key, value) -> {
				if (value instanceof Boolean) {
					options.addPreference(key, (Boolean) value);
				} else if (value instanceof Integer) {
					options.addPreference(key, (Integer) value);
				} else {
					options.addPreference(key, value.toString());
				}
			});
			log.debug("Applied {} Firefox preferences", config.getPreferences().size());
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

		log.info("Firefox capabilities built successfully");
		return options;
	}

	/**
	 * Applies cloud provider options (LambdaTest, BrowserStack, etc.).
	 * 
	 * @param options FirefoxOptions to apply remote settings to
	 * @param config  BrowserConfig containing remote configuration
	 */
	private void applyRemoteOptions(FirefoxOptions options, BrowserConfig config) {
		BrowserConfig.RemoteConfig remote = config.getRemoteConfig();

		if (remote.getOptions() != null && !remote.getOptions().isEmpty()) {
			Map<String, Object> cloudOptions = new HashMap<>(remote.getOptions());
			options.setCapability("LT:Options", cloudOptions);
			log.debug("Applied {} remote cloud options", cloudOptions.size());
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
