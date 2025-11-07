package com.nopcommerce.testdata.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nopcommerce.testdata.models.NavigationMenuTestData;
import com.nopcommerce.testdata.readers.CsvDataReader;
import com.nopcommerce.testdata.readers.JsonDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NavigationMenuDataProvider - Centralized DataProvider class for Navigation Menu tests
 * 
 * This class serves as a centralized repository for all TestNG DataProviders
 * related to navigation menu testing. It reads test data from external files
 * (JSON/CSV) instead of hardcoding data in test classes.
 * 
 * Design Pattern: Factory Pattern for Test Data
 * 
 * Benefits:
 * - Separation of test data from test logic
 * - Easy to maintain and update test data
 * - Support for multiple data formats (JSON, CSV)
 * - Reusable across multiple test classes
 * - Non-technical users can modify test data
 * - Version control friendly (data in separate files)
 * 
 * Industry Best Practices:
 * - Externalized test data (JSON/CSV files)
 * - Type-safe data models (POJOs)
 * - Centralized data provider methods
 * - Comprehensive logging
 * - Error handling
 * 
 * @author NopCommerce Automation Team
 * @version 2.0 - Enhanced with external data sources
 */
public class NavigationMenuDataProvider {
    
    private static final Logger logger = LogManager.getLogger(NavigationMenuDataProvider.class);
    
    // File paths for test data
    private static final String NAVIGATION_MENU_JSON = "testdata/navigation-menu-data.json";
    private static final String NAVIGATION_MENU_CSV = "testdata/navigation-menu-data.csv";
    private static final String NEGATIVE_TEST_JSON = "testdata/navigation-menu-negative-data.json";
    
    /**
     * DataProvider: All navigation menu data from JSON file
     * 
     * Reads comprehensive test data from navigation-menu-data.json
     * including both main menus and submenus.
     * 
     * @return Object[][] array with format: {mainMenu, subMenu, expectedUrl, description}
     */
    @DataProvider(name = "navigationMenuDataFromJson")
    public static Object[][] getNavigationMenuDataFromJson() {
        logger.info("Loading navigation menu test data from JSON");
        
        try {
            TypeReference<List<NavigationMenuTestData>> typeRef = 
                new TypeReference<List<NavigationMenuTestData>>() {};
            
            return JsonDataReader.readAsDataProvider(
                NAVIGATION_MENU_JSON, 
                typeRef, 
                NavigationMenuTestData::toObjectArray
            );
            
        } catch (Exception e) {
            logger.error("Failed to load navigation menu data from JSON", e);
            throw new RuntimeException("Failed to load test data from JSON", e);
        }
    }
    
    /**
     * DataProvider: All navigation menu data from CSV file
     * 
     * Reads test data from navigation-menu-data.csv.
     * CSV format: mainMenu,subMenu,expectedUrl,description
     * 
     * @return Object[][] array with format: {mainMenu, subMenu, expectedUrl, description}
     */
    @DataProvider(name = "navigationMenuDataFromCsv")
    public static Object[][] getNavigationMenuDataFromCsv() {
        logger.info("Loading navigation menu test data from CSV");
        
        try {
            return CsvDataReader.readFromResource(NAVIGATION_MENU_CSV);
            
        } catch (Exception e) {
            logger.error("Failed to load navigation menu data from CSV", e);
            throw new RuntimeException("Failed to load test data from CSV", e);
        }
    }
    
    /**
     * DataProvider: Invalid navigation menu data for negative testing
     * 
     * Reads negative test scenarios from navigation-menu-negative-data.json
     * 
     * @return Object[][] array with format: {mainMenu, subMenu, description}
     */
    @DataProvider(name = "invalidNavigationMenuData")
    public static Object[][] getInvalidNavigationMenuData() {
        logger.info("Loading invalid navigation menu test data from JSON");
        
        try {
            List<NavigationMenuTestData> dataList = JsonDataReader.readFromResource(
                NEGATIVE_TEST_JSON,
                new TypeReference<List<NavigationMenuTestData>>() {}
            );
            
            Object[][] data = new Object[dataList.size()][];
            for (int i = 0; i < dataList.size(); i++) {
                NavigationMenuTestData testData = dataList.get(i);
                // For negative tests, we don't need expectedUrl
                data[i] = new Object[] { 
                    testData.getMainMenu(), 
                    testData.getSubMenu(), 
                    testData.getDescription() 
                };
            }
            
            logger.info("Loaded " + data.length + " negative test scenarios");
            return data;
            
        } catch (Exception e) {
            logger.error("Failed to load invalid navigation menu data from JSON", e);
            throw new RuntimeException("Failed to load negative test data from JSON", e);
        }
    }
    
