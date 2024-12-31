package com.lilcoin.auth;

import com.lilcoin.config.JwtService;
import com.lilcoin.level.LevelEntity;
import com.lilcoin.level.LevelRepository;
import com.lilcoin.token.Token;
import com.lilcoin.token.TokenRepository;
import com.lilcoin.token.TokenType;
import com.lilcoin.user.Role;
import com.lilcoin.user.User;
import com.lilcoin.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final LevelRepository levelRepository;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
      .firstname(request.getFirstname())
      .lastname(request.getLastname())
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .order(userRepository.count() + 1)
      .role(request.getRole())
      .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );
    var user = repository.findByEmail(request.getEmail())
      .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .build();
  }

  public User registerIfNotExists(String firstName, String lastName, String username, Long botUserId) {
    var userByEmail = repository.findByEmail(botUserId + "@lilcoin1.ru");
    if (userByEmail.isPresent()) {
      User user = userByEmail.get();
      user.setNewUser(false);
      return user;
    }

    var user = User.builder()
      .firstname(firstName)
      .lastname(lastName)
      .email(botUserId + "@lilcoin1.ru")
      .password(username)
      .role(Role.USER)
      .botUserId(botUserId)
      .newUser(true)
      .order(userRepository.count() + 1)
      .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    saveUserToken(savedUser, jwtToken);

    LevelEntity level = new LevelEntity();
    level.setLevel(1);
    level.setUserId(user.getId());
    levelRepository.save(level);

    return user;
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
      .user(user)
      .token(jwtToken)
      .expired(false)
      .revoked(false)
      .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
        .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public String authenticateByUsername(String username) {
    Optional<User> byEmail = userRepository.findByEmail(username + "@lilcoin1.ru");
    if (byEmail.isEmpty()) {
      throw new UsernameNotFoundException("USERNAME_NOT_FOUND");
    }

    List<Token> allValidTokenByUser = tokenRepository.findAllValidTokenByUser(byEmail.get().getId());
    return allValidTokenByUser.get(0).getToken();
  }
}
