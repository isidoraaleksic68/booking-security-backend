package pki.backend.com.example.PKI.Service.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pki.backend.com.example.PKI.Service.model.Certificate;

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
//    private String type; //sta ce ovaj tip predstavljati??
    private String subject;
    private String alias;   //ovo je nullable jer kad jos nije generisano je NULL!
    private boolean isRevoked;
    private String commonName;
    private boolean isCA;
    private boolean isDS;
    private boolean isKE;
    private boolean isKCS;
    private boolean isCRLS;

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


//    @Autowired
//    private UserRepo userRepo;

//    public CertificateDTO(X509Certificate certificate){
//        this.serialNumber = certificate.getSerialNumber();
//        this.subject = certificate.getSubjectX500Principal().getName();
//        this.startDate = String.valueOf(certificate.getNotBefore());
//        this.endDate = String.valueOf(certificate.getNotAfter());
////        this.extendedKeyUsage = certificate.getExtendedKeyUsage();
////        this.keyUsage = certificate.getKeyUsage();
//        this.issuer = certificate.getIssuerX500Principal().getName();
//        // OVE PARAMETRE IZVLACIMO IZ USER-A
//        this.commonName = null;
//        this.organization = null;
//        this.issuerOrganization = null;
//        this.issuerCommonName = null;
//    }

}