package com.cgev.matcher.helper.chain;

import com.cgev.matcher.dto.Employee;

public abstract class MatchingProcessor {
    private MatchingProcessor successor;

    public abstract MatchingResult match(Employee e1, Employee e2);

    public void setSuccessor(MatchingProcessor successor) {
        this.successor = successor;
    }

    public MatchingResult next(Employee e1, Employee e2) {
        if (successor != null) {
           return successor.match(e1, e2);
        }
        return new MatchingResult(MatchingResult.MatchingState.PERCENT, 0);
    }
}