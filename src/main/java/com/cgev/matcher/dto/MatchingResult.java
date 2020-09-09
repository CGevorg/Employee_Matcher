package com.cgev.matcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class MatchingResult {
    @JsonProperty("avgScore")
    private double avgScore;

    @JsonProperty("listOfMatches")
    private Set<Triplet<String, String, Double>> listOfMatches = new HashSet<>();

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public Set<Triplet<String, String, Double>> getListOfMatches() {
        return listOfMatches;
    }

    public void setListOfMatches(Set<Triplet<String, String, Double>> listOfMatches) {
        this.listOfMatches = listOfMatches;
    }
}
