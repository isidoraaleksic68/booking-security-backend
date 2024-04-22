package pki.backend.com.example.PKI.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pki.backend.com.example.PKI.Service.model.CertificateData;

import java.math.BigInteger;

@Repository
public interface CertificateDataRepository extends JpaRepository<CertificateData, Long> {
    CertificateData findBySubjectSerialNumber(BigInteger serialNumber);
}
