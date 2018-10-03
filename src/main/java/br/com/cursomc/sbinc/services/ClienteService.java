package br.com.cursomc.sbinc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.Cliente;
import br.com.cursomc.sbinc.domain.dto.ClienteDTO;
import br.com.cursomc.sbinc.repositories.ClienteRepository;
import br.com.cursomc.sbinc.services.exceptions.DataIntegrityException;
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
	
	public Cliente update(Cliente cliente) {
		Cliente c = findById(cliente.getId());
		updateData(c, cliente);
		return repository.save(c);
	}
	
	public void delete(Integer clienteId) {
		findById(clienteId);
		try {
			repository.deleteById(clienteId);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
	}
	
	public List<ClienteDTO> findAll() {
		return repository.findAll()
				.stream().map( cliente -> new ClienteDTO(cliente) )
				.collect(Collectors.toList());
	}
	
	public Page<ClienteDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest).map( cliente -> new ClienteDTO(cliente) );
	}
	
	public Cliente fromDTO(ClienteDTO obj) {
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null, null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());		
	}
}
