package com.nopcommerce.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.nopcommerce.base.BaseTest;
import com.nopcommerce.pages.NavigationMenu;
import com.nopcommerce.testdata.providers.NavigationMenuDataProvider;

/**
 * NavigationMenuEnhancedTest - Enhanced data-driven tests using external test data
 * 
 * This test class demonstrates INDUSTRY BEST PRACTICES for data-driven testing:
 * 
 * ✅ Externalized Test Data: Data stored in JSON/CSV files, not hardcoded
 * ✅ Centralized DataProviders: Reusable data provider class
 * ✅ Type-Safe Models: POJO classes for test data
 * ✅ Separation of Concerns: Test logic separate from test data
 * ✅ Easy Maintenance: Non-technical users can modify JSON/CSV files
 * ✅ Version Control: Test data tracked separately from code
 * 
 * Advantages over previous implementation:
 * - Test data can be modified without recompiling code
 * - Same data can be shared across multiple test classes
 * - Easy to add/remove test scenarios (edit JSON/CSV)
 * - Business analysts can manage test data
 * - Supports data-driven from multiple sources (JSON, CSV, Excel, Database)
 * 
 * @author NopCommerce Automation Team
 * @version 2.0 - Enhanced with external data sources
 */
public class NavigationMenuEnhancedTest extends BaseTest {
    
    private NavigationMenu navigationMenu;
    
    /**
     * Test: All navigation menu scenarios using JSON data
     * 
     * This single test method handles ALL navigation scenarios (12+)
     * by reading test data from navigation-menu-data.json file.
     * 
     * @param mainMenu Main menu name
     * @param subMenu Submenu name (null if no submenu)
     * @param expectedUrl Expected URL fragment after navigation
     * @param description Test scenario description
     */
    @Test(dataProvider = "navigationMenuDataFromJson", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 1,
          description = "Comprehensive navigation test using JSON data")
    public void testNavigationFromJson(String mainMenu, String subMenu, 
                                       String expectedUrl, String description) {
        logger.info("=== Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        
        String navigationPath = subMenu == null ? mainMenu : mainMenu + " → " + subMenu;
        logger.info("Navigating to: " + navigationPath);
        
        navigationMenu.navigateTo(mainMenu, subMenu);
        
        // Verify navigation
        String currentUrl = driver.getCurrentUrl();
        logger.info("Current URL: " + currentUrl);
        
        Assert.assertTrue(currentUrl.contains(expectedUrl), 
            String.format("URL should contain '%s' for navigation '%s' but was: %s", 
                         expectedUrl, navigationPath, currentUrl));
        
        logger.info("✓ Successfully navigated to " + navigationPath);
    }
    
    /**
     * Test: All navigation menu scenarios using CSV data
     * 
     * Same test as above but reads data from CSV file.
     * Demonstrates flexibility to choose data source.
     * 
     * @param mainMenu Main menu name
     * @param subMenu Submenu name (null if no submenu)
     * @param expectedUrl Expected URL fragment
     * @param description Test scenario description
     */
    @Test(dataProvider = "navigationMenuDataFromCsv", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 2,
          enabled = false, // Disabled by default to avoid duplicate tests
          description = "Comprehensive navigation test using CSV data")
    public void testNavigationFromCsv(String mainMenu, String subMenu, 
                                      String expectedUrl, String description) {
        logger.info("=== Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        
        String navigationPath = subMenu == null ? mainMenu : mainMenu + " → " + subMenu;
        logger.info("Navigating to: " + navigationPath);
        
        navigationMenu.navigateTo(mainMenu, subMenu);
        
        // Verify navigation
        String currentUrl = driver.getCurrentUrl();
        logger.info("Current URL: " + currentUrl);
        
        Assert.assertTrue(currentUrl.contains(expectedUrl), 
            String.format("URL should contain '%s' for navigation '%s' but was: %s", 
                         expectedUrl, navigationPath, currentUrl));
        
        logger.info("✓ Successfully navigated to " + navigationPath);
    }
    
    /**
     * Test: Main menu navigation only
     * 
     * Tests only main menu items without submenus.
     * Data is filtered by DataProvider to include only main menus.
     * 
     * @param mainMenu Main menu name
     * @param subMenu Should be null for main menus
     * @param expectedUrl Expected URL fragment
     * @param description Test scenario description
     */
    @Test(dataProvider = "mainMenuOnlyData", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 3,
          description = "Test main menu navigation only")
    public void testMainMenuNavigation(String mainMenu, String subMenu, 
                                       String expectedUrl, String description) {
        logger.info("=== Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        
        logger.info("Navigating to main menu: " + mainMenu);
        navigationMenu.navigateTo(mainMenu, null);
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(expectedUrl), 
            "URL should contain '" + expectedUrl + "' but was: " + currentUrl);
        
        logger.info("✓ Main menu navigation successful");
    }
    
    /**
     * Test: Submenu navigation only
     * 
     * Tests only submenu items (items with parent menus).
     * Data is filtered by DataProvider to include only submenus.
     * 
     * @param mainMenu Main menu name
     * @param subMenu Submenu name
     * @param expectedUrl Expected URL fragment
     * @param description Test scenario description
     */
    @Test(dataProvider = "submenuOnlyData", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 4,
          description = "Test submenu navigation only")
    public void testSubmenuNavigation(String mainMenu, String subMenu, 
                                      String expectedUrl, String description) {
        logger.info("=== Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        
        logger.info("Navigating to: " + mainMenu + " → " + subMenu);
        navigationMenu.navigateTo(mainMenu, subMenu);
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(expectedUrl), 
            "URL should contain '" + expectedUrl + "' but was: " + currentUrl);
        
        logger.info("✓ Submenu navigation successful");
    }
    
    /**
     * Test: Computers category submenus
     * 
     * Tests only Computers submenu items (Desktops, Notebooks, Software).
     * 
     * @param mainMenu Should be "Computers"
     * @param subMenu Computers submenu name
     * @param expectedUrl Expected URL fragment
     * @param description Test scenario description
     */
    @Test(dataProvider = "computersSubmenuData", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 5,
          description = "Test Computers submenu navigation")
    public void testComputersSubmenu(String mainMenu, String subMenu, 
                                     String expectedUrl, String description) {
        logger.info("=== Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        navigationMenu.navigateTo(mainMenu, subMenu);
        
        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl), 
            "URL should contain '" + expectedUrl + "'");
        
        logger.info("✓ Computers submenu navigation successful");
    }
    
    /**
     * Test: Electronics category submenus
     * 
     * Tests only Electronics submenu items (Camera & photo, Cell phones).
     * 
     * @param mainMenu Should be "Electronics"
     * @param subMenu Electronics submenu name
     * @param expectedUrl Expected URL fragment
     * @param description Test scenario description
     */
    @Test(dataProvider = "electronicsSubmenuData", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 6,
          description = "Test Electronics submenu navigation")
    public void testElectronicsSubmenu(String mainMenu, String subMenu, 
                                       String expectedUrl, String description) {
        logger.info("=== Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        navigationMenu.navigateTo(mainMenu, subMenu);
        
        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl), 
            "URL should contain '" + expectedUrl + "'");
        
        logger.info("✓ Electronics submenu navigation successful");
    }
    
