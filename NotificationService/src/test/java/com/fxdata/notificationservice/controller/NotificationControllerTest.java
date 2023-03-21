package com.fxdata.notificationservice.controller;

import com.fxdata.notificationservice.dto.NotificationData;
import com.fxdata.notificationservice.service.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@ExtendWith(SpringExtension.class)
//@TestPropertySource(locations = {"classpath:vls-core-test.properties", "classpath:vls-database-test.properties", "classpath:vls-logging-test.properties", "classpath:vls-server-test.properties"})
class NotificationControllerTest {

    private static String NOTIFICATION_SERVICE_URL;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Value("${notification.service.version}")
    private String version;

    @BeforeEach
    void setUp() {
        NOTIFICATION_SERVICE_URL =  "/api/" +version+"/notification-service";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sendNotification() {
    }

    @Test
    void welcomePage() {
    }


    @Test
    void emptyBodyThrowsAnValidationError() throws Exception {
        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));
        this.mockMvc.perform(
                post(NOTIFICATION_SERVICE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void wrongMethodThrowsAnError() throws Exception {
        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));;
        this.mockMvc.perform(get(NOTIFICATION_SERVICE_URL))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void noData() throws Exception {
        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));;
        MvcResult mvcResult = this.mockMvc.perform(post(NOTIFICATION_SERVICE_URL))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void throwsAnError() throws Exception {
        doThrow(new RuntimeException("Error")).when(notificationService).sendNotification(Mockito.any(NotificationData.class));;
        MvcResult mvcResult = this.mockMvc.perform(
                post(NOTIFICATION_SERVICE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonContent())
        ).andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    private String getJsonContent() {
        return "{\n" +
                " \n" +
                "  \"fromAddress\": \"someEmail@aus.com.au\",\n" +
                "  \"toAddress\": \"someEmail@aus.com.au\",\n" +
                "  \"subject\":\"subject\",\n" +
                "  \"type\":\"email\",\n" +
                "  \"body\":\"body\",\n" +
                "  \"attachementLocation\": \"attachmentLocation\"\n" +
                "  \n" +
                "}";
    }

}