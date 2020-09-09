package com.cgev.matcher.helper.converter;

import com.cgev.matcher.dto.Employee;
import com.cgev.matcher.exception.CSVParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvFileConverter implements EmployeeConverter {
    private final Logger logger = LoggerFactory.getLogger(CsvFileConverter.class);

    /**
     * Extracting employees list from .csv file
     *
     * @param file from where we are going to extract the data
     * @return collection of employees
     */
    @Override
    public List<Employee> convert(MultipartFile file) {
        logger.info("File processing started");
        try (InputStream inputStream = file.getInputStream();
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<Employee> employees = new ArrayList<>();
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            List<CSVRecord> records = csvParser.getRecords();
            for (CSVRecord csvRecord : records.subList(1, records.size())) {
                Employee employee = new Employee();
                employee.setName(csvRecord.get(0));
                employee.setEmail(csvRecord.get(1));
                employee.setDivision(csvRecord.get(2));
                employee.setAge(Integer.parseInt(csvRecord.get(3)));
                employee.setUtcOffset(Integer.parseInt(csvRecord.get(4)));
                employees.add(employee);
            }
            logger.info("File processing finished successfully");
            return employees;
        } catch (IOException e) {
            throw new CSVParseException();
        }

    }
}