    /**
     * Test: Apparel category submenus
     * 
     * Tests only Apparel submenu items (Shoes, Clothing, Accessories).
     * 
     * @param mainMenu Should be "Apparel"
     * @param subMenu Apparel submenu name
     * @param expectedUrl Expected URL fragment
     * @param description Test scenario description
     */
    @Test(dataProvider = "apparelSubmenuData", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 7,
          description = "Test Apparel submenu navigation")
    public void testApparelSubmenu(String mainMenu, String subMenu, 
                                   String expectedUrl, String description) {
        logger.info("=== Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        navigationMenu.navigateTo(mainMenu, subMenu);
        
        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl), 
            "URL should contain '" + expectedUrl + "'");
        
        logger.info("✓ Apparel submenu navigation successful");
    }
    
    /**
     * Test: Invalid menu navigation - Negative testing
     * 
     * Tests error handling for invalid menu navigation scenarios.
     * Data comes from navigation-menu-negative-data.json
     * 
     * @param mainMenu Main menu name (may be invalid)
     * @param subMenu Submenu name (may be invalid)
     * @param description Test scenario description
     */
    @Test(dataProvider = "invalidNavigationMenuData", 
          dataProviderClass = NavigationMenuDataProvider.class,
          priority = 8,
          expectedExceptions = {RuntimeException.class, Exception.class},
          description = "Negative test for invalid menu navigation")
    public void testInvalidMenuNavigation(String mainMenu, String subMenu, String description) {
        logger.info("=== Negative Test: " + description + " ===");
        
        navigationMenu = new NavigationMenu(driver);
        
        String navigationPath = subMenu == null ? mainMenu : mainMenu + " → " + subMenu;
        logger.info("Attempting invalid navigation: " + navigationPath);
        
        // This should throw RuntimeException
        navigationMenu.navigateTo(mainMenu, subMenu);
        
        logger.error("Test should have thrown exception for: " + navigationPath);
    }
}
