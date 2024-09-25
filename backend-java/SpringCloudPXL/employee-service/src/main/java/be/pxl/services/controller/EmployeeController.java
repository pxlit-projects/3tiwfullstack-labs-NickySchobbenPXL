package be.pxl.services.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.domain.dto.EmployeeResponse;
import be.pxl.services.services.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;
    private static final Logger LOGGER = LogManager.getLogger(EmployeeController.class);

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<Long> addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Long id = employeeService.addEmployee(employeeRequest);
        LOGGER.info("Employee created with ID: {}", id);
        return ResponseEntity.created(URI.create("/employee/" + id)).build();
    }


    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<EmployeeResponse>> findByOrganizationId(@PathVariable Long organizationId) {
        return ResponseEntity.ok(employeeService.findEmployeesByOrganization(organizationId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.findEmployeesByDepartment(departmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        employeeService.UpdateEmployee(id, employeeRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
 }
