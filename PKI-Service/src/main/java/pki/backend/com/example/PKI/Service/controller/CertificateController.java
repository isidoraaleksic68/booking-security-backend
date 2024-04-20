package pki.backend.com.example.PKI.Service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pki.backend.com.example.PKI.Service.service.CertificateService;
import pki.backend.com.example.PKI.Service.service.KeyStoreService;

import java.io.IOException;

public class CertificateController {

    CertificateService certificateService = new CertificateService();
    KeyStoreService keyStoreService = new KeyStoreService();
    public static String KEYSTORE_PATH = "";


    //TODO: getAll()
    // revoke()
    // download()
    // getAllUserCertificates() --> userAlias
    // generateCertificate() ---> iCA, EE


    @GetMapping("/revoke/{userAlias}/{serialNumber}")
    public ResponseEntity revokeCertificate(@PathVariable String userAlias, @PathVariable String serialNumber) {
        String keyStorePassword = null;
        try {
            keyStorePassword = keyStoreService.getKeyStorePassword(userAlias);
            certificateService.revokeCertificate(KEYSTORE_PATH, serialNumber, keyStorePassword);
            return new ResponseEntity(HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }



}
