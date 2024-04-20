package pki.backend.com.example.PKI.Service.service;

import org.javatuples.Pair;
import pki.backend.com.example.PKI.Service.keystore.KeyStoreReader;
import pki.backend.com.example.PKI.Service.keystore.KeyStoreWriter;

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

public class KeyStoreService {

    //TODO: putanja do keyStore-a
    // Kreirati .pem koji za CertAlias cuva PrivateKey+KeyPass,
    // gde ako ima credential-s dobija pristup KeyStore-u (ACL impl)
    // S tim da s obzirom da se svi keyStore-ovi cuvaju na istom mestu, keyPass je u principu isti za sve
    private final String KEYSTORE_PATH = "src/main/resources/static/RootKeyStore.jks";

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




}


