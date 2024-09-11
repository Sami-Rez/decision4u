package at.spengergasse.decision.service;

import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    private static RuntimeException ofUserNotFound(String userId) {
        return new IllegalArgumentException("User not found with id " + userId);
    }

    private static RuntimeException ofEmailTaken(String email) {
        return new IllegalArgumentException("Email already taken: " + email);
    }

    public User findById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> ofUserNotFound(userId));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllExclude(String userId) {
        return userRepository.findAllExclude(userId);
    }

    public void checkEmailNotTaken(String email) {
        if (userRepository.existsByEmail(email)) throw ofEmailTaken(email);
    }
}
