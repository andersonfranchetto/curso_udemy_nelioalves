package com.nelioalves.cursomc.services.interfaces;

import javax.mail.internet.MimeMessage;

import com.nelioalves.cursomc.domain.Cliente;
import org.springframework.mail.SimpleMailMessage;

import com.nelioalves.cursomc.domain.Pedido;

public interface EmailService {

    void sendOrderConfirmationEmail(Pedido pedido);

    void sendMail(SimpleMailMessage message);

    void sendOrderConfirmationHtmlEmail(Pedido pedido);

    void sendHtmlMail(MimeMessage message);

    void sendNewPasswordEmail(Cliente cliente, String newPassword);
}
