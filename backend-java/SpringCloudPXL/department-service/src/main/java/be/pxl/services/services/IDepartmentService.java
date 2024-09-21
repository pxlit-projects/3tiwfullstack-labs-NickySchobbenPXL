package be.pxl.services.services;

import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import java.util.List;

public interface IDepartmentService {

    List<DepartmentResponse> getAllDepartments();

    Long addDepartment(DepartmentRequest departmentRequest);

    DepartmentResponse findDepartmentById(Long id);

    List<DepartmentResponse> findDepartmentsByOrganizationId(Long organizationId);

    List<DepartmentResponse> findByOrganizationWithEmployees(Long organizationId);
}
