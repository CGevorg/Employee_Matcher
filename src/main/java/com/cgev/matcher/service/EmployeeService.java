package com.cgev.matcher.service;

import com.cgev.matcher.dto.Employee;
import com.cgev.matcher.dto.MatchingResult;
import com.cgev.matcher.dto.Triplet;
import com.cgev.matcher.exception.CSVParseException;
import com.cgev.matcher.exception.CouldNotParseFileException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    /**
     * Processing given multipart file.
     *
     * @param file which are we going to process
     */
    public MatchingResult processEmployees(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        logger.info("Processing the file: " + fileName);
        String fileExtenstion = fileName.substring(fileName.lastIndexOf("."));
        List<Employee> employees;
        if (".csv".equals(fileExtenstion)) {
            employees = cvsToEmployeesConverter(file);
        } else {
            throw new CouldNotParseFileException(fileExtenstion);
        }
        return matchingEmployees(employees);
    }


    /**
     * Extracting employees list from .csv file
     *
     * @param file from where we are going to extract the data
     * @return collection of employees
     */
    public List<Employee> cvsToEmployeesConverter(MultipartFile file) {
        logger.info("File processing started");
        try (InputStream inputStream = file.getInputStream();
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            List<Employee> employees = new ArrayList<>();
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            List<CSVRecord> records = csvParser.getRecords();
            for (CSVRecord csvRecord : records.subList(1, records.size())) {
                Employee employee = new Employee();
                employee.setName(csvRecord.get(0));
                employee.setEmail(csvRecord.get(1));
                employee.setDivision(csvRecord.get(2));
                employee.setAge(Integer.parseInt(csvRecord.get(3)));
                employee.setUtc_offset(Integer.parseInt(csvRecord.get(4)));
                employees.add(employee);
            }
            logger.info("File processing finished successfully");
            return employees;
        } catch (IOException e) {
            throw new CSVParseException();
        }
    }

    /**
     * Processing the list of employees
     *
     * @param employees list which we are going to process
     * @return the matching object which is containing matching results
     */
    public MatchingResult matchingEmployees(List<Employee> employees) {
        SimpleWeightedGraph<Employee, DefaultWeightedEdge> graph = createSimpleGraph(employees);
        MatchingAlgorithm<Employee, DefaultWeightedEdge> algorithm = getMaxWeightMatchingAlgorithm(graph);
        MatchingAlgorithm.Matching<Employee, DefaultWeightedEdge> matching = algorithm.getMatching();
        Set<Triplet<String, String, Double>> listOfMatches = matching.getEdges()
                .stream()
                .map(edge ->
                        new Triplet<>(graph.getEdgeSource(edge).getName(),
                                graph.getEdgeTarget(edge).getName(),
                                graph.getEdgeWeight(edge))).collect(Collectors.toSet());
        MatchingResult result = new MatchingResult();
        result.setListOfMatches(listOfMatches);
        result.setAvgScore(matching.getWeight());
        return result;
    }


    /**
     * Retrieves matching algorithm
     *
     * @param graph for which we are going to choose an algorithm.
     * @return algorithm implementation object
     */
    public MatchingAlgorithm<Employee, DefaultWeightedEdge> getMaxWeightMatchingAlgorithm(SimpleWeightedGraph<Employee, DefaultWeightedEdge> graph) {
        return new KolmogorovWeightedMatching<>(graph);
    }


    public int getMaxResult(List<Employee> employees, Set<Employee> exclude_list, Map<Set<Employee>, Integer> cash) {
        if (cash.containsKey(exclude_list))
            return cash.get(exclude_list);
        int max = 0;
        for (int i = 0; i < employees.size() - 1; i++) {
            if (exclude_list.contains(employees.get(i)))
                continue;
            for (int j = i + 1; j < employees.size(); j++) {
                if (exclude_list.contains(employees.get(j))) {
                    continue;
                }
                HashSet<Employee> next_level = new HashSet<>(exclude_list);
                next_level.add(employees.get(i));
                next_level.add(employees.get(j));
                int result = findWeight(employees.get(i), employees.get(j)) + getMaxResult(employees, next_level, cash);
                if (max < result) {
                    max = result;
                }
            }
        }
        cash.put(exclude_list, max);
        return max;
    }

    public int findWeight(Employee e1, Employee e2) {
        int weight = 0;
        if (e1.getDivision().equals(e2.getDivision())) {
            weight += 30;
        }
        if (Math.abs(e1.getAge() - e2.getAge()) <= 5) {
            weight += 30;
        }
        if (e1.getUtc_offset().equals(e2.getUtc_offset())) {
            weight += 40;
        }
        return weight;
    }

    SimpleWeightedGraph<Employee, DefaultWeightedEdge> createSimpleGraph(List<Employee> employees) {
        if (employees.isEmpty()) {
            throw new RuntimeException("asdas");
        }
        SimpleWeightedGraph<Employee, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        employees.forEach(graph::addVertex);


        for (int i = 0; i < employees.size() - 1; i++) {
            Employee employee1 = employees.get(i);
            for (int j = i + 1; j < employees.size(); j++) {
                Employee employee2 = employees.get(j);
                int weight = findWeight(employee1, employee2);
                DefaultWeightedEdge edge = graph.addEdge(employee1, employee2);
                graph.setEdgeWeight(edge, weight);
            }
        }
        graph.setVertexSupplier(Employee::new);
        return graph;
    }
}