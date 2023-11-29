package kr.ac.kumoh.illdang100.tovalley.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.YearMonth;

public class YearMonthRangeValidator implements ConstraintValidator<YearMonthRange, YearMonth> {

    @Override
    public void initialize(YearMonthRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(YearMonth value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        int currentYear = YearMonth.now().getYear();
        int inputYear = value.getYear();

        return inputYear >= (currentYear - 2) && inputYear <= currentYear;
    }
}

