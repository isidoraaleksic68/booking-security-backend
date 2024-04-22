package pki.backend.com.example.PKI.Service.service;

import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pki.backend.com.example.PKI.Service.dto.CertificateDTO;
import pki.backend.com.example.PKI.Service.dto.RequestDTO;

import pki.backend.com.example.PKI.Service.model.MyCertificate;
import pki.backend.com.example.PKI.Service.model.Request;
import pki.backend.com.example.PKI.Service.repository.RequestRepository;
import org.bouncycastle.asn1.x509.Extension;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@Service
public class CertificateService {

    //NOTE: getUser i getAll metode dodate u READER KLASU!

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private KeyStoreService keyStoreService;

//
//    @Autowired
//    public CertificateService(RequestRepository requestRepository, KeyStoreService keyStoreService){
//        this.keyStoreService = keyStoreService;
//        this.requestRepository = requestRepository;
//    }

    //TODO: Funkcije za kreirati
    // createRootCert() --> ne treba
    // createIntermediate --> done
    // createEECertificate() --> done
    // isCertificateValid()
    // generateKeyPair() --> done


    public X509Certificate createCertificate(CertificateDTO dto) throws Exception {

        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(new BigInteger(128, new SecureRandom()));
        X509Certificate issuerCert = keyStoreService.getCertificateByAlias(dto.getIssuer());
        if (! CertificateUtils.datesInRangeOfIssuer(dto.transformToDate(dto.getStartDate()), dto.transformToDate(dto.getEndDate()), issuerCert)){
            return null;
        }
        certGen.setNotBefore(dto.transformToDate(dto.getStartDate()));
        certGen.setNotAfter(dto.transformToDate(dto.getEndDate()));

        Request r = requestRepository.findById(dto.getRequestId());
        certGen.setSubjectDN(new X500Principal("CN=" + dto.getCommonName() + ", O=" + r.getOrganisation() + ", OU=" + r.getOrganisationUnit() + ", C=" + r.getCountry()));

        X500Principal issuerX500Principal = issuerCert.getSubjectX500Principal();
        String issuerOrganisation = issuerX500Principal.getName("O");
        String issuerOrganisationUnit = issuerX500Principal.getName("OU");
        String issuerCountry = issuerX500Principal.getName("C");
        String issuerName = issuerX500Principal.getName("CN");
        certGen.setIssuerDN(new X500Principal("CN=" + issuerName + ", O=" + issuerOrganisation + ", OU=" + issuerOrganisationUnit + ", C=" + issuerCountry + ", L=" + dto.getIssuer()));
        KeyPair keyPair = generateKeyPair();

        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        certGen.setPublicKey(keyPair.getPublic());

        certGen.addExtension("isRevoked", false, CertificateUtils.booleanToByteArray(false));
        certGen.addExtension("isCA", false, CertificateUtils.booleanToByteArray(dto.isCA()));
        certGen.addExtension("isDS", false, CertificateUtils.booleanToByteArray(dto.isDS()));
        certGen.addExtension("isKE", false, CertificateUtils.booleanToByteArray(dto.isKE()));
        certGen.addExtension("isKCS", false, CertificateUtils.booleanToByteArray(dto.isKCS()));
        certGen.addExtension("isCRLS", false, CertificateUtils.booleanToByteArray(dto.isCRLS()));

        // Generate certificate
        X509Certificate x509newCert = certGen.generate(keyStoreService.getPrivateKey(dto.getIssuer()), "BC");
        MyCertificate newCert = new MyCertificate(false, x509newCert);
        String newCertAlias = CertificateUtils.generateAlias(r.getOrganisationUnit());

        if (dto.isCA()) {
            keyStoreService.saveRootCertificate(false,newCertAlias, newCert.getX509Certificate(), keyPair.getPrivate());
        }
        else {
            keyStoreService.saveEndEntityCertificate(newCertAlias, newCert.getX509Certificate());
        }
        return newCert.getX509Certificate();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public void addCertificateCreationRequest(RequestDTO dto){
        Request r = new Request(dto);
        requestRepository.save(r);
    }


    public List<CertificateDTO> getAllCertificates() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        List<MyCertificate> temp = keyStoreService.getAllCertificates();
        List<CertificateDTO> dtos = new ArrayList<>();
        for (MyCertificate certificate : temp) {
          CertificateDTO dto = new CertificateDTO(certificate);
          dtos.add(dto);
        }
        return dtos;
    }

