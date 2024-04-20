package pki.backend.com.example.PKI.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pki.backend.com.example.PKI.Service.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Query(value="SELECT * FROM users u WHERE u.id = :id", nativeQuery = true)
    Optional<User> findById(@Param("id") Integer id); // Assuming id is of type Integer
}
