package br.com.cursomc.sbinc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.cursomc.sbinc.domain.Cliente;
import br.com.cursomc.sbinc.domain.dto.ClienteNewDTO;
import br.com.cursomc.sbinc.domain.enums.TipoCliente;
import br.com.cursomc.sbinc.repositories.ClienteRepository;
import br.com.cursomc.sbinc.resources.exceptions.FieldMessage;
import br.com.cursomc.sbinc.services.validation.utils.Utils;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert ann) { }
	
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !Utils.isValidCpf(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "Cpf inválido"));
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !Utils.isValidCnpj(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "Cnpj inválido"));
		}
		
		Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		list.forEach(e -> {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		});
		
		return list.isEmpty();
	}
}
