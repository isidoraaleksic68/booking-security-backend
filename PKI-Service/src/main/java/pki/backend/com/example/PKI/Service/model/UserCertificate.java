package pki.backend.com.example.PKI.Service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCertificate {
    public String alias;
    public boolean isRevoked;
}