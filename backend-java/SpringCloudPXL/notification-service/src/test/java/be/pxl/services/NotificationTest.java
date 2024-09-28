package be.pxl.services;


import be.pxl.services.domain.Notification;
import be.pxl.services.domain.dto.NotificationRequest;
import be.pxl.services.exceptions.NotificationNotFoundException;
import be.pxl.services.repository.NotificationRepository;
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


@SpringBootTest(classes = NotificationServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class NotificationTest {


    @BeforeEach
    public void clearRepository() {
        notificationRepository.deleteAll();
    }


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationRepository notificationRepository;

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
    public void testGetNotifications() throws Exception {
        Notification notification1 = Notification.builder()
                .message("Hello")
                .subject("School")
                .sender("Nicky")
                .receiver("PXL")
                .build();

        Notification notification2 = Notification.builder()
                .message("Grr")
                .subject("Household")
                .sender("Chisato")
                .receiver("Nicky")
                .build();

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].message").value("Hello"))
                .andExpect(jsonPath("$[1].message").value("Grr"))
                .andExpect(jsonPath("$[0].subject").value("School"))
                .andExpect(jsonPath("$[1].subject").value("Household"))
                .andExpect(jsonPath("$[0].sender").value("Nicky"))
                .andExpect(jsonPath("$[1].sender").value("Chisato"))
                .andExpect(jsonPath("$[0].receiver").value("PXL"))
                .andExpect(jsonPath("$[1].receiver").value("Nicky"));

        assertEquals(2, notificationRepository.findAll().size());
    }

    @Test
    public void testFindById() throws Exception {
        Notification notification = Notification.builder()
                .message("Hello")
                .subject("School")
                .sender("Nicky")
                .receiver("PXL")
                .build();

        Long id = notificationRepository.save(notification).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notification/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello"))
                .andExpect(jsonPath("$.subject").value("School"))
                .andExpect(jsonPath("$.sender").value("Nicky"))
                .andExpect(jsonPath("$.receiver").value("PXL"));

        assertEquals(1, notificationRepository.findAll().size());
    }

    @Test
    public void testAddNotification() throws Exception {
        Notification notification = Notification.builder()
                .message("Hello")
                .subject("School")
                .sender("Nicky")
                .receiver("PXL")
                .build();

        String notificationString = objectMapper.writeValueAsString(notification);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notificationString))
                .andExpect(status().isCreated());

        assertEquals(1, notificationRepository.findAll().size());
    }

    @Test
    public void testUpdateNotificationById() throws Exception {
        Notification notification = Notification.builder()
                .message("Hello")
                .subject("School")
                .sender("Nicky")
                .receiver("PXL")
                .build();

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Hmm")
                .sender("Bart")
                .build();

        Long id = notificationRepository.save(notification).getId();

        String requestContent = objectMapper.writeValueAsString(notificationRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/notification/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isAccepted());

        Notification notificationToCheck = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " cannot be found"));
        assertEquals("Hmm", notificationToCheck.getMessage());
        assertEquals("Bart", notificationToCheck.getSender());
    }

    @Test
    public void testDeleteNotificationById() throws Exception {
        Notification notification = Notification.builder()
                .message("Hello")
                .subject("School")
                .sender("Nicky")
                .receiver("PXL")
                .build();

        Long id = notificationRepository.save(notification).getId();

        assertEquals(1, notificationRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notification/{notificationId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(0, notificationRepository.findAll().size());
    }
}
