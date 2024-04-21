//package pki.backend.com.example.PKI.Service.model;
//
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import java.util.ArrayList;
//
//@Entity
//@Table(name = "users")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
//@Getter
//@Setter
//@NoArgsConstructor
//public class User {
//
//    @Id
//    @Column(name = "id")
//    private String id;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "commonName", unique = false)
//    private String commonName;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "surname", unique = false)
//    private String surname;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "givenName", unique = false)
//    private String givenName;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "organization", unique = false)
//    private String organization;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "organizationUnit", unique = false)
//    private String organizationUnit;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "country", unique = false)
//    private String country;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "email", unique = true)
//    private String email;
//
//    @NotEmpty
//    @NotNull
//    @Column(name = "password", unique = true)
//    private String password;
//
//    @Column(name = "certificatesSerialNumbers", unique = false)
//    @ElementCollection
//    private ArrayList<String> certificatesSerialNumbers;
//
//    public User(String id, String commonName, String surname, String givenName, String organization, String organizationUnit,
//                String country, String email, String password, ArrayList<String> certificatesSerialNumbers) {
//        this.id = id;
//        this.commonName = commonName;
//        this.surname = surname;
//        this.givenName = givenName;
//        this.organization = organization;
//        this.organizationUnit = organizationUnit;
//        this.country = country;
//        this.email = email;
//        this.password = password;
//        this.certificatesSerialNumbers = certificatesSerialNumbers;
//    }
//
//}
//
