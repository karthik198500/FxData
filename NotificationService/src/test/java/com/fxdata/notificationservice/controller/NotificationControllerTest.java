package com.fxdata.notificationservice.controller;

import com.fxdata.notificationservice.dto.NotificationData;
import com.fxdata.notificationservice.service.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.mockito.Mockito.doNothing;
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
    void retrieveDataKey() throws Exception {
        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));
        /*Assertions.assertThrowsExactly(MethodArgumentNotValidException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                mockMvc.perform(
                        post(NOTIFICATION_SERVICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                );
            }
        });*/
        this.mockMvc.perform(
                post(NOTIFICATION_SERVICE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void retrieveDataKeyWrongMethod() throws Exception {
        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));;
        this.mockMvc.perform(post(NOTIFICATION_SERVICE_URL + "1"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void retrieveDataKeyNoArgument() throws Exception {

        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));;
        this.mockMvc.perform(get(NOTIFICATION_SERVICE_URL))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void retrieveDataKeyNoData() throws Exception {
        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));;
        MvcResult mvcResult = this.mockMvc.perform(get(NOTIFICATION_SERVICE_URL + "10"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        //Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains(DATAKEY_NOT_FOUND));
    }

    @Test
    void testParamAsRequestParameter() throws Exception {
        doNothing().when(notificationService).sendNotification(Mockito.any(NotificationData.class));
        this.mockMvc.perform(get(NOTIFICATION_SERVICE_URL + "?dataKey=1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}