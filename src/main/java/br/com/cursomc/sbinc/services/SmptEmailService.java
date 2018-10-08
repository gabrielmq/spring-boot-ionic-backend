package br.com.cursomc.sbinc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmptEmailService extends AbstractEmailService {

	private static final Logger LOG = LoggerFactory.getLogger(SmptEmailService.class);
	
	@Autowired
	private MailSender sender;
	
	@Autowired
	private JavaMailSender javaMail;
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email");
		sender.send(msg);
		LOG.info("Email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando email HTML");
		javaMail.send(msg);
		LOG.info("Email enviado");
	}
}
