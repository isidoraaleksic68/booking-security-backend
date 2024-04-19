package pki.backend.com.example.PKI.Service.service;

import pki.backend.com.example.PKI.Service.keystore.KeyStoreReader;
import pki.backend.com.example.PKI.Service.keystore.KeyStoreWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class KeyStoreService {

    //TODO: putanja do keyStore-a
    // Kreirati .pem koji za CertAlias cuva PrivateKey+KeyPass,
    // gde ako ima credential-s dobija pristup KeyStore-u (ACL impl)
    // S tim da s obzirom da se svi keyStore-ovi cuvaju na istom mestu, keyPass je u principu isti za sve
    private final String KEYSTORE_PATH = "src/main/resources/static/RootKeyStore.jks";
    private final String PEM_FILE_PATH = "src/main/resources/static/user_credentials.pem";

    private KeyStoreWriter keyStoreWriter;
    private KeyStoreReader keyStoreReader;

    public KeyStoreService() {}

    //TODO: IZMENI WRITE FUNKCIJU, NEMA CUVANJA PRIVATE KEY-A I KEYSTORE PASSWORDA U ISTOM FAJLU!
    public void saveCertificate(String alias, PrivateKey privateKey, Certificate certificate){
        String KEYSTORE_PASSWORD=getKeyStorePassword(alias);

        keyStoreWriter.loadKeyStore(KEYSTORE_PATH, KEYSTORE_PASSWORD.toCharArray());
        keyStoreWriter.write(alias, privateKey, KEYSTORE_PASSWORD.toCharArray(), new Certificate[] { certificate } );
        keyStoreWriter.saveKeyStore(KEYSTORE_PATH, KEYSTORE_PASSWORD.toCharArray());

    }

    //TODO: SAVE TO .JKS! [chain of responsibility, alias, cert]

    //GET CERT na osnovu user alias-a
    public Certificate getCertificate(String alias){
        return keyStoreReader.readCertificate(KEYSTORE_PATH, getKeyStorePassword(alias), alias);
    }

    //TODO: GET KeyStorePass na osnovu alias-a

    public String getKeyStorePassword(String alias){
        String keyPass="";
        return keyPass;
    }

    //TODO: GET PrivateKey na osnovu alias-a
    public String getPrivateKey(String alias){
        String privateKey="";
        return privateKey;
    }

    //TODO: Provera da li user ima prava pristupa --> ako njegov alias, se poklapa sa aliasom u pem-u da

    public Boolean doesUserHaveRights(String alias){
        return false;
    }

    //TODO:
    // Nakon toga se dobavlja keyStorePassword za dati alias,
    // kako bi se pristupilo keyPass-u i
    // iscitao sertifikat tj. pozvala getCertificate funkcija

    public Certificate getUserCertificate(String alias){

        if(doesUserHaveRights(alias)){
            return getCertificate(alias);
        }
        return null;
    }


    //TODO: Cuvanje kredencijala u .pem-u
    // Save private key and KeyStore password to PEM file
    public void saveCredentials(String alias, PrivateKey privateKey, String keyStorePassword) throws IOException {
        // Encrypt privateKey and keyStorePassword before saving to PEM file
        String encryptedPrivateKey = encrypt(privateKey);
        String encryptedKeyStorePassword = encrypt(keyStorePassword);

        // Save alias, encryptedPrivateKey, and encryptedKeyStorePassword to PEM file
        String pemContent = alias + ":" + encryptedPrivateKey + ":" + encryptedKeyStorePassword;
        try (FileOutputStream fos = new FileOutputStream(PEM_FILE_PATH)) {
            fos.write(pemContent.getBytes());
        }
    }

    // Encrypt data (private key and password)
    private String encrypt(Object data) {
        // Implement encryption logic
        // For now, return a placeholder
        return "encrypted_" + data.toString();
    }

    // Decrypt data (private key and password)
    private <T> T decrypt(String encryptedData) {
        // Implement decryption logic
        // For now, return a placeholder
        return (T) encryptedData.replace("encrypted_", "");
    }

    // Load content of PEM file
    private String loadPemFileContent() throws IOException {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(PEM_FILE_PATH)) {
            int data;
            while ((data = fis.read()) != -1) {
                content.append((char) data);
            }
        }
        return content.toString();
    }


}


