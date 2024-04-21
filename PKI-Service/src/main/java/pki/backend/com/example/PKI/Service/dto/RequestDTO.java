package pki.backend.com.example.PKI.Service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pki.backend.com.example.PKI.Service.model.Request;

@Getter
@Setter
@NoArgsConstructor
public class RequestDTO {
    private String email;
    private String organisation;
    private String organisationUnit;
    private String country;
    private boolean isCA;
    private boolean isDS;
    private boolean isKE;
    private boolean isKCS;
    private boolean isCRLS;

    public RequestDTO(Request request){
        this.email = request.getEmail();
        this.organisation = request.getOrganisation();
        this.organisationUnit = request.getOrganisationUnit();
        this.country = request.getCountry();
        this.isCA = request.isCA();
        this.isDS = request.isDS();
        this.isKE = request.isKE();
        this.isKCS = request.isKCS();
        this.isCRLS = request.isCRLS();
    }

}
