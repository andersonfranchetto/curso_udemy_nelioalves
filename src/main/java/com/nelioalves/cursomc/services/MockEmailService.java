package com.nelioalves.cursomc.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public class MockEmailService extends AbstractEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendMail(SimpleMailMessage message) {
        LOG.info("Simulando Envio de Email........");
        LOG.info(message.toString());
        LOG.info("Email Enviado.");
    }

    @Override
    public void sendHtmlMail(MimeMessage message) {
        LOG.info("Simulando Envio de Email Html........");
        LOG.info(message.toString());
        LOG.info("Email Enviado.");
    }
}
