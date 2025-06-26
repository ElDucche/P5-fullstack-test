package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotFoundExceptionTest {
    @Test
    void testNoArgConstructor() {
        NotFoundException exception = new NotFoundException();
        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception.getMessage()).isNull();
    }
}
