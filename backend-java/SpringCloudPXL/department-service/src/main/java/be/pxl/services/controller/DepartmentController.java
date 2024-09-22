package be.pxl.services.controller;

import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import be.pxl.services.services.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;
    private static final Logger LOGGER = LogManager.getLogger(DepartmentController.class);

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findDepartmentById(id));
    }


    @PostMapping
    public ResponseEntity<Long> addDepartment(@RequestBody DepartmentRequest departmentRequest) {
        Long id = departmentService.addDepartment(departmentRequest);
        return ResponseEntity.created(URI.create("/department/" + id)).build();
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DepartmentResponse>> findByOrganizationId(@PathVariable Long organizationId) {
        return ResponseEntity.ok(departmentService.findDepartmentsByOrganizationId(organizationId));
    }

    @GetMapping("/organization/{organizationId}/with-employees")
    public ResponseEntity<List<DepartmentResponse>> findOrganizationWithEmployees(@PathVariable Long organizationId) {
        return ResponseEntity.ok(departmentService.findByOrganizationWithEmployees(organizationId));

    }
}