package com.ms.email.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ms.email.enums.StatusEmail;
import com.ms.email.model.EmailModel;
import com.ms.email.repository.EmailRepository;

@Service
public class EmailService {

  Logger logger = LogManager.getLogger(EmailService.class);

  @Autowired
  private EmailRepository emailRepository;

  @Autowired
  private JavaMailSender emailSender;

  @Transactional
  public void sendEmail(EmailModel emailModel) {
    emailModel.setSendDateEmail(LocalDateTime.now());
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(emailModel.getEmailFrom());
      message.setTo(emailModel.getEmailTo());
      message.setSubject(emailModel.getSubject());
      message.setText(emailModel.getText());

      emailSender.send(message);
      emailModel.setStatusEmail(StatusEmail.SENT);
      logger.info("Email sent successfully to: {} ", emailModel.getEmailTo());
    } catch (MailException e) {
      emailModel.setStatusEmail(StatusEmail.ERROR);
      logger.error("Email with error: {} ", emailModel.toString());
      logger.error("Error {} ", e);
    } finally {
      emailRepository.save(emailModel);
      logger.info("Email saved successfully emailId: {} ", emailModel.getEmailId());
    }
  }

  public Page<EmailModel> findAll(Pageable pageable) {
    return emailRepository.findAll(pageable);
  }

  public Optional<EmailModel> findById(UUID emailId) {
    return emailRepository.findById(emailId);
  }
}
