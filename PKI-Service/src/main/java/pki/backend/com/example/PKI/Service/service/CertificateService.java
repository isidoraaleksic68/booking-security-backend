package pki.backend.com.example.PKI.Service.service;

import org.bouncycastle.x509.X509V3CertificateGenerator;
import pki.backend.com.example.PKI.Service.dto.CertificateDTO;
import pki.backend.com.example.PKI.Service.dto.RequestDTO;

import pki.backend.com.example.PKI.Service.model.MyCertificate;
import pki.backend.com.example.PKI.Service.model.Request;
import pki.backend.com.example.PKI.Service.repository.RequestRepository;

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
import java.util.List;

public class CertificateService {

    //NOTE: getUser i getAll metode dodate u READER KLASU!

    RequestRepository requestRepository;
    KeyStoreService keyStoreService;


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
        certGen.setIssuerDN(new X500Principal("CN=" + issuerName + ", O=" + issuerOrganisation + ", OU=" + issuerOrganisationUnit + ", C=" + issuerCountry + ", AL=" + dto.getIssuer()));
        KeyPair keyPair = generateKeyPair();

        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        certGen.setPublicKey(keyPair.getPublic());

        certGen.addExtension("isRevoked", false, CertificateUtils.booleanToByteArray(false));
        //todo: dodaj ostale ekstenzije....

        // Generate certificate
        X509Certificate x509newCert = certGen.generate(keyStoreService.getPrivateKey(dto.getIssuer()), "BC");
        MyCertificate newCert = new MyCertificate(false, x509newCert);
        String newCertAlias = CertificateUtils.generateAlias(r.getOrganisationUnit());
        //todo: po ekstenzijama odrediti da li je ICA ili je EE u pitanju, mislim da ide ovako ali treba proveriti sutra!
        if (dto.isCA()) {
            keyStoreService.saveRootCertificate(newCertAlias, newCert.getX509Certificate(), keyPair.getPrivate());
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
