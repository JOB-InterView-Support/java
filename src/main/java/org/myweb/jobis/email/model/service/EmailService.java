package org.myweb.jobis.email.model.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail); // 이메일 수신자
            message.setSubject(subject); // 이메일 제목
            message.setText(body); // 이메일 본문
            mailSender.send(message); // 이메일 발송
        } catch (Exception e) {
            // 예외 로그 출력
            System.err.println("이메일 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
