package be.pxl.services.controller;

import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.services.OrganizationService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationById(id));
    }

    @GetMapping()
    public ResponseEntity<List<OrganizationResponse>> findAllOrganizations() {
        return ResponseEntity.ok(organizationService.findAllOrganizations());
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

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrganizationById(@PathVariable Long id) {
        organizationService.deleteOrganizationById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateOrganizationById(@PathVariable Long id, @RequestBody OrganizationRequest organizationRequest) {
        organizationService.updateOrganizationById(id, organizationRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
