package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthEntryPointJwtTest {
    private AuthEntryPointJwt authEntryPointJwt;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException authException;
    @Mock
    private Logger logger;

    private ByteArrayOutputStream responseStream;
    private ServletOutputStream servletOutputStream;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        authEntryPointJwt = new AuthEntryPointJwt();
        responseStream = new ByteArrayOutputStream();
        servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                responseStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true; // Always ready to write
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                throw new UnsupportedOperationException("Unimplemented method 'setWriteListener'");
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);
    }

    @Test
    void commence_shouldSetUnauthorizedResponse_andWriteJsonBody() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn("/api/test");
        when(authException.getMessage()).thenReturn("Bad credentials");

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = responseStream.toString();
        // On vérifie que le JSON contient les champs attendus
        assertThat(json)
            .contains("\"status\":401")
            .contains("\"error\":\"Unauthorized\"")
            .contains("\"message\":\"Bad credentials\"")
            .contains("\"path\":\"/api/test\"");
    }
}
