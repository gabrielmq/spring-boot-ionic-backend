package br.com.cursomc.sbinc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class SmptEmailService extends AbstractEmailService {

	private static final Logger LOG = LoggerFactory.getLogger(SmptEmailService.class);
	
	@Autowired
	private MailSender sender;
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email");
		sender.send(msg);
		LOG.info("Email enviado");
	}
}
