package com.project.sokdak2.api.service;


import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

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
