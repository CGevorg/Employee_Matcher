package com.cgev.matcher.service;

import com.cgev.matcher.dto.Employee;
import com.cgev.matcher.dto.MatchingResult;
import com.cgev.matcher.dto.Triplet;
import com.cgev.matcher.exception.CouldNotParseFileException;
import com.cgev.matcher.exception.NothingToMatchException;
import com.cgev.matcher.helper.converter.CsvFileConverter;
import com.cgev.matcher.helper.converter.EmployeeConverter;
import com.cgev.matcher.helper.validator.AgeValidator;
import com.cgev.matcher.helper.validator.DivisionValidator;
import com.cgev.matcher.helper.validator.MatchingValidator;
import com.cgev.matcher.helper.validator.TimezoneValidator;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatcherService {
    private final Logger logger = LoggerFactory.getLogger(MatcherService.class);

    /**
     * Extracting employees from given file and matching them with each other.
     *
     * @param file from where we are going to extract input data
     * @return the result which is containing information about matching
     */
    public MatchingResult matchEmployees(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        logger.info("Processing the file: " + fileName);
        String fileExtenstion = fileName.substring(fileName.lastIndexOf("."));
        EmployeeConverter converter;
        if (".csv".equals(fileExtenstion)) {
            converter = new CsvFileConverter();
        } else {
            throw new CouldNotParseFileException(fileExtenstion);
        }
        return matchEmployees(converter.convert(file));
    }

    /**
     * Processing the list of employees
     *
     * @param employees list which we are going to process
     * @return the matching object which is containing matching results
     */
    private MatchingResult matchEmployees(List<Employee> employees) {
        SimpleWeightedGraph<Employee, DefaultWeightedEdge> graph = generateWeightedGraph(employees);
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
        result.setAvgScore(matching.getWeight()/ (employees.size() / 2));
        return result;
    }


    /**
     * Retrieves maximum weight matching algorithm
     *
     * @param graph for which we are going to choose an algorithm.
     * @return algorithm implementation object
     */
    private MatchingAlgorithm<Employee, DefaultWeightedEdge> getMaxWeightMatchingAlgorithm(SimpleWeightedGraph<Employee, DefaultWeightedEdge> graph) {
        return new KolmogorovWeightedMatching<>(graph);
    }

    /**
     * Determine edge weight of two vertices
     *
     * @param e1 first vertex
     * @param e2 second vertex
     * @return the weight of edge
     */
    private int determineWeightOfEdge(Employee e1, Employee e2) {
        List<MatchingValidator> validators = Arrays.asList(
                AgeValidator.getInstance(),
                DivisionValidator.getInstance(),
                TimezoneValidator.getInstance());
        int sum = validators.stream().mapToInt(validator -> validator.validateMatching(e1, e2)).sum();
        if(sum > 100) {
            sum = 100;
        }
        return sum;
    }

    /**
     * Generates weighted graph from given list of employees.
     *
     * @param employees From where we are going to generate a graph
     * @return Graph where each vertex is an employee
     */
    private SimpleWeightedGraph<Employee, DefaultWeightedEdge> generateWeightedGraph(List<Employee> employees) {
        if(employees.isEmpty()) {
            logger.error("Given list of employees are empty");
            throw new NothingToMatchException();
        }
        logger.info("Creating simple graph with given list of employees");
        SimpleWeightedGraph<Employee, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        logger.debug("Adding vertices to the graph");
        employees.forEach(graph::addVertex);

        for (int i = 0; i < employees.size() - 1; i++) {
            Employee employee1 = employees.get(i);
            for (int j = i + 1; j < employees.size(); j++) {
                Employee employee2 = employees.get(j);
                int weight = determineWeightOfEdge(employee1, employee2);
                DefaultWeightedEdge edge = graph.addEdge(employee1, employee2);
                graph.setEdgeWeight(edge, weight);
            }
        }
        logger.debug("Adding vertex supplier for a graph");
        graph.setVertexSupplier(Employee::new);
        logger.info("Graph created successfully");
        return graph;
    }
}