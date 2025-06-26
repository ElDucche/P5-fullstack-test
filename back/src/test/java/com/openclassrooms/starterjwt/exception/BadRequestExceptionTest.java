package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BadRequestExceptionTest {
    @Test
    void testNoArgConstructor() {
        BadRequestException exception = new BadRequestException();
        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception.getMessage()).isNull();
    }
}
