package com.project.sokdak2.api.service;


import com.project.sokdak2.api.domain.user.Role;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.exception.AlreadyExistsException;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

  public SessionUser signup(SessionUser sessionUser){
    Optional<User> user = userRepository.findUserByEmail(sessionUser.getEmail());
    if(user.isPresent()){
      throw new AlreadyExistsException("이미 존재하는 이메일 입니다.");
    }

    User newUser = User.builder()
            .name(sessionUser.getName())
            .email(sessionUser.getEmail())
            .password(sessionUser.getPassword())
            .role(Role.GENERAL)
            .build();

    userRepository.save(newUser);

    return newUser.toSessionUser();
  }
}
