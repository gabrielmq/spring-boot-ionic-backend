package br.com.cursomc.sbinc.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.cursomc.sbinc.domain.Pedido;

public interface EmailService {
	void sendOrderConfirmationEmail(Pedido pedido);
	void sendEmail(SimpleMailMessage msg);
}
