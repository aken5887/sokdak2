package com.project.dailylog.api.service;


import com.project.dailylog.api.domain.User;
import com.project.dailylog.api.exception.InvalidLoginException;
import com.project.dailylog.api.repository.UserRepository;
import com.project.dailylog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User login(Login login){
    User user = userRepository.findByUserIdAndPassword(login.getUserId(), login.getPassword())
        .orElseThrow(() -> new InvalidLoginException());
    return user;
  }
}
