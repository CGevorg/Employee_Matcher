package com.cgev.matcher.helper.converter;

import com.cgev.matcher.dto.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeConverter {
    /**
     * Extracting employees list from .csv file
     *
     * @param file from where we are going to extract the data
     * @return collection of employees
     */
    List<Employee> convert(MultipartFile file);
}
