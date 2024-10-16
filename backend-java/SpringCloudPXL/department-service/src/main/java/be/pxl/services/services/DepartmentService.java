package be.pxl.services.services;

import be.pxl.services.client.EmployeeClient;
import be.pxl.services.domain.Department;
import be.pxl.services.domain.Employee;
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

    private final EmployeeClient employeeClient;

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll().stream().toList();
        for (Department department : departments) {
            List<Employee> listOfEmployees = employeeClient.getEmployeesByDepartmentId(department.getId());
            department.setEmployees(listOfEmployees);
        }
        return departments.stream().map(this::mapDepartmentToDto).toList();
    }

    private DepartmentResponse mapDepartmentToDto(Department department) {
        return DepartmentResponse.builder()
                .organizationId(department.getOrganizationId())
                .name(department.getName())
                .employees(department.getEmployees())
                .position(department.getPosition())
                .build();
    }

    @Override
    public Long addDepartment(DepartmentRequest departmentRequest) {
        Department newDepartment = Department.builder()
                .organizationId(departmentRequest.getOrganizationId())
                .name(departmentRequest.getName())
                .employees(departmentRequest.getEmployees())
                .position(departmentRequest.getPosition())
                .build();

        return departmentRepository.save(newDepartment).getId();
    }

    @Override
    public DepartmentResponse findDepartmentById(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);

        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            department.setEmployees(employeeClient.getEmployeesByDepartmentId(id));
            return mapDepartmentToDto(department);
        } else {
            throw new DepartmentNotFoundException("Department with id " + id + " not found");
        }
    }

    @Override
    public List<DepartmentResponse> findDepartmentsByOrganizationId(Long organizationId) {
        List<Department> listOfDepartments = departmentRepository.findByOrganizationId(organizationId);
        for (Department department : listOfDepartments) {
            department.setEmployees(employeeClient.getEmployeesByDepartmentId(organizationId));
        }
        return listOfDepartments.stream().map(this::mapDepartmentToDto).toList();
    }

    @Override
    public List<DepartmentResponse> findByOrganizationWithEmployees(Long organizationId) {
        List<Department> listOfDepartments = departmentRepository.findByOrganizationId(organizationId);
        List<Department> departments = listOfDepartments.stream().filter(department -> !department.getEmployees().isEmpty()).toList();
        for (Department department : departments) {
            department.setEmployees(employeeClient.getEmployeesByDepartmentId(department.getId()));
        }
        return listOfDepartments.stream().map(this::mapDepartmentToDto).toList();
    }

    @Override
    public void updateDepartmentById(Long departmentId, DepartmentRequest departmentRequest) {
        Department departmentToUpdate = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException("Department with id " + departmentId + " could not be found"));
        departmentToUpdate.setEmployees(departmentRequest.getEmployees());
        departmentToUpdate.setName(departmentRequest.getName());
        departmentToUpdate.setPosition(departmentRequest.getPosition());
        departmentToUpdate.setOrganizationId(departmentRequest.getOrganizationId());

        departmentRepository.save(departmentToUpdate);
    }

    @Override
    public void deleteDepartmentById(Long departmentId) {
        Department departmentToDelete = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException("Department with id " + departmentId + " could not be found"));
        departmentRepository.delete(departmentToDelete);
    }
}
