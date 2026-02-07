package com.eventmanagement.validation;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PersonalCodeValidatorTest {

    private PersonalCodeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PersonalCodeValidator();
    }

    @ParameterizedTest
    @MethodSource("validPersonalCodes")
    void shouldAcceptValidPersonalCodes(String code) {
        assertThat(validator.isValid(code, null)).isTrue();
    }

    static Stream<String> validPersonalCodes() {
        return Stream.of(
                "49403136515",  // Female, 1994
                "39912310174",  // Male, 1999
                "50301010004",  // Male, 2003
                "60101010006",  // Female, 2001
                "37605030299"   // Male, 1976
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldAcceptNullAndBlank(String code) {
        assertThat(validator.isValid(code, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567890",       // Too short (10 digits)
            "123456789012",     // Too long (12 digits)
            "4940313651A",      // Contains letter
            "abcdefghijk"       // All letters
    })
    void shouldRejectInvalidFormat(String code) {
        assertThat(validator.isValid(code, null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "09403136515",      // Gender digit 0
            "79403136515",      // Gender digit 7
            "89403136515"       // Gender digit 8
    })
    void shouldRejectInvalidGenderDigit(String code) {
        assertThat(validator.isValid(code, null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "49413136515",      // Month 13
            "49400006515",      // Month 00
            "49403326515",      // Day 32
            "49402300004"       // Feb 30
    })
    void shouldRejectInvalidDate(String code) {
        assertThat(validator.isValid(code, null)).isFalse();
    }

    @Test
    void shouldRejectInvalidChecksum() {
        // Valid code is 49403136515, change last digit
        assertThat(validator.isValid("49403136516", null)).isFalse();
        assertThat(validator.isValid("49403136510", null)).isFalse();
    }
}
