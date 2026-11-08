
package com.freelancehub.service;

import com.freelancehub.model.Client;
import com.freelancehub.model.Freelancer;
import com.freelancehub.model.User;
import com.freelancehub.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Client registerClient(
            String name,
            String email,
            String password,
            String companyName) {

        checkEmail(email);

        String hashedPassword = passwordEncoder.encode(password);

        Client client = new Client(
                name, email, hashedPassword, companyName
        );

        return (Client) userRepository.save(client);
    }

    public Freelancer registerFreelancer(
            String name,
            String email,
            String password,
            String skills,
            int experienceYears) {

        checkEmail(email);

        if (experienceYears < 0) {
            throw new IllegalArgumentException(
                    "Experience cannot be negative"
            );
        }

        String hashedPassword = passwordEncoder.encode(password);

        Freelancer freelancer = new Freelancer(
                name, email, hashedPassword, skills, experienceYears
        );

        return (Freelancer) userRepository.save(freelancer);
    }

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }
    }

public User login(String email, String password) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Invalid email or password"
            ));

    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new IllegalArgumentException(
                "Invalid email or password"
        );
    }

    return user;
}

}
