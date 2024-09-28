package be.pxl.services;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.exceptions.DepartmentNotFoundException;
import be.pxl.services.repository.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = DepartmentServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class DepartmentTest {


    @BeforeEach
    public void clearRepository() {
        departmentRepository.deleteAll();
    }


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

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
    public void testGetDepartments() throws Exception {
        Department department1 = Department.builder()
                .name("Teachers")
                .position("Somewhere")
                .organizationId(1L)
                .build();

        Department department2 = Department.builder()
                .name("Students")
                .position("Something")
                .organizationId(1L)
                .build();

        departmentRepository.save(department1);
        departmentRepository.save(department2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Teachers"))
                .andExpect(jsonPath("$[1].name").value("Students"))
                .andExpect(jsonPath("$[0].position").value("Somewhere"))
                .andExpect(jsonPath("$[1].position").value("Something"))
                .andExpect(jsonPath("$[0].organizationId").value(1L))
                .andExpect(jsonPath("$[1].organizationId").value(1L));

        assertEquals(2, departmentRepository.findAll().size());
    }

    @Test
    public void testFindById() throws Exception {
        Department department = Department.builder()
                .name("Teachers")
                .position("Somewhere")
                .organizationId(1L)
                .build();

        Long id = departmentRepository.save(department).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Teachers"))
                .andExpect(jsonPath("$.position").value("Somewhere"))
                .andExpect(jsonPath("$.organizationId").value(1L));

        assertEquals(1, departmentRepository.findAll().size());

    }

    @Test
    public void testAddDepartment() throws Exception {
        Department department = Department.builder()
                .name("Teachers")
                .position("Somewhere")
                .organizationId(1L)
                .build();

        String departmentString = objectMapper.writeValueAsString(department);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentString))
                .andExpect(status().isCreated());

        assertEquals(1, departmentRepository.findAll().size());
    }

    @Test
    public void testFindByOrganizationId() throws Exception {
        Department department1 = Department.builder()
                .name("Teachers")
                .position("Somewhere")
                .organizationId(1L)
                .build();

        Department department2 = Department.builder()
                .name("Students")
                .position("Something")
                .organizationId(1L)
                .build();

        departmentRepository.save(department1);
        departmentRepository.save(department2);

        Long organizationId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/organization/{organizationId}", organizationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Teachers"))
                .andExpect(jsonPath("$[1].name").value("Students"));

        assertEquals(2, departmentRepository.findAll().size());
    }


    // Currently cannot test this
    @Test
    public void testFindOrganizationWithEmployees() {

    }

    @Test
    public void testUpdateDepartmentById() throws Exception {
        Department department = Department.builder()
                .name("Teachers")
                .position("Somewhere")
                .organizationId(1L)
                .build();

        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("Students")
                .position("France")
                .build();

        Long departmentId = departmentRepository.save(department).getId();

        String requestContent = objectMapper.writeValueAsString(departmentRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/department/{departmentId}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isAccepted());

        Department departmentToCheck = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException("Department with id " + departmentId + " cannot be found"));
        assertEquals("Students", departmentToCheck.getName());
        assertEquals("France", departmentToCheck.getPosition());
    }

    @Test
    public void testDeleteDepartmentById() throws Exception {
        Department department = Department.builder()
                .name("Teachers")
                .position("Somewhere")
                .organizationId(1L)
                .build();

        Long departmentId = departmentRepository.save(department).getId();

        assertEquals(1, departmentRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/department/{departmentId}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(0, departmentRepository.findAll().size());
    }
}
