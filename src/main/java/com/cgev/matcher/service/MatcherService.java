package com.cgev.matcher.service;

import com.cgev.matcher.dto.Employee;
import com.cgev.matcher.dto.Result;
import com.cgev.matcher.exception.CouldNotParseFileException;
import com.cgev.matcher.exception.NothingToMatchException;
import com.cgev.matcher.helper.chain.AgeMatchingProcessor;
import com.cgev.matcher.helper.chain.DivisionMatchingProcessor;
import com.cgev.matcher.helper.chain.LocationMatchingProcessor;
import com.cgev.matcher.helper.chain.MatchingProcessor;
import com.cgev.matcher.helper.chain.MatchingResult;
import static com.cgev.matcher.helper.chain.MatchingResult.MatchingState;
import com.cgev.matcher.helper.chain.TimezoneMatchingProcessor;
import com.cgev.matcher.helper.converter.CsvFileConverter;
import com.cgev.matcher.helper.converter.EmployeeConverter;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
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
    public Result matchEmployees(MultipartFile file) {
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
    private Result matchEmployees(List<Employee> employees) {
        SimpleWeightedGraph<Employee, DefaultWeightedEdge> graph = generateWeightedGraph(employees);
        MatchingAlgorithm<Employee, DefaultWeightedEdge> algorithm = getMaxWeightMatchingAlgorithm(graph);
        MatchingAlgorithm.Matching<Employee, DefaultWeightedEdge> matching = algorithm.getMatching();
        List<List<? extends Serializable>> collect = matching.getEdges()
                .stream()
                .map(edge ->
                        Arrays.asList(
                                graph.getEdgeSource(edge).getName(),
                                graph.getEdgeTarget(edge).getName(),
                                graph.getEdgeWeight(edge)
                        )
                ).collect(Collectors.toList());
        Result result = new Result();
        result.setListOfMatches(collect);
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

    public MatchingProcessor makeMatchingChain() {
        MatchingProcessor chain1 = new LocationMatchingProcessor();
        MatchingProcessor chain2 = new DivisionMatchingProcessor();
        MatchingProcessor chain3 = new AgeMatchingProcessor();
        MatchingProcessor chain4 = new TimezoneMatchingProcessor();
        chain1.setSuccessor(chain2);
        chain2.setSuccessor(chain3);
        chain3.setSuccessor(chain4);
        return chain1;
    }
    /**
     * Determine edge weight of two vertices
     *
     * @param e1 first vertex
     * @param e2 second vertex
     * @return the weight of edge
     */
    private int determineWeightOfEdge(Employee e1, Employee e2) {
        MatchingProcessor matchingProcessor = makeMatchingChain();
        MatchingResult match = matchingProcessor.match(e1, e2);
        if(match.getMatchingState() == MatchingState.NOT_MATCHED) {
            return 0;
        } else if(match.getMatchingState() == MatchingState.MATCHED) {
            return 100;
        } else {
            return match.getScore();
        }
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