package com.eventmanagement.validation;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.stream.IntStream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates Estonian personal identification codes.
 *
 * <p>Format: GYYMMDDXXXC (11 digits)
 * <ul>
 *   <li>G — Gender/century (1-2: 1800s, 3-4: 1900s, 5-6: 2000s)</li>
 *   <li>YYMMDD — Date of birth</li>
 *   <li>XXX — Birth order number (001-999)</li>
 *   <li>C — Checksum digit</li>
 * </ul>
 *
 */
public class PersonalCodeValidator implements ConstraintValidator<ValidPersonalCode, String> {

    private static final int CODE_LENGTH = 11;
    private static final int[] WEIGHTS_1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
    private static final int[] WEIGHTS_2 = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return isCorrectFormat(value)
                && isValidGenderDigit(value)
                && isValidDate(value)
                && isValidChecksum(value);
    }

    private boolean isCorrectFormat(String code) {
        return code.length() == CODE_LENGTH
                && code.chars().allMatch(Character::isDigit);
    }

    private boolean isValidGenderDigit(String code) {
        int gender = Character.getNumericValue(code.charAt(0));
        return gender >= 1 && gender <= 6;
    }

    private boolean isValidDate(String code) {
        try {
            int century = extractCentury(Character.getNumericValue(code.charAt(0)));
            int year = century + Integer.parseInt(code.substring(1, 3));
            int month = Integer.parseInt(code.substring(3, 5));
            int day = Integer.parseInt(code.substring(5, 7));
            LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException | NumberFormatException e) {
            return false;
        }
    }

    private int extractCentury(int genderDigit) {
        return switch (genderDigit) {
            case 1, 2 -> 1800;
            case 3, 4 -> 1900;
            case 5, 6 -> 2000;
            default -> throw new IllegalArgumentException("Invalid gender digit: %d".formatted(genderDigit));
        };
    }

    /**
     * Checksum is computed in two rounds using weighted modular arithmetic.
     * If round 1 yields remainder 10, round 2 is applied.
     * If round 2 also yields 10, the checksum digit is 0.
     */
    private boolean isValidChecksum(String code) {
        int expected = Character.getNumericValue(code.charAt(10));
        int checksum = computeWeightedSum(code, WEIGHTS_1) % 11;

        if (checksum == 10) {
            checksum = computeWeightedSum(code, WEIGHTS_2) % 11;
            checksum = (checksum == 10) ? 0 : checksum;
        }

        return checksum == expected;
    }

    private int computeWeightedSum(String code, int[] weights) {
        return IntStream.range(0, 10)
                .map(i -> Character.getNumericValue(code.charAt(i)) * weights[i])
                .sum();
    }
}
