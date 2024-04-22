package pki.backend.com.example.PKI.Service.keystore;

import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.security.auth.x500.X500Principal;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Date;


@Component
public class KeyStoreWriter {

    private KeyStore keyStore;

    public KeyStoreWriter() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                //Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            try {
                keyStore.load(null, password);
                //saveKeyStore(fileName, password);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (CertificateException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //WARNING: DO NOT USE
//    public void write(String alias, PrivateKey privateKey, char[] password, Certificate[] certificateChain) {
//        try {
//            keyStore.setKeyEntry(alias, privateKey, password, certificateChain);
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //WARNING: DO NOT USE!
//    public void writeCertificateChain(String alias, Certificate[] chain) throws Exception {
//        try {
//            keyStore.setKeyEntry(alias, null, null, chain);
//        } catch (Exception e) {
//            throw new Exception("Failed to write certificate chain to KeyStore", e);
//        }
//    }

    public void writeCertificate(String alias, Certificate certificate) throws Exception {
        try {
            keyStore.setCertificateEntry(alias, certificate);
        } catch (Exception e) {
            throw new Exception("Failed to write certificate to KeyStore", e);
        }
    }

    public String writePrivateKey(String alias, PrivateKey privateKey) throws Exception {
        String newKeyPass = "nov" + LocalDateTime.now().toString() + "key" + alias + "pass";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Kreiraj prazan sertifikat
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X500Principal dnName = new X500Principal("CN=Test Certificate");
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN(dnName);
        certGen.setNotBefore(new Date());
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000)); // 1 year validity
        certGen.setSubjectDN(dnName);
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        // Potpiši sertifikat
        X509Certificate certificate = certGen.generate(keyPair.getPrivate(), "BC");

        Certificate[] temp = new Certificate[1];
        temp[0] = certificate;
        keyStore.setKeyEntry(alias, privateKey, newKeyPass.toCharArray(), temp);
        return newKeyPass;
    }
}