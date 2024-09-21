package be.pxl.services.services;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import be.pxl.services.exceptions.DepartmentNotFoundException;
import be.pxl.services.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {

    private final DepartmentRepository departmentRepository;
    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::mapDepartmentToDto).toList();
    }

    private DepartmentResponse mapDepartmentToDto(Department department) {
        return DepartmentResponse.builder()
                .organizationId(department.getOrganizationId())
                .name(department.getName())
                // .employees(department.getEmployees())
                .position(department.getPosition())
                .build();
    }

    @Override
    public Long addDepartment(DepartmentRequest departmentRequest) {
        Department newDepartment = Department.builder()
                .organizationId(departmentRequest.getOrganizationId())
                .name(departmentRequest.getName())
                // .employees(departmentRequest.getEmployees())
                .position(departmentRequest.getPosition())
                .build();

        return departmentRepository.save(newDepartment).getId();
    }

    @Override
    public DepartmentResponse findDepartmentById(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);

        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            return mapDepartmentToDto(department);
        } else {
            throw new DepartmentNotFoundException("Department with id " + id + " not found");
        }
    }

    @Override
    public List<DepartmentResponse> findDepartmentsByOrganizationId(Long organizationId) {
        List<Department> listOfDepartments = departmentRepository.findByOrganizationId(organizationId);
        return listOfDepartments.stream().map(this::mapDepartmentToDto).toList();
    }

    @Override
    public List<DepartmentResponse> findByOrganizationWithEmployees(Long organizationId) {
        // List<Department> listOfDepartments = departmentRepository.findByOrganizationId(organizationId);
        // return listOfDepartments.stream().filter(department -> !department.getEmployees().isEmpty()).map(this::mapDepartmentToDto).toList();
        return null;
    }
}
