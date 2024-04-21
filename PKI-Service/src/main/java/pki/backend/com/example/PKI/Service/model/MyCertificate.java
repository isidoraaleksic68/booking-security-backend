package pki.backend.com.example.PKI.Service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.cert.X509Certificate;

@Getter
@Setter
@AllArgsConstructor
public class MyCertificate {

    private String alias;

    private X509Certificate x509Certificate;//U ISSUER PRINCIPALU POD 'AL' IMAM ISSUEROV ALIAS

    public MyCertificate() {}

    public MyCertificate(Boolean revoked, X509Certificate x509Certificate) {
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


}
