package be.pxl.services;


import be.pxl.services.domain.Organization;
import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.exception.OrganizationNotFoundException;
import be.pxl.services.repository.OrganizationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrganizationServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class OrganizationTest {


    @BeforeEach
    public void clearRepository() {
        organizationRepository.deleteAll();
    }


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.data.datasource.username", sqlContainer::getUsername);
        registry.add("spring.data.datasource.password", sqlContainer::getPassword);
    }


    @Test
    public void testFindById() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Hasselt")
                .build();

        Long id = organizationRepository.save(organization).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PXL"))
                .andExpect(jsonPath("$.address").value("Hasselt"));

        assertEquals(1, organizationRepository.findAll().size());

    }

    @Test
    public void testAddOrganization() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Hasselt")
                .build();

        String organizationString = objectMapper.writeValueAsString(organization);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(organizationString))
                .andExpect(status().isCreated());

        assertEquals(1, organizationRepository.findAll().size());
    }

    @Test
    public void testDeleteOrganizationById() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Hasselt")
                .build();

        Long organizationId = organizationRepository.save(organization).getId();

        assertEquals(1, organizationRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/organization/{id}", organizationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(0, organizationRepository.findAll().size());
    }

    @Test
    public void testUpdateOrganizationById() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Hasselt")
                .build();

        OrganizationRequest organizationRequest = OrganizationRequest.builder()
                .name("UCLL")
                .address("Leuven")
                .build();

        Long organizationId = organizationRepository.save(organization).getId();

        String requestContent = objectMapper.writeValueAsString(organizationRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/organization/{id}", organizationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isAccepted());

        Organization organizationToCheck = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + organizationId + " cannot be found"));
        assertEquals("UCLL", organizationToCheck.getName());
        assertEquals("Leuven", organizationToCheck.getAddress());

    }

    // TODO: Test hier nog bijschrijven
    @Test
    public void testFindByIdWithDepartments() {

    }

    // TODO: Test hier nog bijschrijven
    @Test
    public void testFindByIdWithDepartmentsAndEmployees() {

    }

    // TODO: Test hier nog bijschrijven
    @Test
    public void testFindByIdWithEmployees() {

    }
}