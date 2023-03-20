package com.ms.email.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ms.email.dto.EmailDto;
import com.ms.email.service.EmailService;

@Component
public class EmailConsumer {

  @Autowired
  EmailService emailService;

  @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
  public void listen(@Payload EmailDto emailDto) {
    emailService.sendEmail(emailDto.convertToEmailModel());
  }
}
