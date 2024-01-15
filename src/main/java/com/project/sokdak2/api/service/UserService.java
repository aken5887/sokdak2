package com.project.sokdak2.api.service;


import com.project.sokdak2.api.domain.user.Session;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.exception.InvalidLoginException;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.Login;
import com.project.sokdak2.api.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public SessionResponse login(Login login){
    User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
        .orElseThrow(() -> new InvalidLoginException());
    Session session = user.addSession();
    return SessionResponse.builder().accessToken(session.getAccessToken()).build();
  }

  @Transactional
  public User loginUser(Login login){
    User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
        .orElseThrow(() -> new InvalidLoginException());
    user.addSession();
    return user;
  }

  public List<String> findUserEmails(){
    List<User> users = userRepository.findAll();
    return users.stream()
            .filter(user -> !user.getEmail().isEmpty() && isValidEmail(user.getEmail()))
            .map(user -> user.getEmail())
            .collect(Collectors.toList());
  }

  public boolean isValidEmail(String email){
    String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    return pattern.matcher(email).matches();
  }
}
