package be.pxl.services.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.pxl.services.domain.Employee;
import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.domain.dto.EmployeeResponse;
import be.pxl.services.exceptions.EmployeeNotFoundException;
import be.pxl.services.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private static final Logger LOGGER = LogManager.getLogger(EmployeeService.class);
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::mapEmployeeToDto).toList();
    }

    private EmployeeResponse mapEmployeeToDto(Employee employee) {
        return EmployeeResponse.builder()
                .age(employee.getAge())
                .name(employee.getName())
                .departmentId(employee.getDepartmentId())
                .organizationId(employee.getOrganizationId())
                .position(employee.getPosition())
                .build();
    }

    // TODO: Kleine bug, de ID die teruggestuurd wordt is altijd 1, maar wordt wel altijd goed opgeslagen in de database.
    @Override
    public Long addEmployee(EmployeeRequest employeeRequest) {
        Employee newEmployee = Employee.builder()
                .age(employeeRequest.getAge())
                .name(employeeRequest.getName())
                .organizationId(employeeRequest.getOrganizationId())
                .departmentId(employeeRequest.getDepartmentId())
                .position(employeeRequest.getPosition())
                .build();

        Employee savedEmployee = employeeRepository.save(newEmployee);
        return savedEmployee.getId();
    }

    public EmployeeResponse findEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            return mapEmployeeToDto(employee);
        } else {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
    }

    @Override
    public List<EmployeeResponse> findEmployeesByDepartment(Long departmentId) {
        List<Employee> listOfEmployees = employeeRepository.findByDepartmentId(departmentId);
        return listOfEmployees.stream().map(this::mapEmployeeToDto).toList();
    }

    @Override
    public List<EmployeeResponse> findEmployeesByOrganization(Long organizationId) {
        List<Employee> listOfEmployees = employeeRepository.findByOrganizationId(organizationId);
        return listOfEmployees.stream().map(this::mapEmployeeToDto).toList();
    }

    @Override
    public void UpdateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee employeeToUpdate = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
        employeeToUpdate.setName(employeeRequest.getName());
        employeeToUpdate.setAge(employeeRequest.getAge());
        employeeToUpdate.setPosition(employeeRequest.getPosition());
        employeeToUpdate.setDepartmentId(employeeRequest.getDepartmentId());
        employeeToUpdate.setOrganizationId(employeeRequest.getOrganizationId());

        employeeRepository.save(employeeToUpdate);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        Employee employeeToDelete = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
        employeeRepository.delete(employeeToDelete);
    }
}
