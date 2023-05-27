package com.rest.todo.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final Pattern phoneNumberPattern = Pattern.compile("^\\+[0-9]{1,3}[0-9]{8,10}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null ) return false;

        Matcher matcher = phoneNumberPattern.matcher(value);
        return matcher.matches();
    }
}
