package be.pxl.services.services;

import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;

import java.util.List;

public interface IOrganizationService {

    OrganizationResponse findOrganizationById(Long id);

    OrganizationResponse findOrganizationByIdWithDepartments(Long id);

    OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long id);

    Long addOrganization(OrganizationRequest organizationRequest);

    OrganizationResponse findOrganizationByIdWithEmployees(Long id);

    void deleteOrganizationById(Long id);

    void updateOrganizationById(Long id, OrganizationRequest organizationRequest);

    List<OrganizationResponse> findAllOrganizations();
}
