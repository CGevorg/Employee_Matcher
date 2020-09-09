package com.cgev.matcher.helper.validator;

import com.cgev.matcher.dto.Employee;

public class DivisionValidator extends MatchingValidator {

    private static volatile  DivisionValidator validator;

    private DivisionValidator() {
        super(30);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer validateMatching(Employee e1, Employee e2) {
        return e1.getDivision().equals(e2.getDivision()) ? getMaxPercentOfMatching() : 0;
    }

    /**
     * Retrieves singleton instance of a validator.
     * @return instance of validator.
     */
    public static DivisionValidator getInstance() {
        if(validator == null) {
            synchronized(DivisionValidator.class) {
                if (validator == null)
                    validator = new DivisionValidator();
            }
        }
        return validator;
    }
}
