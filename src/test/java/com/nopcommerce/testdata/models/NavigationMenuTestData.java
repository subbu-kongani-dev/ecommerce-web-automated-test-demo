package com.nopcommerce.testdata.models;

/**
 * NavigationMenuTestData - Model class for Navigation Menu test data
 * 
 * This POJO (Plain Old Java Object) represents a single test data record
 * for navigation menu testing. Used to deserialize JSON/CSV test data.
 * 
 * Design Pattern: Data Transfer Object (DTO)
 * 
 * Benefits:
 * - Type-safe test data representation
 * - Easy to serialize/deserialize from JSON/CSV
 * - Clear data structure for test scenarios
 * - Reusable across multiple test classes
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class NavigationMenuTestData {
    
    private String mainMenu;
    private String subMenu;
    private String expectedUrl;
    private String description;
    
    /**
     * Default constructor required for JSON deserialization
     */
    public NavigationMenuTestData() {
    }
    
    /**
     * Constructor with all fields
     * 
     * @param mainMenu Main menu name
     * @param subMenu Submenu name (can be null)
     * @param expectedUrl Expected URL fragment after navigation
     * @param description Test scenario description
     */
    public NavigationMenuTestData(String mainMenu, String subMenu, String expectedUrl, String description) {
        this.mainMenu = mainMenu;
        this.subMenu = subMenu;
        this.expectedUrl = expectedUrl;
        this.description = description;
    }
    
    // Getters and Setters
    
    public String getMainMenu() {
        return mainMenu;
    }
    
    public void setMainMenu(String mainMenu) {
        this.mainMenu = mainMenu;
    }
    
    public String getSubMenu() {
        return subMenu;
    }
    
    public void setSubMenu(String subMenu) {
        this.subMenu = subMenu;
    }
    
    public String getExpectedUrl() {
        return expectedUrl;
    }
    
    public void setExpectedUrl(String expectedUrl) {
        this.expectedUrl = expectedUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Converts test data to Object array format required by TestNG DataProvider
     * 
     * @return Object[] array with test data in order: mainMenu, subMenu, expectedUrl, description
     */
    public Object[] toObjectArray() {
        return new Object[] { mainMenu, subMenu, expectedUrl, description };
    }
    
    /**
     * Returns navigation path for logging
     * 
     * @return Formatted navigation path (e.g., "Books" or "Computers → Desktops")
     */
    public String getNavigationPath() {
        return subMenu == null || subMenu.isEmpty() ? mainMenu : mainMenu + " → " + subMenu;
    }
    
    @Override
    public String toString() {
        return "NavigationMenuTestData{" +
                "mainMenu='" + mainMenu + '\'' +
                ", subMenu='" + subMenu + '\'' +
                ", expectedUrl='" + expectedUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
