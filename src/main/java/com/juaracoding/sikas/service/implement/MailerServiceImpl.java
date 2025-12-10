package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-253.28294.334, built on December 5, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/9/2025 11:48 PM
@Last Modified 12/9/2025 11:48 PM
Version 1.0
*/

import com.juaracoding.sikas.service.MailerService;
import com.juaracoding.sikas.util.TemplateReaderUtil;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MailerServiceImpl implements MailerService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void sendTemplateEmail(String to, String subject, String templatePath, Map<String, String> variables) {

        try {
            String html = TemplateReaderUtil.readHtmlTemplate(templatePath);

            for (Map.Entry<String, String> entry : variables.entrySet()) {
                html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("superadmin@sikas.com");
            helper.setText(html, true);

            mailSender.send(message);

            System.out.println("[ASYNC] Email template sent to: " + to);

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to send email template: " + e.getMessage());
        }
    }
}