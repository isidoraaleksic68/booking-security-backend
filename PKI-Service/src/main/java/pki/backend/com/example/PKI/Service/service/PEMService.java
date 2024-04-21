package pki.backend.com.example.PKI.Service.service;

import lombok.NoArgsConstructor;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

@NoArgsConstructor
@Service
public class PEMService {

    private final String PEM_FILE_PATH = "src/main/resources/static/user_credentials.pem";

    public String getPEM_FILE_PATH(){
        return PEM_FILE_PATH;
    }

    //todo:should be set up, in some moment like when creating file, but we should do that later, just call of this func
    public void setACL() throws IOException {

        Path path = Paths.get(PEM_FILE_PATH);

        // Create ACL for 'user'
        UserPrincipal user = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("Intel");
        AclEntry entry = AclEntry.newBuilder()
                .setType(AclEntryType.ALLOW)
                .setPrincipal(user)
                .setPermissions(Set.of(AclEntryPermission.READ_DATA, AclEntryPermission.WRITE_DATA, AclEntryPermission.EXECUTE))
                .build();


        AclFileAttributeView aclView = Files.getFileAttributeView(path, AclFileAttributeView.class);
        //get existing acl entries
        List<AclEntry> aclEntries = aclView.getAcl();

        aclEntries.add(entry);
        aclView.setAcl(aclEntries);
    }

    private void addKeyPassToPEM(String JKSPassBasic, String JKSPassPrivateKeys, List<Pair<String, String>> keyPasses) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, IOException {
        //just helper function for writeToPEM()
        //jks pass 1 i jks pass 2 are for two files that will be made, and KeyPass is password to access
        // single entry inside .jks2
        StringBuilder keyPassesForEncryptionBuilder= new StringBuilder();
        for (Pair<String, String> pair : keyPasses) {
            String alias = pair.getValue0();
            String keyPass = pair.getValue1();
            keyPassesForEncryptionBuilder.append(alias).append(",").append(keyPass).append(";");
        }
        String forEncryption = JKSPassBasic + ":" + JKSPassPrivateKeys + ":"+keyPassesForEncryptionBuilder.toString();

        forEncryption = forEncryption.substring(0, forEncryption.length() - 1);

        String encryptedData = encryptPEM(forEncryption);

        BufferedWriter writer = new BufferedWriter(new FileWriter(PEM_FILE_PATH));
        writer.write(encryptedData);
        System.out.println("Encrypted data successfully written to file.");
    }

    public String[] loadPEM() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        //Note: this function will return on String[2] just one String with all keyPasses combined like:
        // 'alias,keyPass;alias,keyPass;alias,keyPass'
        //Read encrypted data from file
        StringBuilder encryptedDataBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(PEM_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            encryptedDataBuilder.append(line);
        }

        String encryptedData = encryptedDataBuilder.toString();

        // Decrypt the encrypted data
        String decryptedData =  decryptPEM(encryptedData);
        return decryptedData.split(":");
    }

    public void writeToPEM(boolean firstKeyPass, String alias, String newKeyPass, String newKeyPassBasic, String newKeyPassPrivateKeys) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        //this function will write into PEM file, it doesnt matter if it is first entry or n-th entry
        if (firstKeyPass){
            Pair<String, String> pair = new Pair<>(alias, newKeyPass);
            addKeyPassToPEM(newKeyPassBasic, newKeyPassPrivateKeys, List.of(pair));
            return;
        }
        String[] PEMData = loadPEM();
        String KeyStorePassBasic = PEMData[0];
        String KeyStorePassPrivateKeys = PEMData[1];
        List<Pair<String, String>> PEMKeyPasses = convertStringToPairs(PEMData[2]);
        addKeyPassToPEM(KeyStorePassBasic, KeyStorePassPrivateKeys, PEMKeyPasses);
    }

    private static List<Pair<String, String>> convertStringToPairs(String pairString) {
        //will take string that loadPEM() gives and then will turn it into List of pair<alias, KeyPass>
        List<Pair<String, String>> PEMKeyPasses = new ArrayList<>();
        String[] pairs = pairString.split(";");
        for (String pair : pairs) {
            PEMKeyPasses.add(new Pair<>(pair.split(",")[0], pair.split(",")[1]));
        }
        return PEMKeyPasses;
    }

    public String getBasicKeyStorePassword() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String[] PEMData = loadPEM();
        return PEMData[0];
    }

    public String getPrivateKeysKeyStorePassword() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String[] PEMData = loadPEM();
        return PEMData[1];
    }

    // Load KeyPass from PEM file based on alias
    public String getKeyPass(String alias) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        // Load PEM file content and extract encryptedPrivateKey
        String[] PEMContent = loadPEM();
        List<Pair<String, String>> PEMKeyPasses = convertStringToPairs(PEMContent[2]);

        for (Pair<String, String> pair : PEMKeyPasses){
            if (pair.getValue0().equals(alias)){
                return pair.getValue1();
            }
        }

        return null; //there is no KeyPass for given alias.
    }

    private String encryptPEM(String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String SALT = "dusanSaltBae";
        String encryptionKey = "tajnaZaEnkriptovanje";
        // Generate key using SALT and encription key [these two will always be the same so no problem?]
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Create instance of AES algorithm
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encript data
        byte[] encrypted = cipher.doFinal(data.toString().getBytes());

        // Convert to Base64 and return
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decryptPEM(String encryptedData) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String encryptionKey = "tajnaZaEnkriptovanje";
        String SALT = "dusanSaltBae";
        // Decript data using the same AES algorithm
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedDataBytes = cipher.doFinal(encryptedDataBytes);
        return new String(decryptedDataBytes);
    }


}

