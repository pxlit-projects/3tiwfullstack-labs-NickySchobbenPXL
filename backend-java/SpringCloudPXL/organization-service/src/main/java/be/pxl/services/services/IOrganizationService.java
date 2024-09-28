package be.pxl.services.services;

import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;

public interface IOrganizationService {

    public OrganizationResponse findOrganizationById(Long id);

    public OrganizationResponse findOrganizationByIdWithDepartments(Long id);

    public OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long id);

    public Long addOrganization(OrganizationRequest organizationRequest);

    public OrganizationResponse findOrganizationByIdWithEmployees(Long id);

    public void deleteOrganizationById(Long id);

    public void updateOrganizationById(Long id, OrganizationRequest organizationRequest);
}
