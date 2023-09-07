package com.project.dailylog.api.service;


import com.project.dailylog.api.domain.Session;
import com.project.dailylog.api.domain.User;
import com.project.dailylog.api.exception.InvalidLoginException;
import com.project.dailylog.api.repository.UserRepository;
import com.project.dailylog.api.request.Login;
import com.project.dailylog.api.response.SessionResponse;
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
}
