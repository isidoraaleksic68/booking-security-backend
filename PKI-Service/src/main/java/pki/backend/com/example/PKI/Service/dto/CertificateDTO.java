package pki.backend.com.example.PKI.Service.dto;


import org.springframework.beans.factory.annotation.Autowired;
import pki.backend.com.example.PKI.Service.model.Certificate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

public class CertificateDTO {

    private BigInteger serialNumber;
    private String subjectEmail;
    private String commonName;
    private String organization;
    private Date startDate;
    private Date endDate;
    private ArrayList<String> keyUsage;
    private ArrayList<String> extendedKeyUsage;
    private String issuerEmail;

    private String issuerCommonName;
    private String issuerOrganization;


//    @Autowired
//    private UserRepo userRepo;

    public CertificateDTO() {

    }
    public CertificateDTO(BigInteger serialNumber, String subjectEmail, String commonName, String organization) {
        this.serialNumber = serialNumber;
        this.subjectEmail = subjectEmail;
        this.commonName = commonName;
        this.organization = organization;

    }
    public CertificateDTO(Certificate certificate){
        this.serialNumber = certificate.getSerialNumber();
        this.subjectEmail = certificate.getSubjectEmail();
        this.startDate = certificate.getStartDate();
        this.endDate = certificate.getEndDate();
//        this.extendedKeyUsage = certificate.getExtendedKeyUsage();
//        this.keyUsage = certificate.getKeyUsage();
        this.issuerEmail = certificate.getIssuerEmail();
        // OVE PARAMETRE IZVLACIMO IZ USER-A
        this.commonName = null;
        this.organization = null;
        this.issuerOrganization = null;
        this.issuerCommonName = null;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(ArrayList<String> keyUsage) {
        this.keyUsage = keyUsage;
    }

    public ArrayList<String> getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(ArrayList<String> extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public String getIssuerEmail() {
        return issuerEmail;
    }

    public void setIssuerEmail(String issuerEmail) {
        this.issuerEmail = issuerEmail;
    }

    public String getIssuerCommonName() {
        return issuerCommonName;
    }

    public void setIssuerCommonName(String issuerCommonName) {
        this.issuerCommonName = issuerCommonName;
    }

    public String getIssuerOrganization() {
        return issuerOrganization;
    }

    public void setIssuerOrganization(String issuerOrganization) {
        this.issuerOrganization = issuerOrganization;
    }
}