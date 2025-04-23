package app.service;

import app.entity.User;
import app.repository.UserRepository;
import app.util.exception.ResourceNotFoundException;
import app.util.exception.auth.UserAlreadyExistsAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {
    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * @param user User
     * @return persisted User
     */
    @Transactional
    public User create(User user) throws UserAlreadyExistsAuthenticationException {
        if (repo.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsAuthenticationException(
                    "User with email " + user.getEmail() + " already exists");
        }
        return repo.save(user);
    }

    /**
     * @param email User's email
     * @return User
     */
    public User getByEmail(String email) throws ResourceNotFoundException {
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email " + email + " not found"));
    }

    /**
     * @param id User's email
     * @return User
     */
    public User getById(Long id) throws ResourceNotFoundException {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id " + id + " not found"));
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByEmail(email);
    }
}
