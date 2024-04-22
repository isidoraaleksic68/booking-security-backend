package pki.backend.com.example.PKI.Service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "certificate_data")
@NoArgsConstructor
@Getter
@Setter
public class CertificateData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String issuerAlias;
    private String subjectAlias;
    private BigInteger subjectSerialNumber;
    private Boolean isRevoked;
    private Boolean isCA;
    private Boolean isDS;
    private Boolean isKE;
    private Boolean isKCS;
    private Boolean isCRLS;


    public CertificateData(String subjectAlias, String issuerAlias, BigInteger subjectSerialNumber, boolean isCA,
                        boolean isDS, boolean isKE, boolean isKCS, boolean isCRLS, boolean isRevoked){
        this.subjectAlias = subjectAlias;
        this.issuerAlias = issuerAlias;
        this.subjectSerialNumber = subjectSerialNumber;
        this.isCA = isCA;
        this.isDS = isDS;
        this.isKE = isKE;
        this.isKCS = isKCS;
        this.isCRLS = isCRLS;
        this.isRevoked = isRevoked;
    }


    @Override
    public String toString() {
        return "CertificateData{" +
                "issuerAlias='" + issuerAlias + '\'' +
                ", subjectAlias='" + subjectAlias + '\'' +
                ", subjectSerialNumber=" + subjectSerialNumber +
                ", isRevoked=" + isRevoked +
                ", isCA=" + isCA +
                ", isDS=" + isDS +
                ", isKE=" + isKE +
                ", isKCS=" + isKCS +
                ", isCRLS=" + isCRLS +
                '}';
    }
}
