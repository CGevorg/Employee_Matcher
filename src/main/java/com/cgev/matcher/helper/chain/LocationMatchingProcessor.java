package com.cgev.matcher.helper.chain;

import com.cgev.matcher.dto.Employee;

public class LocationMatchingProcessor extends MatchingProcessor {

    @Override
    public MatchingResult match(Employee e1, Employee e2) {
        if (e1.getSameLocationPreference().equalsIgnoreCase("YES")
                || e2.getSameLocationPreference().equalsIgnoreCase("YES")) {
            if (!e1.getLocation().equalsIgnoreCase(e2.getLocation())) {
                return new MatchingResult(MatchingResult.MatchingState.NOT_MATCHED, 0);
            }
        } else if (e1.getSameLocationPreference().equalsIgnoreCase("NO")
                || e2.getSameLocationPreference().equalsIgnoreCase("NO")) {
            if (e1.getLocation().equalsIgnoreCase(e2.getLocation())) {
                return new MatchingResult(MatchingResult.MatchingState.NOT_MATCHED, 0);
            }
        }
        return next(e1, e2);
    }
}
