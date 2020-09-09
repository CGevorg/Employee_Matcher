package com.cgev.matcher.helper.validator;

import com.cgev.matcher.dto.Employee;

public abstract class MatchingValidator {

    private final Integer maxPercentOfMatching;

    MatchingValidator(Integer maxPercentOfMatching) {
        this.maxPercentOfMatching = maxPercentOfMatching;
    }
    /**
     * Validating employees matching.
     *
     * @param e1 first employee
     * @param e2 second employee
     * @return The percent of validation from zero to maxPercentOfMatching.
     */
    public abstract Integer validateMatching(Employee e1, Employee e2);

    public Integer getMaxPercentOfMatching() {
        return maxPercentOfMatching;
    }
}
