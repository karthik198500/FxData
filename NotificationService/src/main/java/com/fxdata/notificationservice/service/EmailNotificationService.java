package com.fxdata.notificationservice.service;

import com.fxdata.notificationservice.dto.NotificationData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Log4j2
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender emailSender;

    public EmailNotificationService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendNotification(NotificationData notificationData) {
        //TODO - write a retry mechanism
        //TODO - store messages in a persistent store and delete them once sent or otherwise retry.
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(notificationData.getFromAddress());
            helper.setTo(notificationData.getToAddress());
            helper.setSubject(notificationData.getSubject());
            helper.setText(notificationData.getBody());
            if(null!=notificationData.getAttachmentLocation()) {
                FileSystemResource file
                        = new FileSystemResource(new File(notificationData.getAttachmentLocation()));
                helper.addAttachment(file.getFilename(), file);
            }
            log.debug("Sending mail with the below details"+message);
            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Exception while sending an email"+notificationData.toString(),e);
            throw new RuntimeException(e);
        }
    }
}

