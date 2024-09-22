package be.pxl.services.service;

import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;

import java.util.Optional;

public interface IOrganizationService {

    public OrganizationResponse findOrganizationById(Long id);

    public OrganizationResponse findOrganizationByIdWithDepartments(Long id);

    public OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long id);

    public Long addOrganization(OrganizationRequest organizationRequest);

    public OrganizationResponse findOrganizationByIdWithEmployees(Long id);
}
