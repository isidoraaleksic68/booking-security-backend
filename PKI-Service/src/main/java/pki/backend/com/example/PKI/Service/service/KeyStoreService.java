package pki.backend.com.example.PKI.Service.service;

import org.javatuples.Pair;
import pki.backend.com.example.PKI.Service.keystore.KeyStoreReader;
import pki.backend.com.example.PKI.Service.keystore.KeyStoreWriter;
import pki.backend.com.example.PKI.Service.model.CertificateGenerator;
import pki.backend.com.example.PKI.Service.model.Issuer;
import pki.backend.com.example.PKI.Service.model.Subject;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.security.cert.X509Certificate;
import java.util.Date;

public class KeyStoreService {

    //TODO: putanja do keyStore-a
    // Kreirati .pem koji za CertAlias cuva PrivateKey+KeyPass,
    // gde ako ima credential-s dobija pristup KeyStore-u (ACL impl)
    // S tim da s obzirom da se svi keyStore-ovi cuvaju na istom mestu, keyPass je u principu isti za sve
    private final String KEYSTORE_PATH = "src/main/resources/static/RootKeyStore.jks";



    private KeyStoreWriter keyStoreWriter;
    private KeyStoreReader keyStoreReader;
    private CertificateGenerator certificateGenerator;


    public KeyStoreService() {
        this.certificateGenerator=new CertificateGenerator();
    }

    //TODO: IZMENI WRITE FUNKCIJU, NEMA CUVANJA PRIVATE KEY-A I KEYSTORE PASSWORDA U ISTOM FAJLU!
    public void saveCertificateToJKS(String alias, X509Certificate certificate) throws Exception {

        String keyStorePassword = getKeyStorePassword(alias);

        keyStoreWriter.loadKeyStore(KEYSTORE_PATH, keyStorePassword.toCharArray());
        keyStoreWriter.writeCertificate(alias, certificate);
        keyStoreWriter.saveKeyStore(KEYSTORE_PATH, keyStorePassword.toCharArray());
    }

    //TODO: SAVE TO .JKS! [chain of responsibility, alias, cert], izmeni da ne radi sa private keyem
    public void saveCertificate(String alias, X509Certificate certificate, String issuerAlias) throws Exception {
        String keyStorePassword = getKeyStorePassword(alias);

        Certificate[] certificatesChain = keyStoreReader.getCertificateChain(KEYSTORE_PATH, keyStorePassword, issuerAlias);
        Certificate[] newCertificateChain;

        if (certificatesChain.length == 0) {
            X509Certificate rootCertificate =(X509Certificate) keyStoreReader.readCertificate(KEYSTORE_PATH, keyStorePassword, issuerAlias);
            if (rootCertificate == null) {
                throw new Exception("Issuer certificate not found in KeyStore");
            }
            newCertificateChain = new Certificate[]{rootCertificate, certificate};
        } else {
            newCertificateChain = new Certificate[certificatesChain.length + 1];
            System.arraycopy(certificatesChain, 0, newCertificateChain, 0, certificatesChain.length);
            newCertificateChain[newCertificateChain.length - 1] = certificate;
        }

        keyStoreWriter.loadKeyStore(KEYSTORE_PATH, keyStorePassword.toCharArray());
        keyStoreWriter.writeCertificateChain(alias, newCertificateChain);
        keyStoreWriter.saveKeyStore(KEYSTORE_PATH, keyStorePassword.toCharArray());
    }

    //GET CERT na osnovu user alias-a
    public Certificate getCertificate(String alias) throws IOException {
        return keyStoreReader.readCertificate(KEYSTORE_PATH, getKeyStorePassword(alias), alias);
    }

    //Kreiraj mi novi sertifikat?????
    public X509Certificate generateCertificate(Subject subject, Issuer issuer, Date startDate, Date endDate, String serialNumber) {
        return certificateGenerator.generateCertificate(subject, issuer, startDate, endDate, serialNumber);
    }


    //TODO: GET KeyStorePass na osnovu alias-a

    // Load KeyStore password from PEM file based on alias
    public String getKeyStorePassword(String alias) throws IOException {
        // Load PEM file content and extract encryptedKeyStorePassword
        String pemContent = loadPemFileContent();
        String[] parts = pemContent.split(":");
        if (parts.length != 3 || !parts[0].equals(alias)) {
            throw new IllegalArgumentException("Invalid credentials for alias: " + alias);
        }
        return decrypt(parts[2]);
    }
    //TODO: GET PrivateKey na osnovu alias-a
    // Load private key from PEM file based on alias
    public PrivateKey getPrivateKey(String alias) throws IOException {
        // Load PEM file content and extract encryptedPrivateKey
        String pemContent = loadPemFileContent();
        String[] parts = pemContent.split(":");
        if (parts.length != 3 || !parts[0].equals(alias)) {
            throw new IllegalArgumentException("Invalid credentials for alias: " + alias);
        }
        String encryptedPrivateKey = parts[1];

        // Decrypt encryptedPrivateKey
        return decrypt(encryptedPrivateKey);
    }
    //TODO: Provera da li user ima prava pristupa --> ako njegov alias, se poklapa sa aliasom u pem-u da

    public Boolean doesUserHaveRights(String alias){
        return false;
    }

    //TODO:
    // Nakon toga se dobavlja keyStorePassword za dati alias,
    // kako bi se pristupilo keyPass-u i
    // iscitao sertifikat tj. pozvala getCertificate funkcija

    public Certificate getUserCertificate(String alias) throws IOException {

        if(doesUserHaveRights(alias)){
            return getCertificate(alias);
        }
        return null;
    }




}


