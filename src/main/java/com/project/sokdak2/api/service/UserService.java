package com.project.sokdak2.api.service;


import com.project.sokdak2.api.domain.Session;
import com.project.sokdak2.api.domain.User;
import com.project.sokdak2.api.exception.InvalidLoginException;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.Login;
import com.project.sokdak2.api.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public SessionResponse login(Login login){
    User user = userRepository.findByUserIdAndPassword(login.getUserId(), login.getPassword())
        .orElseThrow(() -> new InvalidLoginException());
    Session session = user.addSession();
    return SessionResponse.builder().accessToken(session.getAccessToken()).build();
  }

  @Transactional
  public User loginUser(Login login){
    User user = userRepository.findByUserIdAndPassword(login.getUserId(), login.getPassword())
        .orElseThrow(() -> new InvalidLoginException());
    user.addSession();
    return user;
  }
}