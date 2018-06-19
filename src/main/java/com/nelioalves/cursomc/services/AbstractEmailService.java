package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService{

    @Value("${default.sender}")
    private String sender;

    @Override
    public void sendOrderConfirmationEmail(Pedido pedido){
        sendMail(prepareSimpleMailMessageFromPedido(pedido));
    }

    protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(pedido.getCliente().getEmail());
        message.setFrom(sender);
        message.setSubject("Pedido confirmado, Código: " + pedido.getId());
        message.setSentDate(new Date(System.currentTimeMillis()));
        message.setText(pedido.toString());
        return message;
    }

}
