package pki.backend.com.example.PKI.Service.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import pki.backend.com.example.PKI.Service.model.CertificateData;
import pki.backend.com.example.PKI.Service.model.MyCertificate;
import pki.backend.com.example.PKI.Service.service.CertificateUtils;

import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class CertificateDTO {
    private int requestId;  //request id iz kojeg je nastao sertifikat(potreban za podatke o subjectu)
    private String issuer;  //predstavljace issuer alias!
    private String startDate;
    private String endDate;
    private String subject; //cn od subjecta
    private String alias;   //ovo je nullable jer kad jos nije generisano je NULL!
    private String commonName;
    private boolean isRevoked;
    private boolean isCA;
    private boolean isDS;
    private boolean isKE;
    private boolean isKCS;
    private boolean isCRLS;

    public CertificateDTO(MyCertificate certificate, CertificateData certificateData){
        this.issuer = certificateData.getIssuerAlias();
        this.startDate = certificate.getX509Certificate().getNotBefore().toString();
        this.endDate = certificate.getX509Certificate().getNotAfter().toString();
        this.subject = certificate.getX509Certificate().getSubjectDN().getName();
        this.alias = certificate.getAlias();
        this.isRevoked = certificateData.getIsRevoked();
        this.isCA = certificateData.getIsCA();
        this.isDS = certificateData.getIsDS();
        this.isKE = certificateData.getIsKE();
        this.isKCS = certificateData.getIsKCS();
        this.isCRLS = certificateData.getIsCRLS();
    }

    public Date transformToDate(String dateStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
        }
        return null;
    }

///---------------------------------------------------------------------
//    private BigInteger serialNumber;
//    private String subjectEmail;
//    private String organisation;
//    private ArrayList<String> keyUsage;
//    private ArrayList<String> extendedKeyUsage;
//
//    private String issuerEmail;
//    private String commonName;
//    private Date startDate;
//    private Date endDate;
//
//    private String issuerCommonName;
//    private String issuerOrganization;


}