package pki.backend.com.example.PKI.Service.service;

import pki.backend.com.example.PKI.Service.keystore.KeyStoreReader;

import pki.backend.com.example.PKI.Service.keystore.KeyStoreWriter;
import pki.backend.com.example.PKI.Service.model.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class CertificateService {

    //NOTE: getUser i getAll metode dodate u READER KLASU!
    KeyStoreReader kSReader;
    KeyStoreWriter kSWriter;


    //TODO: Funkcije za kreirati
    // createRootCert()
    // createIntermediate
    // createEECertificate()
    // isCertificateValid()
    // generateKeyPair()


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

    public Certificate convertX509toCertClass(X509Certificate x509Certificate){


        // Create a new Certificate object with the information from the X509Certificate
        Certificate certificate = new Certificate(
                x509Certificate.getSubjectDN().getName(),
                x509Certificate.getIssuerDN().getName(),
                x509Certificate.getSerialNumber(),
                x509Certificate.getNotBefore(),
                x509Certificate.getNotAfter(),
                false, // Set the revoked field to false by default
                x509Certificate
        );

        return certificate;
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
