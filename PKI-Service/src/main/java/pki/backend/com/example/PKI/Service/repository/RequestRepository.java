package pki.backend.com.example.PKI.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pki.backend.com.example.PKI.Service.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findByEmail(String email);
    Request findById(int id);
    List<Request> findAll();
}
