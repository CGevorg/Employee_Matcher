package com.cgev.matcher.helper.validator;

import com.cgev.matcher.dto.Employee;

public class TimezoneValidator extends MatchingValidator {

    private static volatile  TimezoneValidator validator;

    private TimezoneValidator() {
        super(40);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer validateMatching(Employee e1, Employee e2) {
        return e1.getUtcOffset().equals(e2.getUtcOffset()) ? getMaxPercentOfMatching() : 0;
    }

    /**
     * Retrieves singleton instance of a validator.
     * @return instance of validator.
     */
    public static TimezoneValidator getInstance() {
        if(validator == null) {
            synchronized(TimezoneValidator.class) {
                if (validator == null)
                    validator = new TimezoneValidator();
            }
        }
        return validator;
    }
}
