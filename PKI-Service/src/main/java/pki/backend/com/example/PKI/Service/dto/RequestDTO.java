package pki.backend.com.example.PKI.Service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