    public List<RequestDTO> getAllRequests() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        List<Request> requests = requestRepository.findAll();
        List<RequestDTO> dtos = new ArrayList<>();
        for (Request request : requests) {
            dtos.add(new RequestDTO(request));
        }
        return dtos;
    }

    public X509Certificate generateRootCertificate() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = generateKeyPair();
        // Set certificate attributes
        String newCertAlias = CertificateUtils.generateAlias("RootCompanyEnterprise");
        X500Principal issuer = new X500Principal("CN=RootCA, L=" + newCertAlias);
        X500Principal subject = issuer; // Self-signed, so issuer and subject are the same
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // Valid for 1 year
        BigInteger serialNumber = new BigInteger(128, new SecureRandom());

        // Create certificate
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(serialNumber);
        certGen.setIssuerDN(issuer);
        certGen.setNotBefore(startDate);
        certGen.setNotAfter(endDate);
        certGen.setSubjectDN(subject);
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        ExtensionsGenerator extGen = new ExtensionsGenerator();
        ASN1ObjectIdentifier customExtensionOID = new ASN1ObjectIdentifier("1.2.3.4.1");
        byte[] extensionValue = CertificateUtils.booleanToByteArray(false);
        extGen.addExtension(customExtensionOID, true, extensionValue);

        ASN1ObjectIdentifier customExtensionOIDCA = new ASN1ObjectIdentifier("1.2.3.4.2");
        byte[] extensionValueTrue = CertificateUtils.booleanToByteArray(true);
        extGen.addExtension(customExtensionOIDCA, true, extensionValueTrue);

        ASN1ObjectIdentifier customExtensionOIDDS = new ASN1ObjectIdentifier("1.2.3.4.3");
        extGen.addExtension(customExtensionOIDDS, true, extensionValue);

        ASN1ObjectIdentifier customExtensionOIDKE = new ASN1ObjectIdentifier("1.2.3.4.4");
        extGen.addExtension(customExtensionOIDKE, true, extensionValue);

        ASN1ObjectIdentifier customExtensionOIDKCS = new ASN1ObjectIdentifier("1.2.3.4.5");
        extGen.addExtension(customExtensionOIDKCS, true, extensionValue);

        ASN1ObjectIdentifier customExtensionOIDCRLS = new ASN1ObjectIdentifier("1.2.3.4.6");
        extGen.addExtension(customExtensionOIDCRLS, true, extensionValue);

        Extensions extensions = extGen.generate();

        certGen.addExtension(customExtensionOID, true, extensions.getExtension(customExtensionOID).getEncoded());
        certGen.addExtension(customExtensionOIDCA, true, extensions.getExtension(customExtensionOIDCA).getEncoded());
        certGen.addExtension(customExtensionOIDDS, true, extensions.getExtension(customExtensionOIDDS).getEncoded());
        certGen.addExtension(customExtensionOIDKE, true, extensions.getExtension(customExtensionOIDKE).getEncoded());
        certGen.addExtension(customExtensionOIDKCS, true, extensions.getExtension(customExtensionOIDKCS).getEncoded());
        certGen.addExtension(customExtensionOIDCRLS, true, extensions.getExtension(customExtensionOIDCRLS).getEncoded());

        // Sign the certificate
        PrivateKey privateKey = keyPair.getPrivate();
        X509Certificate cert = certGen.generate(privateKey,"BC");

        keyStoreService.saveRootCertificate(true,newCertAlias, cert, keyPair.getPrivate());

        return cert;
    }


//    public void revokeCertificate(String serialNumber, String ksFilePath,String password){
//
//        X509Certificate x509certificate = kSReader.getCertificateBySerialNumber(ksFilePath, password, serialNumber);
//        MyCertificate certificate=convertX509toCertClass(x509certificate);
//        certificate.setRevoked(true);
//
//        try {
//            kSWriter.writeCertificate(certificate.getIssuerEmail(), certificate.getX509Certificate());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
