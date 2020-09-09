package com.cgev.matcher.helper.validator;

import com.cgev.matcher.dto.Employee;

public class AgeValidator extends MatchingValidator {

    private static volatile  AgeValidator validator;

    private AgeValidator() {
        super(30);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer validateMatching(Employee e1, Employee e2) {
        return Math.abs(e1.getAge() - e2.getAge()) <= 5 ? getMaxPercentOfMatching() : 0;
    }

    /**
     * Retrieves singleton instance of a validator.
     * @return instance of validator.
     */
    public static AgeValidator getInstance() {
        if(validator == null) {
            synchronized(AgeValidator.class) {
                if (validator == null)
                    validator = new AgeValidator();
            }
        }
        return validator;
    }
}
