package com.nelioalves.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.nelioalves.cursomc.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nelioalves.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

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

    protected String htmlFromTemplatePedido(Pedido pedido){
        Context context = new Context();
        context.setVariable("pedido", pedido);
        return templateEngine.process("email/confirmacaoPedido", context);
    }

    @Override
    public void sendOrderConfirmationHtmlEmail(Pedido pedido){
        try{
            sendHtmlMail(prepareMimeMessageFromPedido(pedido));
        }catch (MessagingException e){
            System.out.println("Exception, HtmlEmail: " + e.getMessage());
            sendOrderConfirmationEmail(pedido);
        }
    }

    protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(pedido.getCliente().getEmail());
        helper.setFrom(sender);
        helper.setSubject("Pedido confirmado, Código: " + pedido.getId());
        helper.setSentDate(new Date(System.currentTimeMillis()));
        helper.setText(htmlFromTemplatePedido(pedido), true);
        return mimeMessage;
    }
}
