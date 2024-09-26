package be.pxl.services.services;

import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.domain.dto.EmployeeResponse;

import java.util.List;

public interface IEmployeeService {

    List<EmployeeResponse> getAllEmployees();

    Long addEmployee(EmployeeRequest employeeRequest);

    EmployeeResponse findEmployeeById(Long id);

    List<EmployeeResponse> findEmployeesByDepartment(Long departmentId);

    List<EmployeeResponse> findEmployeesByOrganization(Long organizationId);

    void UpdateEmployee(Long id, EmployeeRequest employeeRequest);

    void deleteEmployeeById(Long id);
}
