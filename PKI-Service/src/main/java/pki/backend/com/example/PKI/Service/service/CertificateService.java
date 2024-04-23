package pki.backend.com.example.PKI.Service.service;

import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cmc.CertificationRequest;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pki.backend.com.example.PKI.Service.dto.CertificateDTO;
import pki.backend.com.example.PKI.Service.dto.RequestDTO;

import pki.backend.com.example.PKI.Service.model.CertificateData;
import pki.backend.com.example.PKI.Service.model.MyCertificate;
import pki.backend.com.example.PKI.Service.model.Request;
import pki.backend.com.example.PKI.Service.repository.CertificateDataRepository;
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
import java.util.Arrays;
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

    @Autowired
    private CertificateDataRepository certificateDataRepository;



    //TODO: Funkcije za kreirati
    // createRootCert() --> ne treba
    // createIntermediate --> done
    // createEECertificate() --> done
    // isCertificateValid()
    // generateKeyPair() --> done


    public X509Certificate createCertificate(CertificateDTO dto) throws Exception {
        KeyPair keyPair = generateKeyPair();
        Request request = requestRepository.findById(dto.getRequestId());
        X509Certificate issuer = keyStoreService.getCertificateByAlias(dto.getIssuer());
        CertificateData issuerData = certificateDataRepository.findBySubjectSerialNumber(issuer.getSerialNumber());
        BigInteger serialNumber = new BigInteger(64, new SecureRandom());
        // Prepare the X509CertificateBuilder
        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                CertificateUtils.createOrganisation(dto.getCommonName(), request.getOrganisation(),
                        request.getOrganisationUnit(), request.getCountry()),  // issuer
                serialNumber,                 // serial number
                new Date(System.currentTimeMillis()), // validity start
                new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L), // validity end
                CertificateUtils.createPerson(issuer.getSubjectX500Principal().getName(), null, null, null,
                        null, null),            // subject
                SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()) // subject public key
        );

        addExtensionsToCertificate(certBuilder, dto, keyPair.getPublic());

        // Create a ContentSigner to sign the certificate
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyStoreService.getPrivateKey(issuerData.getSubjectAlias()));

        String alias = CertificateUtils.generateAlias(request.getOrganisation());
        // Build the certificate
        X509Certificate newCert = new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));

        if (dto.isCa()){
            keyStoreService.saveRootCertificate(false, alias, newCert, keyPair.getPrivate());
        } else {
            keyStoreService.saveEndEntityCertificate(alias, newCert);
        }
        CertificateData dataAboutSert = new CertificateData(alias, issuerData.getSubjectAlias(), serialNumber,
                dto.isCa(), dto.isDs(), dto.isKe(), dto.isKcs(), dto.isCrls(), false);
        certificateDataRepository.save(dataAboutSert);
        return newCert;
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
            CertificateData data = certificateDataRepository.findBySubjectSerialNumber(certificate.getX509Certificate().getSerialNumber());
          CertificateDTO dto = new CertificateDTO(certificate, data);
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
        KeyPair keyPair = generateKeyPair();
        BigInteger serialNumber = new BigInteger(64, new SecureRandom());

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                CertificateUtils.createOrganisation("Root CA", "My Root organisation enterprise",
                                                    "Certificate issuer department", "SRB"),  // issuer
                serialNumber,                 // serial number
                new Date(System.currentTimeMillis()), // validity start
                new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L), // validity end
                CertificateUtils.createOrganisation("Root CA", "My Root organisation enterprise",
                        "Certificate issuer department", "SRB"),            // subject
                SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()) // subject public key
        );

        BasicConstraints basicConstraints = new BasicConstraints(true);

        AuthorityKeyIdentifier a = new AuthorityKeyIdentifier(keyPair.getPublic().getEncoded());
        certBuilder.addExtension(Extension.authorityKeyIdentifier, false, a);

        boolean[] certificateRole = new boolean[]{true, true, false, false, false};

        int roleValue = 0;
        for (int i = 0; i < certificateRole.length; i++) {
            if(certificateRole[i]){
                roleValue |= (1 << i);
            }
        }

        KeyUsage roleUsage = new KeyUsage(roleValue);
        byte[] PKID = makePublicKeyID(keyPair.getPublic().getEncoded());
        certBuilder.addExtension(Extension.subjectKeyIdentifier, false, PKID);

        certBuilder.addExtension(Extension.keyUsage, true, roleUsage);
        certBuilder.addExtension(Extension.basicConstraints, true, basicConstraints);

        String alias = CertificateUtils.generateAlias("my root organisation enterprise");
        System.out.println("ROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOTALIAS:        " + alias);
        // Create a ContentSigner to sign the certificate
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());

        // Build the certificate
        X509Certificate newCert = new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));
        keyStoreService.saveRootCertificate(true, alias, newCert, keyPair.getPrivate());
        CertificateData dataAboutSert = new CertificateData(alias, alias, serialNumber, true, true,
                false, false, false,false);
        certificateDataRepository.save(dataAboutSert);
        return newCert;
    }

    public void addExtensionsToCertificate(X509v3CertificateBuilder  builder, CertificateDTO certificateDTO, PublicKey subjectPublicKey) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        BasicConstraints basicConstraints = new BasicConstraints(certificateDTO.isCa());

        if (certificateDTO.isCa()){
            AuthorityKeyIdentifier a = new AuthorityKeyIdentifier(keyStoreService.getCertificateByAlias(certificateDTO.getIssuer()).getPublicKey().getEncoded());
            builder.addExtension(Extension.authorityKeyIdentifier, false, a);
        }

        if (!certificateDTO.isCa()){
            GeneralName san = new GeneralName(GeneralName.dNSName, requestRepository.findById(certificateDTO.getRequestId()).getOrganisation());
            GeneralNames sanNames = new GeneralNames(san);
            builder.addExtension(org.bouncycastle.asn1.x509.Extension.subjectAlternativeName, false, sanNames);
        }

        boolean[] certificateRole = new boolean[5];
        certificateRole[0] = certificateDTO.isCa();
        certificateRole[1] = certificateDTO.isDs();
        certificateRole[2] = certificateDTO.isKcs();
        certificateRole[3] = certificateDTO.isKe();
        certificateRole[4] = certificateDTO.isCrls();

        int roleValue = 0;
        for (int i = 0; i < certificateRole.length; i++) {
            if(certificateRole[i]){
                roleValue |= (1 << i);
            }
        }

        KeyUsage roleUsage = new KeyUsage(roleValue);
        byte[] PKID = makePublicKeyID(subjectPublicKey.getEncoded());
        builder.addExtension(Extension.subjectKeyIdentifier, false, PKID);

        builder.addExtension(Extension.keyUsage, true, roleUsage);
        builder.addExtension(Extension.basicConstraints, true, basicConstraints);
    }

    public static byte[] makePublicKeyID(byte[] publicKey)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(publicKey);
        return Arrays.copyOfRange(hash, 0, 20);
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
