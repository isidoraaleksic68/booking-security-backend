package pki.backend.com.example.PKI.Service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pki.backend.com.example.PKI.Service.model.User;
import pki.backend.com.example.PKI.Service.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public User save(User user){
        return userRepo.save(user);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> findById(String Id){
        return userRepo.findById(Integer.valueOf(Id));
    }
}
