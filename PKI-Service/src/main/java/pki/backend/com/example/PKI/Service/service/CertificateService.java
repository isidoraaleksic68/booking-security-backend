package pki.backend.com.example.PKI.Service.service;

import org.bouncycastle.x509.X509V3CertificateGenerator;
import pki.backend.com.example.PKI.Service.dto.CertificateDTO;
import pki.backend.com.example.PKI.Service.keystore.KeyStoreReader;

import pki.backend.com.example.PKI.Service.keystore.KeyStoreWriter;
import pki.backend.com.example.PKI.Service.model.Certificate;
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
import java.util.UUID;

public class CertificateService {

    //NOTE: getUser i getAll metode dodate u READER KLASU!

    KeyStoreReader kSReader; //todo: ovo izmeniti, ova dva objekta ne treba da budu tu vec treba da imamo samo KeyStoreService
    KeyStoreWriter kSWriter; //todo: ovo izmeniti, ova dva objekta ne treba da budu tu vec treba da imamo samo KeyStoreService
    RequestRepository requestRepository;
    KeyStoreService keyStoreService;


    //TODO: Funkcije za kreirati
    // createRootCert() --> ne treba
    // createIntermediate
    // createEECertificate()
    // isCertificateValid()
    // generateKeyPair() --> done


    public X509Certificate createCertificate(CertificateDTO dto) throws Exception {

        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(new BigInteger(128, new SecureRandom()));
        certGen.setNotBefore(dto.transformToDate(dto.getStartDate()));
        certGen.setNotAfter(dto.transformToDate(dto.getEndDate()));

        Request r = requestRepository.findById(dto.getRequestId());
        certGen.setSubjectDN(new X500Principal("CN=" + dto.getCommonName() + ", O=" + r.getOrganisation() + ", OU=" + r.getOrganisationUnit() + ", C=" + r.getCountry()));

        Certificate issuer = getByAlias(dto.getIssuer());
        X500Principal subjectPrincipal = issuer.getX509Certificate().getSubjectX500Principal();
        String issuerOrganisation = subjectPrincipal.getName("O");
        String issuerOrganisationUnit = subjectPrincipal.getName("OU");
        String issuerCountry = subjectPrincipal.getName("C");
        String issuerName = subjectPrincipal.getName("CN");
        certGen.setIssuerDN(new X500Principal("CN=" + issuerName + ", O=" + issuerOrganisation + ", OU=" + issuerOrganisationUnit + ", C=" + issuerCountry));

        KeyPair keyPair = generateKeyPair();

        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        // Generisanje sertifikata
        X509Certificate x509newCert = certGen.generate(keyPair.getPrivate(), "BC");
        Certificate newCert = new Certificate(false, x509newCert);
        String newCertAlias = generateAlias(r.getOrganisationUnit());
        //todo: po ekstenzijama odrediti da li je ICA ili je EE u pitanju, za sada sam ostavio da je ICA
        keyStoreService.saveRootCertificate(newCertAlias, newCert.getX509Certificate(), keyPair.getPrivate());
        return newCert.getX509Certificate();
    }

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }


    public void revokeCertificate(String serialNumber, String ksFilePath,String password){

        X509Certificate x509certificate = kSReader.getCertificateBySerialNumber(ksFilePath, password, serialNumber);
        Certificate certificate=convertX509toCertClass(x509certificate);
        certificate.setRevoked(true);

        try {
            kSWriter.writeCertificate(certificate.getIssuerEmail(), certificate.getX509Certificate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Certificate getByAlias(String alias) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        //todo: namestiti da se ovaj revoked ucitava...nekako kasnije
        return new Certificate(false,  keyStoreService.getCertificate(alias));
    }






    public static String generateAlias(String companyName) {
        UUID uuid = UUID.randomUUID();
        return companyName.trim().replaceAll("\\s+", "").concat("-").concat(uuid.toString().replace("-", ""));
    }




    //TODO: implementiraj getAll i getBySubjectEmail!!!!!
    // 1. ---> izvuci  all certs iz .jks
    // 2. ---> izvuci certs iz .jks, idi kroz certs, proveri poklapanje subjecta, dodaj u listu i vrati

    public ArrayList<Certificate> getAll(){
        return new ArrayList<>();
    }

    public ArrayList<Certificate> getBySubjectEmail(String email){
        return new ArrayList<>();
    }
}
