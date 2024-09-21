package be.pxl.services.service;

import be.pxl.services.domain.Organization;
import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.exception.OrganizationNotFoundException;
import be.pxl.services.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponse findOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        return mapOrganizationToDto(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartments(Long id) {
        return null;
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long id) {
        return null;
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithEmployees(Long id) {
        return null;
    }

    private OrganizationResponse mapOrganizationToDto(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .build();
    }
}
