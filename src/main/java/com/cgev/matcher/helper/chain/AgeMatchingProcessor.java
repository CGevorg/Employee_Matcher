package com.cgev.matcher.helper.chain;

import com.cgev.matcher.dto.Employee;

public class AgeMatchingProcessor extends MatchingProcessor {
    @Override
    public MatchingResult match(Employee e1, Employee e2) {
        MatchingResult next = next(e1, e2);
        if(next.getMatchingState().equals(MatchingResult.MatchingState.PERCENT)) {
            next.setScore(next.getScore() + (Math.abs(e1.getAge() - e2.getAge()) <= 5 ? 30 : 0));
        }
        return next;
    }
}
