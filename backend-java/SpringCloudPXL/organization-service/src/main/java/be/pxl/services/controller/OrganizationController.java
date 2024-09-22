package be.pxl.services.controller;

import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationById(id));
    }

    @PostMapping
    public ResponseEntity<Long> addOrganization(@RequestBody OrganizationRequest organizationRequest) {
        Long id = organizationService.addOrganization(organizationRequest);
        return ResponseEntity.created(URI.create("/api/organization/" + id)).build();
    }

    @GetMapping("/{id}/with-departments")
    public ResponseEntity<OrganizationResponse>  findByIdWithDepartments(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationByIdWithDepartments(id));
    }

    @GetMapping("/{id}/with-departments-and-employees")
    public ResponseEntity<OrganizationResponse>  findByIdWithDepartmentsAndEmployees(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationByIdWithDepartmentsAndEmployees(id));
    }

    @GetMapping("/{id}/with-employees")
    public ResponseEntity<OrganizationResponse> findByIdWithEmployees(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationByIdWithEmployees(id));
    }
}
