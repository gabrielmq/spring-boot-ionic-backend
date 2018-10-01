package br.com.cursomc.sbinc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.Cliente;
import br.com.cursomc.sbinc.repositories.ClienteRepository;
import br.com.cursomc.sbinc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	public Cliente findById(Integer clienteId) {
		return repository.findById(clienteId)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " +clienteId
						+ ", Tipo: "+Categoria.class.getName()));
	}
}
