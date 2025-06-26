package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageResponseTest {
    @Test
    void testConstructorAndGetter() {
        MessageResponse response = new MessageResponse("Hello World");
        assertThat(response.getMessage()).isEqualTo("Hello World");
    }

    @Test
    void testSetter() {
        MessageResponse response = new MessageResponse("");
        response.setMessage("New Message");
        assertThat(response.getMessage()).isEqualTo("New Message");
    }
}
