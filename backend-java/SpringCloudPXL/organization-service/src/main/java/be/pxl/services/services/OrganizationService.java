package be.pxl.services.services;

import be.pxl.services.client.DepartmentClient;
import be.pxl.services.client.EmployeeClient;
import be.pxl.services.domain.Department;
import be.pxl.services.domain.Employee;
import be.pxl.services.domain.Organization;
import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.exception.OrganizationNotFoundException;
import be.pxl.services.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;
    private final EmployeeClient employeeClient;
    private final DepartmentClient departmentClient;

    private static final Logger LOGGER = LogManager.getLogger(OrganizationService.class);

    @Transactional
    @Override
    public OrganizationResponse findOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        List<Employee> listOfEmployees = employeeClient.getEmployeesByOrganizationId(id);
        List<Department> listOfDepartments = departmentClient.getDepartmentsByOrganizationId(id);
        organization.setEmployees(listOfEmployees);
        organization.setDepartments(listOfDepartments);
        return mapOrganizationToDto(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartments(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        List<Department> listOfDepartments = departmentClient.getDepartmentsByOrganizationId(id);
        if (!listOfDepartments.isEmpty()) {
            List<Employee> listOfEmployees = employeeClient.getEmployeesByOrganizationId(id);
            organization.setDepartments(listOfDepartments);
            organization.setEmployees(listOfEmployees);
            return mapOrganizationToDto(organization);
        }
        return null;
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        List<Department> listOfDepartments = departmentClient.getDepartmentsByOrganizationId(id);
        List<Employee> listOfEmployees = employeeClient.getEmployeesByOrganizationId(id);
        if (listOfEmployees.isEmpty() || listOfDepartments.isEmpty()) {
            return null;
        }
        organization.setEmployees(listOfEmployees);
        organization.setDepartments(listOfDepartments);
        return mapOrganizationToDto(organization);
    }


    @Override
    public OrganizationResponse findOrganizationByIdWithEmployees(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + id + " does not exist"));
        List<Employee> listOfEmployees = employeeClient.getEmployeesByOrganizationId(id);
        if (listOfEmployees.isEmpty()) {
            return null;
        }
        List<Department> listOfDepartments = departmentClient.getDepartmentsByOrganizationId(id);
        organization.setEmployees(listOfEmployees);
        organization.setDepartments(listOfDepartments);
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

    public List<OrganizationResponse> findAllOrganizations() {
        List<Organization> listOfAllOrganizations = organizationRepository.findAll().stream().toList();
        List<OrganizationResponse> listOfOrganizationResponses = new ArrayList<>();
        for (Organization organization : listOfAllOrganizations) {
            Long organizationId = organization.getId();
            List<Employee> listOfAllEmployees = employeeClient.getEmployeesByOrganizationId(organizationId);
            List<Department> listOfAllDepartments = departmentClient.getDepartmentsByOrganizationId(organizationId);
            organization.setEmployees(listOfAllEmployees);
            organization.setDepartments(listOfAllDepartments);
            listOfOrganizationResponses.add(mapOrganizationToDto(organization));
        }
        return listOfOrganizationResponses;
    }
}
