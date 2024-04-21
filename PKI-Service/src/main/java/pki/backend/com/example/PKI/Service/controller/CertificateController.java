package pki.backend.com.example.PKI.Service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pki.backend.com.example.PKI.Service.dto.CertificateDTO;
import pki.backend.com.example.PKI.Service.dto.RequestDTO;
import pki.backend.com.example.PKI.Service.model.Certificate;
import pki.backend.com.example.PKI.Service.model.User;
import pki.backend.com.example.PKI.Service.service.CertificateService;
import pki.backend.com.example.PKI.Service.service.KeyStoreService;
import pki.backend.com.example.PKI.Service.service.UserService;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class CertificateController {

    CertificateService certificateService = new CertificateService();
    UserService userService=new UserService();
    KeyStoreService keyStoreService = new KeyStoreService();
    public static String KEYSTORE_PATH = "";

    //TODO:
    // getAll()
    // download()
    // getAllUserCertificates() --> userAlias
    // generateCertificate() ---> iCA, EE


    //TODO: IZMENI DA VRACA CERTIFICATE DTO!
    @GetMapping("/revoke/{userAlias}/{serialNumber}")
    public ResponseEntity<Void> revokeCertificate(@PathVariable String userAlias, @PathVariable String serialNumber) {
        String keyStorePassword = null;
        try {
            keyStorePassword = keyStoreService.getKeyStorePassword(userAlias);
            certificateService.revokeCertificate(KEYSTORE_PATH, serialNumber, keyStorePassword);
            return new ResponseEntity(HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/getAll/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<CertificateDTO>> getAllCertificatesForAUser(@RequestParam(name = "email") String email) {
        User user = userService.findByEmail(email);

        ArrayList<CertificateDTO> certificateDtos = new ArrayList<>();
        for (Certificate certificate : certificateService.getBySubjectEmail(email)) {

            java.security.cert.Certificate check = null;
            try {
                check = keyStoreService.getCertificate(certificate.getSerialNumber().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            X509Certificate currCert = (X509Certificate)check;

            if(!certificate.getRevoked() && currCert.getBasicConstraints() > -1 &&
                    certificate.getEndDate().getYear() - certificate.getStartDate().getYear() > 1 ) {

                CertificateDTO c = new CertificateDTO(certificate);

                c.setCommonName(user.getCommonName());
                c.setOrganization(user.getOrganization());
                User issuer = userService.findByEmail(c.getIssuerEmail());
                c.setIssuerCommonName(issuer.getCommonName());
                c.setIssuerOrganization(issuer.getOrganization());

                certificateDtos.add(c);
            }
        }

        return new ResponseEntity(certificateDtos, HttpStatus.OK);
    }


    @GetMapping(value = "/getAllUserCertificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<CertificateDTO>> getAllCertificates() {
        ArrayList<CertificateDTO> certificateDtos = new ArrayList<>();
        for (Certificate certificate : certificateService.getAll()) {

            java.security.cert.Certificate check = null;
            try {
                check = keyStoreService.getCertificate(certificate.getSerialNumber().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            X509Certificate currCert = (X509Certificate) check;

            if (!certificate.getRevoked() && currCert.getBasicConstraints() > -1 &&
                    certificate.getEndDate().getYear() - certificate.getStartDate().getYear() > 1) {

                User user = userService.findByEmail(certificate.getSubjectEmail());

                CertificateDTO c = new CertificateDTO(certificate);
                c.setCommonName(user.getCommonName());
                c.setOrganization(user.getOrganization());
                certificateDtos.add(c);
            }
        }

        return new ResponseEntity(certificateDtos, HttpStatus.OK);
    }

    @PostMapping(value = "/createCertificate")
    public ResponseEntity<Void> createCertificate(@RequestBody CertificateDTO certificateDTO) {
        try {
            certificateService.createCertificate(certificateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/submitCertificateRequest")
    public ResponseEntity<Void> createCertificateRequest(@RequestBody RequestDTO requestDTO) {
        certificateService.addCertificateCreationRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}


