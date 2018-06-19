package com.nelioalves.cursomc.services.interfaces;

import com.nelioalves.cursomc.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendOrderConfirmationEmail(Pedido pedido);

    void sendMail(SimpleMailMessage simpleMailMessage);
}
