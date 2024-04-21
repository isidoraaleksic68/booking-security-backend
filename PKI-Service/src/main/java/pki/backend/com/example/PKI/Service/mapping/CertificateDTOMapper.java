package pki.backend.com.example.PKI.Service.mapping;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pki.backend.com.example.PKI.Service.dto.CertificateDTO;
import pki.backend.com.example.PKI.Service.model.Certificate;

@Component
public class CertificateDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public CertificateDTOMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }
    public static Certificate fromDTOToCertificate (CertificateDTO dto)
    {
        return modelMapper.map(dto, Certificate.class);
    }

    public static CertificateDTO fromCertificateToDTO(Certificate model)
    {
        return modelMapper.map(model, CertificateDTO.class);
    }



}
