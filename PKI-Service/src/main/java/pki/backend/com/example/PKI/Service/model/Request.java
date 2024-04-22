package pki.backend.com.example.PKI.Service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pki.backend.com.example.PKI.Service.dto.RequestDTO;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cert_request_table")
@Getter
@Setter
@NoArgsConstructor
public class Request {
    @Id
    @Column(name = "id")
    private String id;

    @NotEmpty
    @NotNull
    @Column(name = "email", unique = false)
    private String email;

    @NotEmpty
    @NotNull
    @Column(name = "organisation", unique = false)
    private String organisation;

    @NotEmpty
    @NotNull
    @Column(name = "organisationUnit", unique = false)
    private String organisationUnit;

    @NotEmpty
    @NotNull
    @Column(name = "country", unique = false)
    private String country;

    @NotEmpty
    @NotNull
    @Column(name = "isCA", unique = false)
    private boolean isCA;

    @NotEmpty
    @NotNull
    @Column(name = "isDS", unique = false)
    private boolean isDS;

    @NotEmpty
    @NotNull
    @Column(name = "isKE", unique = false)
    private boolean isKE;

    @NotEmpty
    @NotNull
    @Column(name = "isKCS", unique = false)
    private boolean isKCS;

    @NotEmpty
    @NotNull
    @Column(name = "isCRLS", unique = false)
    private boolean isCRLS;

    public Request(RequestDTO dto){
        this.email = dto.getEmail();
        this.organisation = dto.getOrganisation();
        this.organisationUnit = dto.getOrganisationUnit();
        this.country = dto.getCountry();
        this.isCA = dto.isCA();
        this.isDS = dto.isDS();
        this.isKE = dto.isKE();
        this.isKCS = dto.isKCS();
        this.isCRLS = dto.isCRLS();
    }
}
