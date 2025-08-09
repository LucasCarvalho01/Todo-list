package com.lucascarvalho.todo_list.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Security Tests")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String testSecretKey = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdGVzdGluZy1wdXJwb3Nlcy1vbmx5LW5vdC1mb3ItcHJvZHVjdGlvbg==";
    private final long testExpirationTime = 3600000L;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", testSecretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", testExpirationTime);

        when(userDetails.getUsername()).thenReturn("test@example.com");
    }

    @Test
    @DisplayName("Should generate valid token with correct username")
    void generateToken_ShouldCreateValidTokenWithCorrectUsername() {
        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertThat(token).isNotNull().isNotEmpty();
        assertThat(jwtService.extractUsername(token)).isEqualTo("test@example.com");
        verify(userDetails, atLeastOnce()).getUsername();
    }

    @Test
    @DisplayName("Should validate token successfully for correct user")
    void isTokenValid_ShouldReturnTrue_WhenTokenIsValidForCorrectUser() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should reject token for different user")
    void isTokenValid_ShouldReturnFalse_WhenTokenIsForDifferentUser() {
        // Given
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("different@example.com");

        // When
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should extract expiration date correctly")
    void extractClaim_ShouldExtractExpirationDateCorrectly() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        // Then
        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new Date());

        long expectedExpirationTime = System.currentTimeMillis() + testExpirationTime;
        long actualExpirationTime = expiration.getTime();
        long tolerance = 5000;

        assertThat(actualExpirationTime).isBetween(
                expectedExpirationTime - tolerance,
                expectedExpirationTime + tolerance
        );
    }
}