package com.cgev.matcher.service;

import com.cgev.matcher.exception.CouldNotParseFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    /**
     * Processing given multipart file.
     * @param file which are we going to process
     */
    public void processEmployees(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        logger.info("Processing file: " + fileName);
        String fileExtenstion = fileName.substring(fileName.lastIndexOf("."));
        if(".csv".equals(fileExtenstion)) {
            processCSVFile(file);
        } else {
            throw new CouldNotParseFileException(fileExtenstion);
        }
    }

    /**
     * Processing given multipart file.
     * @param file which are we going to process
     */
    public void processCSVFile(MultipartFile file) {
        logger.info("File processing started");
        logger.info("File processing finished successfully");
    }
}
