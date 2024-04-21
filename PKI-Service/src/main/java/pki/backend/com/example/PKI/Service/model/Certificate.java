package pki.backend.com.example.PKI.Service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.cert.X509Certificate;

@Getter
@Setter
@AllArgsConstructor
public class Certificate {

//    private String subjectEmail;
//    private String issuerEmail;
//    private BigInteger serialNumber;
//    private Date startDate;
//    private Date endDate;
    private Boolean revoked;



    // svi prethodni podaci mogu da se izvuku i iz X509Certificate, osim privatnog kljuca issuera
    private X509Certificate x509Certificate;

    public Certificate() {}

    public Certificate(Boolean revoked, X509Certificate x509Certificate) {

        this.revoked = revoked;
        this.x509Certificate = x509Certificate;


//        this.keyUsage = keyUsage;
//        this.extendedKeyUsage = extendedKeyUsage;
//        this.issuerSerialNumber = issuerSerialNumber;
//        this.serialNumber = serialNumber;
//        this.subjectEmail = subjectEmail;
//        this.issuerEmail = issuerEmail;
//        this.startDate = startDate;
//        this.endDate = endDate;

    }

//    public BigInteger getSerialNumber() {
//        return serialNumber;
//    }
//
//    public void setSerialNumber(BigInteger serialNumber) {
//        this.serialNumber = serialNumber;
//    }
//
//    public String getSubjectEmail() {
//        return subjectEmail;
//    }
//
//    public void setSubjectEmail(String subjectEmail) {
//        this.subjectEmail = subjectEmail;
//    }
//
//    public String getIssuerEmail() {
//        return issuerEmail;
//    }
//
//    public void setIssuerEmail(String issuerEmail) {
//        this.issuerEmail = issuerEmail;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }

//    public ArrayList<String> getKeyUsage() {
//        return keyUsage;
//    }
//
//    public void setKeyUsage(ArrayList<String> keyUsage) {
//        this.keyUsage = keyUsage;
//    }
//
//    public ArrayList<String> getExtendedKeyUsage() {
//        return extendedKeyUsage;
//    }
//
//    public void setExtendedKeyUsage(ArrayList<String> extendedKeyUsage) {
//        this.extendedKeyUsage = extendedKeyUsage;
//    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }
//    public String getIssuerSerialNumber() {
//        return issuerSerialNumber;
//    }
//
//    public void setIssuerSerialNumber(String issuerSerialNumber) {
//        this.issuerSerialNumber = issuerSerialNumber;
//    }

}
