package com.fxdata.notificationservice.controller;

import com.fxdata.notificationservice.dto.NotificationData;
import com.fxdata.notificationservice.service.NotificationService;
import com.fxdata.notificationservice.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fxdata.notificationservice.util.Constants.*;
import static com.fxdata.notificationservice.util.Constants.SWAGGER_NOTIFICATION_SERVICE_INFO;


@RestController
@Validated
@Api(tags = SWAGGER_TAGS, value = SWAGGER_NOTIFICATION_SERVICE_INFO)
@RequestMapping(API_VERSION_NOTIFICATION_SERVICE)
@Log4j2
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "send notification", response = String.class)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendNotification(
            @RequestBody
            @Validated NotificationData notificationData) {
        if("email".equalsIgnoreCase(notificationData.getType())){
            notificationService.sendNotification(notificationData);
        }else{
            throw new RuntimeException("Notification Type is not supported."+notificationData.getType());
        }

        return  ResponseEntity.ok("Your message is successfully sent.");
    }


    /*
     * Generic URL to test the working of the application.
     * This can be deleted in production grade application.
     * */

    @ApiOperation(value = "Phone Number Parser", response = String.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> welcomePage() {
        log.info("Notification Service welcome page");
        return  ResponseEntity.ok("Welcome to Notification Service");

    }
}
