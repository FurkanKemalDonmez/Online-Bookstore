package com.bookstore.api;

import com.bookstore.api.config.JwtService;
import com.bookstore.api.entity.Role;
import com.bookstore.api.entity.User;
import com.bookstore.api.repository.UserRepository;
import com.bookstore.api.request.LoginRequest;
import com.bookstore.api.request.SignupRequest;
import com.bookstore.api.response.AuthenticationResponse;
import com.bookstore.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private final String MOCK_TOKEN = "mockJwtToken";

    @BeforeEach
    public void setup() {
    }

    @Test
    void testSignUp() {
        SignupRequest request = new SignupRequest("exampleName","exampleEmail","examplePassword", Role.USER);
        User mockUser = new User();
        when(userRepository.save(any())).thenReturn(mockUser);
        when(jwtService.generateToken(any())).thenReturn(MOCK_TOKEN);

        AuthenticationResponse response = userService.signUp(request);

        assertNotNull(response);
        assertEquals(MOCK_TOKEN, response.getToken());
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("exampleEmail", "examplePassword");
        User mockUser = new User();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any())).thenReturn(MOCK_TOKEN);

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        AuthenticationResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals(MOCK_TOKEN, response.getToken());
    }
}
