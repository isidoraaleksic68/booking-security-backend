package pki.backend.com.example.PKI.Service.dto;

public class UserDTO {
    public String commonName;
    public String surname;
    public String givenName;
    public String organization;
    public String organizationUnit;
    public String country;
    public String email;
    public String password;

    public UserDTO() {

    }
    public UserDTO(String commonName, String surname, String givenName, String organization,
                   String organizationUnit, String country, String email, String password) {
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.email = email;
        this.password = password;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
