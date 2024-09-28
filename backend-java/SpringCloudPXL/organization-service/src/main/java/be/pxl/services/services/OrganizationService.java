package be.pxl.services.services;

import be.pxl.services.domain.Organization;
import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.exception.OrganizationNotFoundException;
import be.pxl.services.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;

    private static final Logger LOGGER = LogManager.getLogger(OrganizationService.class);

    @Override
    public OrganizationResponse findOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        LOGGER.info(organization.getEmployees());
        LOGGER.info(organization.getDepartments());
        return mapOrganizationToDto(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartments(Long id) {
        Organization organization = organizationRepository.findById(id).filter(org -> !org.getDepartments().isEmpty())
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        return mapOrganizationToDto(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long id) {
        Organization organization = organizationRepository.findById(id)
                .filter(org -> !org.getEmployees().isEmpty() && !org.getDepartments().isEmpty())
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));

        return mapOrganizationToDto(organization);
    }


    @Override
    public OrganizationResponse findOrganizationByIdWithEmployees(Long id) {
        Organization organization = organizationRepository.findById(id).filter(org -> !org.getEmployees().isEmpty())
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        return mapOrganizationToDto(organization);
    }

    @Override
    public void deleteOrganizationById(Long id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public void updateOrganizationById(Long id, OrganizationRequest organizationRequest) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException("Organization with id" + id + " cannot be found"));
        organization.setName(organizationRequest.getName());
        organization.setEmployees(organizationRequest.getEmployees());
        organization.setAddress(organizationRequest.getAddress());
        organization.setDepartments(organizationRequest.getDepartments());

        organizationRepository.save(organization);
    }

    private OrganizationResponse mapOrganizationToDto(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .employees(organization.getEmployees())
                .departments(organization.getDepartments())
                .build();
    }

    public Long addOrganization(OrganizationRequest organizationRequest) {
        Organization newOrganization = Organization.builder()
                .name(organizationRequest.getName())
                .address(organizationRequest.getAddress())
                .departments(organizationRequest.getDepartments())
                .employees(organizationRequest.getEmployees())
                .build();

        LOGGER.info(newOrganization.getDepartments());
        LOGGER.info(newOrganization.getEmployees());

        return organizationRepository.save(newOrganization).getId();
    }
}
