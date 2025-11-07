package com.nopcommerce.testdata.readers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvDataReader - Utility class for reading test data from CSV files
 * 
 * This class provides methods to read and parse CSV test data files.
 * Uses OpenCSV library for CSV parsing.
 * 
 * Design Pattern: Utility/Helper Pattern
 * 
 * Features:
 * - Read from classpath resources
 * - Skip header row automatically
 * - Convert to Object[][] for TestNG DataProvider
 * - Error handling and logging
 * - Support for empty cells (null values)
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class CsvDataReader {
    
    private static final Logger logger = LogManager.getLogger(CsvDataReader.class);
    
    /**
     * Reads CSV data from classpath resource and returns as 2D Object array
     * 
     * @param resourcePath Path to CSV file in classpath (e.g., "testdata/navigation-menu-data.csv")
     * @param skipHeader Whether to skip the first row (header row)
     * @return Object[][] array suitable for TestNG DataProvider
     * @throws RuntimeException if file cannot be read or parsed
     */
    public static Object[][] readFromResource(String resourcePath, boolean skipHeader) {
        logger.info("Reading CSV data from resource: " + resourcePath);
        
        try (InputStream inputStream = CsvDataReader.class.getClassLoader().getResourceAsStream(resourcePath);
             InputStreamReader reader = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReader(reader)) {
            
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            
            List<String[]> allRows = csvReader.readAll();
            
            if (allRows.isEmpty()) {
                logger.warn("CSV file is empty: " + resourcePath);
                return new Object[0][0];
            }
            
            // Skip header if needed
            int startIndex = skipHeader ? 1 : 0;
            int dataRowCount = allRows.size() - startIndex;
            
            if (dataRowCount <= 0) {
                logger.warn("No data rows found in CSV: " + resourcePath);
                return new Object[0][0];
            }
            
            // Convert to Object[][]
            Object[][] data = new Object[dataRowCount][];
            for (int i = startIndex; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                // Convert empty strings to null
                Object[] objectRow = new Object[row.length];
                for (int j = 0; j < row.length; j++) {
                    objectRow[j] = row[j].isEmpty() ? null : row[j];
                }
                data[i - startIndex] = objectRow;
            }
            
            logger.info("Successfully read " + data.length + " data rows from " + resourcePath);
            return data;
            
        } catch (IOException e) {
            logger.error("Failed to read CSV data from resource: " + resourcePath, e);
            throw new RuntimeException("Failed to read CSV data from: " + resourcePath, e);
        } catch (Exception e) {
            logger.error("Failed to parse CSV data from resource: " + resourcePath, e);
            throw new RuntimeException("Failed to parse CSV data from: " + resourcePath, e);
        }
    }
    
    /**
     * Reads CSV data with header row skipped (most common use case)
     * 
     * @param resourcePath Path to CSV file in classpath
     * @return Object[][] array suitable for TestNG DataProvider
     */
    public static Object[][] readFromResource(String resourcePath) {
        return readFromResource(resourcePath, true);
    }
}
