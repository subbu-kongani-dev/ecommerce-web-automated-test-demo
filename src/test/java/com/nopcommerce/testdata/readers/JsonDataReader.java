package com.nopcommerce.testdata.readers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonDataReader - Utility class for reading test data from JSON files
 * 
 * This class provides methods to read and parse JSON test data files into Java objects.
 * Uses Jackson library for JSON parsing.
 * 
 * Design Pattern: Utility/Helper Pattern
 * 
 * Features:
 * - Read from classpath resources (src/test/resources)
 * - Read from file system paths
 * - Type-safe deserialization using generics
 * - Error handling and logging
 * - Support for arrays and lists
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class JsonDataReader {
    
    private static final Logger logger = LogManager.getLogger(JsonDataReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Reads JSON data from classpath resource and converts to list of objects
     * 
     * @param <T> Type of objects in the list
     * @param resourcePath Path to JSON file in classpath (e.g., "testdata/navigation-menu-data.json")
     * @param typeReference TypeReference for deserialization
     * @return List of deserialized objects
     * @throws RuntimeException if file cannot be read or parsed
     */
    public static <T> List<T> readFromResource(String resourcePath, TypeReference<List<T>> typeReference) {
        logger.info("Reading JSON data from resource: " + resourcePath);
        
        try (InputStream inputStream = JsonDataReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            
            List<T> data = objectMapper.readValue(inputStream, typeReference);
            logger.info("Successfully read " + data.size() + " records from " + resourcePath);
            return data;
            
        } catch (IOException e) {
            logger.error("Failed to read JSON data from resource: " + resourcePath, e);
            throw new RuntimeException("Failed to read JSON data from: " + resourcePath, e);
        }
    }
    
    /**
     * Reads JSON data from file system path
     * 
     * @param <T> Type of objects in the list
     * @param filePath Absolute or relative file path
     * @param typeReference TypeReference for deserialization
     * @return List of deserialized objects
     * @throws RuntimeException if file cannot be read or parsed
     */
    public static <T> List<T> readFromFile(String filePath, TypeReference<List<T>> typeReference) {
        logger.info("Reading JSON data from file: " + filePath);
        
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }
            
            List<T> data = objectMapper.readValue(file, typeReference);
            logger.info("Successfully read " + data.size() + " records from " + filePath);
            return data;
            
        } catch (IOException e) {
            logger.error("Failed to read JSON data from file: " + filePath, e);
            throw new RuntimeException("Failed to read JSON data from: " + filePath, e);
        }
    }
    
    /**
     * Reads JSON array and converts to Object[][] format for TestNG DataProvider
     * 
     * @param <T> Type of objects in the list
     * @param resourcePath Path to JSON file in classpath
     * @param typeReference TypeReference for deserialization
     * @param converter Function to convert object to Object[] array
     * @return Object[][] array suitable for TestNG DataProvider
     */
    public static <T> Object[][] readAsDataProvider(String resourcePath, 
                                                     TypeReference<List<T>> typeReference,
                                                     DataConverter<T> converter) {
        List<T> dataList = readFromResource(resourcePath, typeReference);
        
        if (dataList == null || dataList.isEmpty()) {
            logger.warn("No test data found in: " + resourcePath);
            return new Object[0][0];
        }
        
        Object[][] dataArray = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = converter.convert(dataList.get(i));
        }
        
        logger.info("Converted " + dataArray.length + " records to DataProvider format");
        return dataArray;
    }
    
    /**
     * Functional interface for converting objects to Object[] array
     */
    @FunctionalInterface
    public interface DataConverter<T> {
        Object[] convert(T data);
    }
}
