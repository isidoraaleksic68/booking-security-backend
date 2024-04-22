package pki.backend.com.example.PKI.Service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pki.backend.com.example.PKI.Service.dto.CertificateDTO;
import pki.backend.com.example.PKI.Service.dto.RequestDTO;
import pki.backend.com.example.PKI.Service.service.CertificateService;
import pki.backend.com.example.PKI.Service.service.KeyStoreService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    CertificateService certificateService;



    @PostMapping(value = "/create")
    public ResponseEntity<Void> createCertificate(@RequestBody CertificateDTO certificateDTO) {
        try {
            certificateService.createCertificate(certificateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/request/create")
    public ResponseEntity<Void> createCertificateRequest(@RequestBody RequestDTO requestDTO) {
        certificateService.addCertificateCreationRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CertificateDTO>> getAllCertificates() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        List<CertificateDTO> certificateDtos = certificateService.getAllCertificates();

        return new ResponseEntity<>(certificateDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/request/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RequestDTO>> getAllRequests() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        List<RequestDTO> requestDTOS = certificateService.getAllRequests();

        return new ResponseEntity<>(requestDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/createRootCA", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRootCA() throws Exception {

        certificateService.generateRootCertificate();

        return new ResponseEntity<>(HttpStatus.CREATED);
    }





















//    //TODO: IZMENI DA VRACA CERTIFICATE DTO!
//    @GetMapping("/revoke/{userAlias}/{serialNumber}")
//    public ResponseEntity<Void> revokeCertificate(@PathVariable String userAlias, @PathVariable String serialNumber) {
//        //ili alias ili ceo CERTDTO
//        String keyStorePassword = null;
//        try {
//            keyStorePassword = keyStoreService.getKeyStorePassword(userAlias);
//            certificateService.revokeCertificate(KEYSTORE_PATH, serialNumber, keyStorePassword);
//            return new ResponseEntity(HttpStatus.OK);
//
//        } catch (IOException e) {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//
//    }
}


