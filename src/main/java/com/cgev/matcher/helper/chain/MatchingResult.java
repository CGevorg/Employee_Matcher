package com.cgev.matcher.helper.chain;

public class MatchingResult {
    public MatchingState getMatchingState() {
        return matchingState;
    }

    public void setMatchingState(MatchingState matchingState) {
        this.matchingState = matchingState;
    }

    public enum MatchingState {
        MATCHED,
        NOT_MATCHED,
        PERCENT
    }
    private MatchingState matchingState;
    private int score;

    MatchingResult() {

    }

    MatchingResult(MatchingState matchingState) {
        this.matchingState = matchingState;
    }

    MatchingResult(MatchingState matchingState, int score) {
        this.matchingState = matchingState;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}