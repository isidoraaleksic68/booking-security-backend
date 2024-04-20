package pki.backend.com.example.PKI.Service.service;

import pki.backend.com.example.PKI.Service.keystore.KeyStoreReader;
import pki.backend.com.example.PKI.Service.keystore.KeyStoreWriter;
import pki.backend.com.example.PKI.Service.model.CertificateGenerator;
import pki.backend.com.example.PKI.Service.model.Issuer;
import pki.backend.com.example.PKI.Service.model.Subject;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class KeyStoreService {

    private final String BASIC_KEYSTORE_PATH = "src/main/resources/static/RootKeyStore.jks";
    private final String PRIVATE_KEY_KEYSTORE_PATH = "src/main/resources/static/RootKeyStore.jks";

    private KeyStoreWriter keyStoreWriter;
    private KeyStoreReader keyStoreReader;
    private CertificateGenerator certificateGenerator;
    private PEMService pemService;

    public KeyStoreService() {
        this.certificateGenerator=new CertificateGenerator();
    }

    //needs to save 'alias-certificate' in basic KeyStore and 'alias-private key of newly saved certificate'
    //in PK KeyStore [Private Key Key Store]
    public void saveRootCertificate(String alias, X509Certificate certificate, String issuerAlias) throws Exception {
        String basicKeyStorePassword = pemService.getBasicKeyStorePassword();
        String PKKeyStorePassword = pemService.getPrivateKeysKeyStorePassword();

        //write new certificate
        keyStoreWriter.loadKeyStore(BASIC_KEYSTORE_PATH, basicKeyStorePassword.toCharArray());
        keyStoreWriter.writeCertificate(alias, certificate);
        keyStoreWriter.saveKeyStore(BASIC_KEYSTORE_PATH, basicKeyStorePassword.toCharArray());

        //write private key of newly written certificate
        keyStoreWriter.loadKeyStore(PRIVATE_KEY_KEYSTORE_PATH, PKKeyStorePassword.toCharArray());
        String newKeyPass = keyStoreWriter.writePrivateKey(alias, privateKey);
        keyStoreWriter.saveKeyStore(PRIVATE_KEY_KEYSTORE_PATH, PKKeyStorePassword.toCharArray());

        //write KeyPass for private key of newly written certificate
        pemService.writeToPEM(false, alias, newKeyPass, basicKeyStorePassword, PKKeyStorePassword);
    }

    //this one does not need to save private key, because it will generate new pair of keys every time it is needed,
    //because it doesnt sign anything and doesnt store anything permanent that would be encrypted or signed with
    //its keypair
    public void saveEndEntityCertificate(String alias, X509Certificate certificate, String issuerAlias) throws Exception {
        String basicKeyStorePassword = pemService.getBasicKeyStorePassword();
        String PKKeyStorePassword = pemService.getPrivateKeysKeyStorePassword();

        //write new certificate
        keyStoreWriter.loadKeyStore(BASIC_KEYSTORE_PATH, basicKeyStorePassword.toCharArray());
        keyStoreWriter.writeCertificate(alias, certificate);
        keyStoreWriter.saveKeyStore(BASIC_KEYSTORE_PATH, basicKeyStorePassword.toCharArray());
    }

    //get cert based on alias
    public Certificate getCertificate(String alias) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String basicKeyStorePassword = pemService.getBasicKeyStorePassword();
        return keyStoreReader.readCertificate(BASIC_KEYSTORE_PATH, basicKeyStorePassword, alias);
    }

    //gets certificates private key based on alias
    public PrivateKey getPrivateKey(String alias) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String PKKeyStorePassword=  pemService.getPrivateKeysKeyStorePassword();
        String KeyPass = pemService.getKeyPass(alias);

        return keyStoreReader.readPrivateKey(PRIVATE_KEY_KEYSTORE_PATH, PKKeyStorePassword, alias, KeyPass);
    }


    //Kreiraj mi novi sertifikat?????
    //todo: sta sa ovim da radim???
    public X509Certificate generateCertificate(Subject subject, Issuer issuer, Date startDate, Date endDate, String serialNumber) {
        return certificateGenerator.generateCertificate(subject, issuer, startDate, endDate, serialNumber);
    }


}


