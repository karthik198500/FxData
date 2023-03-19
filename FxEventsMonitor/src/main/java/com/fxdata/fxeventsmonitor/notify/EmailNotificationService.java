package com.fxdata.fxeventsmonitor.notify;

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
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender emailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.emailSender = mailSender;
    }


    @Override
    public void sendNotification() {
        //TODO - write a retry mechanism
        //TODO - store messages in a persistent store and delete them once sent or otherwise retry.
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("noreply@gmail.com");
            helper.setTo("karthik198500@gmail.com");
            helper.setSubject("Forex rate");
            helper.setText("text");

            FileSystemResource file
                    = new FileSystemResource(new File("/Users/kkasiraju/dev/MyMusings/FxRates/FxEventsMonitor/20230318_2203.csv"));
            //helper.addAttachment(file.getFilename(), file);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
