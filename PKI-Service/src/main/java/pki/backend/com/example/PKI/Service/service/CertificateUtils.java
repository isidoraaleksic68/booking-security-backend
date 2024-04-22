package pki.backend.com.example.PKI.Service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import com.fasterxml.jackson.core.type.TypeReference;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

@NoArgsConstructor
public class CertificateUtils {

    //unique alias generator for certificates
    public static String generateAlias(String companyName) {
        UUID uuid = UUID.randomUUID();
        return companyName.trim().replaceAll("\\s+", "").concat("-").concat(uuid.toString().replace("-", ""));
    }

    public static byte[] booleanToByteArray(boolean value) {
        return value ? new byte[]{1} : new byte[]{0};
    }

    public static boolean byteArrayToBoolean(byte[] bytes) {
        if (bytes.length == 1) {
            return bytes[0] != 0;
        }
        throw new IllegalArgumentException("Byte array must have length 1");
    }

    public static boolean isExpired(X509Certificate certificate){
        Date now = new Date();
        return certificate.getNotAfter().before(now);
    }

    public static boolean datesInRangeOfIssuer(Date newStart, Date newEnd, X509Certificate issuer){
        return newStart.after(issuer.getNotBefore()) && issuer.getNotAfter().after(newEnd);
    }

    public static X500Name createPerson(String commonName, String name, String lastName, String organisation, String organisationUnit,
                                 String country){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, commonName);
        builder.addRDN(BCStyle.SURNAME, lastName);
        builder.addRDN(BCStyle.GIVENNAME, name);
        builder.addRDN(BCStyle.O, organisation);
        builder.addRDN(BCStyle.OU, organisationUnit);
        builder.addRDN(BCStyle.C, country);
        return builder.build();
    }

    public static X500Name createOrganisation(String commonName, String organisation, String organisationUnit,
                                 String country){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, commonName);
        builder.addRDN(BCStyle.O, organisation);
        builder.addRDN(BCStyle.OU, organisationUnit);
        builder.addRDN(BCStyle.C, country);
        return builder.build();
    }

}
