package com.cgev.matcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MatchingResult {
    @JsonProperty("avgScore")
    private double avgScore;

    @JsonProperty("listOfMatches")
    private List<List<? extends Serializable>> listOfMatches = new ArrayList<>();

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public List<List<? extends Serializable>> getListOfMatches() {
        return listOfMatches;
    }

    public void setListOfMatches(List<List<? extends Serializable>> listOfMatches) {
        this.listOfMatches = listOfMatches;
    }
}