    /**
     * DataProvider: Main menus only (no submenus) from JSON
     * 
     * Filters test data to return only main menu items without submenus.
     * Useful for testing direct main menu navigation.
     * 
     * @return Object[][] array with format: {mainMenu, subMenu, expectedUrl, description}
     */
    @DataProvider(name = "mainMenuOnlyData")
    public static Object[][] getMainMenuOnlyData() {
        logger.info("Loading main menu only test data from JSON");
        
        try {
            TypeReference<List<NavigationMenuTestData>> typeRef = 
                new TypeReference<List<NavigationMenuTestData>>() {};
            
            List<NavigationMenuTestData> allData = JsonDataReader.readFromResource(NAVIGATION_MENU_JSON, typeRef);
            
            // Filter for main menus only (subMenu is null or empty)
            List<NavigationMenuTestData> mainMenuData = allData.stream()
                .filter(data -> data.getSubMenu() == null || data.getSubMenu().isEmpty())
                .collect(Collectors.toList());
            
            Object[][] data = new Object[mainMenuData.size()][];
            for (int i = 0; i < mainMenuData.size(); i++) {
                data[i] = mainMenuData.get(i).toObjectArray();
            }
            
            logger.info("Filtered " + data.length + " main menu only records");
            return data;
            
        } catch (Exception e) {
            logger.error("Failed to load main menu only data", e);
            throw new RuntimeException("Failed to load main menu data", e);
        }
    }
    
    /**
     * DataProvider: Submenu items only from JSON
     * 
     * Filters test data to return only submenu navigation items.
     * Useful for testing dropdown submenu navigation.
     * 
     * @return Object[][] array with format: {mainMenu, subMenu, expectedUrl, description}
     */
    @DataProvider(name = "submenuOnlyData")
    public static Object[][] getSubmenuOnlyData() {
        logger.info("Loading submenu only test data from JSON");
        
        try {
            TypeReference<List<NavigationMenuTestData>> typeRef = 
                new TypeReference<List<NavigationMenuTestData>>() {};
            
            List<NavigationMenuTestData> allData = JsonDataReader.readFromResource(NAVIGATION_MENU_JSON, typeRef);
            
            // Filter for submenus only (subMenu is not null and not empty)
            List<NavigationMenuTestData> submenuData = allData.stream()
                .filter(data -> data.getSubMenu() != null && !data.getSubMenu().isEmpty())
                .collect(Collectors.toList());
            
            Object[][] data = new Object[submenuData.size()][];
            for (int i = 0; i < submenuData.size(); i++) {
                data[i] = submenuData.get(i).toObjectArray();
            }
            
            logger.info("Filtered " + data.length + " submenu only records");
            return data;
            
        } catch (Exception e) {
            logger.error("Failed to load submenu only data", e);
            throw new RuntimeException("Failed to load submenu data", e);
        }
    }
    
    /**
     * DataProvider: Computers category submenu data
     * 
     * Returns only Computers submenu test data.
     * 
     * @return Object[][] array with Computers submenu data
     */
    @DataProvider(name = "computersSubmenuData")
    public static Object[][] getComputersSubmenuData() {
        return getSubmenuDataByCategory("Computers");
    }
    
    /**
     * DataProvider: Electronics category submenu data
     * 
     * Returns only Electronics submenu test data.
     * 
     * @return Object[][] array with Electronics submenu data
     */
    @DataProvider(name = "electronicsSubmenuData")
    public static Object[][] getElectronicsSubmenuData() {
        return getSubmenuDataByCategory("Electronics");
    }
    
    /**
     * DataProvider: Apparel category submenu data
     * 
     * Returns only Apparel submenu test data.
     * 
     * @return Object[][] array with Apparel submenu data
     */
    @DataProvider(name = "apparelSubmenuData")
    public static Object[][] getApparelSubmenuData() {
        return getSubmenuDataByCategory("Apparel");
    }
    
    /**
     * Helper method to filter submenu data by category
     * 
     * @param category Main menu category name
     * @return Object[][] array with filtered submenu data
     */
    private static Object[][] getSubmenuDataByCategory(String category) {
        logger.info("Loading " + category + " submenu test data");
        
        try {
            TypeReference<List<NavigationMenuTestData>> typeRef = 
                new TypeReference<List<NavigationMenuTestData>>() {};
            
            List<NavigationMenuTestData> allData = JsonDataReader.readFromResource(NAVIGATION_MENU_JSON, typeRef);
            
            // Filter for specific category submenus
            List<NavigationMenuTestData> categoryData = allData.stream()
                .filter(data -> category.equals(data.getMainMenu()) && 
                               data.getSubMenu() != null && 
                               !data.getSubMenu().isEmpty())
                .collect(Collectors.toList());
            
            Object[][] data = new Object[categoryData.size()][];
            for (int i = 0; i < categoryData.size(); i++) {
                data[i] = categoryData.get(i).toObjectArray();
            }
            
            logger.info("Filtered " + data.length + " " + category + " submenu records");
            return data;
            
        } catch (Exception e) {
            logger.error("Failed to load " + category + " submenu data", e);
            throw new RuntimeException("Failed to load " + category + " submenu data", e);
        }
    }
}
