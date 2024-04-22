package pki.backend.com.example.PKI.Service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "certificate_data")
@AllArgsConstructor

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

    public CertificateData() {

    }


    public String getIssuerAlias() {
            return issuerAlias;
        }

    public Boolean getRevoked() {
        return isRevoked;
    }

    public void setRevoked(Boolean revoked) {
        isRevoked = revoked;
    }

    public Boolean getCA() {
        return isCA;
    }

    public void setCA(Boolean CA) {
        isCA = CA;
    }

    public Boolean getDS() {
        return isDS;
    }

    public void setDS(Boolean DS) {
        isDS = DS;
    }

    public Boolean getKE() {
        return isKE;
    }

    public void setKE(Boolean KE) {
        isKE = KE;
    }

    public Boolean getKCS() {
        return isKCS;
    }

    public void setKCS(Boolean KCS) {
        isKCS = KCS;
    }

    public Boolean getCRLS() {
        return isCRLS;
    }

    public void setCRLS(Boolean CRLS) {
        isCRLS = CRLS;
    }

    public void setIssuerAlias(String issuerAlias) {
            this.issuerAlias = issuerAlias;
        }

    public String getSubjectAlias() {
        return subjectAlias;
    }

    public void setSubjectAlias(String subjectAlias) {
        this.subjectAlias = subjectAlias;
    }

    public BigInteger getSubjectSerialNumber() {
        return subjectSerialNumber;
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

    public void setSubjectSerialNumber(BigInteger subjectSerialNumber) {
            this.subjectSerialNumber = subjectSerialNumber;
        }



}
