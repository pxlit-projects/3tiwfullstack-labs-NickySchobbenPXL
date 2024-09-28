package be.pxl.services;


import be.pxl.services.domain.Employee;
import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.exceptions.EmployeeNotFoundException;
import be.pxl.services.repository.EmployeeRepository;
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

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class EmployeeTest {


    @BeforeEach
    public void clearRepository() {
        employeeRepository.deleteAll();
    }


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

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
    public void testCreateAndAddEmployee() throws Exception {
        Employee employee = Employee.builder()
                .age(33)
                .name("Nicky")
                .position("Student")
                .build();

        String employeeString = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeString))
                .andExpect(status().isCreated());

        assertEquals(1, employeeRepository.findAll().size());
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = Employee.builder()
                .age(33)
                .name("Nicky")
                .position("Student")
                .build();

        Long id = employeeRepository.save(employee).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nicky"))
                .andExpect(jsonPath("$.age").value(33))
                .andExpect(jsonPath("$.position").value("Student"));

        assertEquals(1, employeeRepository.findAll().size());
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee1 = Employee.builder()
                .age(33)
                .name("Nicky")
                .position("Student")
                .build();

        Employee employee2 = Employee.builder()
                .age(29)
                .name("John")
                .position("Developer")
                .build();

        Employee employee3 = Employee.builder()
                .age(40)
                .name("Emma")
                .position("Manager")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Nicky"))
                .andExpect(jsonPath("$[1].name").value("John"))
                .andExpect(jsonPath("$[2].name").value("Emma"))
                .andExpect(jsonPath("$[0].position").value("Student"))
                .andExpect(jsonPath("$[1].position").value("Developer"))
                .andExpect(jsonPath("$[2].position").value("Manager"));

        assertEquals(3, employeeRepository.findAll().size());
    }

    @Test
    public void testFindByOrganizationId() throws Exception {
        Employee employee1 = Employee.builder()
                .age(33)
                .name("Nicky")
                .position("Student")
                .organizationId(1L)
                .build();

        Employee employee2 = Employee.builder()
                .age(29)
                .name("John")
                .position("Developer")
                .organizationId(1L)
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Long organizationId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/organization/{organizationId}", organizationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Nicky"))
                .andExpect(jsonPath("$[1].name").value("John"));

        assertEquals(2, employeeRepository.findAll().size());
    }

    @Test
    public void testFindByDepartmentId() throws Exception {
        Employee employee1 = Employee.builder()
                .age(33)
                .name("Alice")
                .position("Manager")
                .departmentId(1L)
                .build();

        Employee employee2 = Employee.builder()
                .age(25)
                .name("Bob")
                .position("Developer")
                .departmentId(1L)
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Long departmentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/department/{departmentId}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));

        assertEquals(2, employeeRepository.findAll().size());
    }


    @Test
    public void testUpdateEmployeeById() throws Exception {
        Employee employeeToAdd = Employee.builder()
                .age(33)
                .name("Nicky")
                .position("Student")
                .organizationId(1L)
                .departmentId(1L)
                .build();

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .age(25)
                .position("Developer")
                .build();

        Long employeeId = employeeRepository.save(employeeToAdd).getId();

        String requestContent = objectMapper.writeValueAsString(employeeRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employee/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isAccepted());

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " cannot be found"));
        assertEquals(25, employee.getAge());
        assertEquals("Developer", employee.getPosition());
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        Employee employeeToDelete = Employee.builder()
                .age(33)
                .position("Student")
                .build();

        Long employeeId = employeeRepository.save(employeeToDelete).getId();

        assertEquals(1, employeeRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(0, employeeRepository.findAll().size());
    }
}
