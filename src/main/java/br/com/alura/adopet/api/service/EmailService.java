package br.com.alura.adopet.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Autowired
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void dispararEmail(String to, String subject, String message) {
    SimpleMailMessage email = new SimpleMailMessage();
    email.setFrom("adopet@email.com.br");
    email.setTo(to);
    email.setSubject(subject);
    email.setText(message);
    javaMailSender.send(email);
  }
}
