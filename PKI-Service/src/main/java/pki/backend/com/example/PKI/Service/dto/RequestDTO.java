package pki.backend.com.example.PKI.Service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pki.backend.com.example.PKI.Service.model.Request;

@Getter
@Setter
@NoArgsConstructor
public class RequestDTO {
    private Long id;
    private String email;
    private String organisation;
    private String organisationUnit;
    private String country;
    private boolean ca;
    private boolean ds;
    private boolean ke;
    private boolean kcs;
    private boolean crls;

    public RequestDTO(Request request){
        this.id = request.getId();
        this.email = request.getEmail();
        this.organisation = request.getOrganisation();
        this.organisationUnit = request.getOrganisationUnit();
        this.country = request.getCountry();
        this.ca = request.isCA();
        this.ds = request.isDS();
        this.ke = request.isKE();
        this.kcs = request.isKCS();
        this.crls = request.isCRLS();
    }

}
