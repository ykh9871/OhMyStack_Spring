package com.ykh.backend.service.email;

import com.ykh.backend.repository.user.EmailService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    public final JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String subject, String messageContent) throws Exception {
        MimeMessage message = createMessage(to, subject, messageContent);
        try {
            emailSender.send(message);
        } catch(MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private MimeMessage createMessage(String to, String subject, String messageContent) throws Exception {
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setText(messageContent, "utf-8", "html");
        message.setFrom(new InternetAddress("ykh6641@gmail.com", "yoongyuheon"));

        return message;
    }

}