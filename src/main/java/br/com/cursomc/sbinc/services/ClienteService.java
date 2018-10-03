package br.com.cursomc.sbinc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.Cidade;
import br.com.cursomc.sbinc.domain.Cliente;
import br.com.cursomc.sbinc.domain.Endereco;
import br.com.cursomc.sbinc.domain.dto.ClienteDTO;
import br.com.cursomc.sbinc.domain.dto.ClienteNewDTO;
import br.com.cursomc.sbinc.domain.enums.TipoCliente;
import br.com.cursomc.sbinc.repositories.ClienteRepository;
import br.com.cursomc.sbinc.repositories.EnderecoRepository;
import br.com.cursomc.sbinc.services.exceptions.DataIntegrityException;
import br.com.cursomc.sbinc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente findById(Integer clienteId) {
		return repository.findById(clienteId)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " +clienteId
						+ ", Tipo: "+Categoria.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = repository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
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
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
		Cidade cidade = new Cidade(objDto.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cliente, cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objDto.getTelefone1());
		
		if (objDto.getTelefone2() != null) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		
		if (objDto.getTelefone3() != null) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());		
	}
}
