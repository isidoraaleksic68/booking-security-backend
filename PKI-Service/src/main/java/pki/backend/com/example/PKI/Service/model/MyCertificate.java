package pki.backend.com.example.PKI.Service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.cert.X509Certificate;

@Getter
@Setter
@NoArgsConstructor
public class MyCertificate {

    private String alias;

    private X509Certificate x509Certificate;//U ISSUER PRINCIPALU POD 'AL' IMAM ISSUEROV ALIAS

    public MyCertificate(String alias, X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
        this.alias = alias;
    }


}
