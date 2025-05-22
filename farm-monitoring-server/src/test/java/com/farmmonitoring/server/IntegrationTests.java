package com.farmmonitoring.server;

import com.farmmonitoring.server.config.MockOAuth2Config;
import com.farmmonitoring.server.config.TestSecurityConfig;
import com.farmmonitoring.server.dto.CreateReadingRequest;
import com.farmmonitoring.server.dto.SensorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, MockOAuth2Config.class})
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testGetSensors() throws Exception {
        mockMvc.perform(get("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testGetFarms() throws Exception {
        mockMvc.perform(get("/api/farms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testGetFields() throws Exception {
        mockMvc.perform(get("/api/fields")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testCreateReading() throws Exception {
        CreateReadingRequest request = new CreateReadingRequest();
        request.setSensorId(1L);
        request.setReadingType("TEMPERATURE");
        request.setValue(new BigDecimal("22.5"));
        request.setUnit("°C");

        mockMvc.perform(post("/api/readings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorId", is(1)))
                .andExpect(jsonPath("$.value", is(22.5)))
                .andExpect(jsonPath("$.unit", is("°C")));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testCreateReadingWithInvalidSensor() throws Exception {
        CreateReadingRequest request = new CreateReadingRequest();
        request.setSensorId(9999L); // Неіснуючий sensor ID
        request.setReadingType("TEMPERATURE");
        request.setValue(new BigDecimal("22.5"));
        request.setUnit("°C");

        mockMvc.perform(post("/api/readings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testGetSensorById() throws Exception {
        // Спершу отримуємо список сенсорів
        MvcResult result = mockMvc.perform(get("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        SensorDto[] sensors = objectMapper.readValue(content, SensorDto[].class);

        // Перевіряємо, чи є хоча б один сенсор
        if (sensors.length > 0) {
            Long sensorId = sensors[0].getId();

            mockMvc.perform(get("/api/sensors/" + sensorId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(sensorId.intValue())));
        }
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
