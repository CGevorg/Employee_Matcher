package com.cgev.matcher.helper.chain;

import com.cgev.matcher.dto.Employee;

public class TimezoneMatchingProcessor extends MatchingProcessor {
    @Override
    public MatchingResult match(Employee e1, Employee e2) {
        MatchingResult next = next(e1, e2);
        if(next.getMatchingState().equals(MatchingResult.MatchingState.PERCENT)) {
            next.setScore(next.getScore() + (e1.getUtcOffset().equals(e2.getUtcOffset()) ? 40 : 0));
        }
        return next;
    }
}
