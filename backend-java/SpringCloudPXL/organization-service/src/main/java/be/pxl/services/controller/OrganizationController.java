package be.pxl.services.controller;

import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@RequestBody Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationById(id));
    }

    @GetMapping("/{id}/with-departments")
    public ResponseEntity<OrganizationResponse>  findByIdWithDepartments(@RequestBody Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationByIdWithDepartments(id));
    }

    @GetMapping("/{id}/with-departments-and-employees")
    public ResponseEntity<OrganizationResponse>  findByIdWithDepartmentsAndEmployees(@RequestBody Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationByIdWithDepartmentsAndEmployees(id));
    }

    @GetMapping("/{id}/with-employees")
    public ResponseEntity<OrganizationResponse>  findByIdWithEmployees(@RequestBody Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationByIdWithEmployees(id));
    }
}
