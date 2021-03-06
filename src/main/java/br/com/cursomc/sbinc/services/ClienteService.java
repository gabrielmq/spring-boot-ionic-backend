package br.com.cursomc.sbinc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.Cidade;
import br.com.cursomc.sbinc.domain.Cliente;
import br.com.cursomc.sbinc.domain.Endereco;
import br.com.cursomc.sbinc.domain.dto.ClienteDTO;
import br.com.cursomc.sbinc.domain.dto.ClienteNewDTO;
import br.com.cursomc.sbinc.domain.enums.Perfil;
import br.com.cursomc.sbinc.domain.enums.TipoCliente;
import br.com.cursomc.sbinc.repositories.ClienteRepository;
import br.com.cursomc.sbinc.repositories.EnderecoRepository;
import br.com.cursomc.sbinc.security.UserSS;
import br.com.cursomc.sbinc.services.exceptions.AuthorizationException;
import br.com.cursomc.sbinc.services.exceptions.DataIntegrityException;
import br.com.cursomc.sbinc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.proile.size}")
	private Integer size;
	
	public Cliente findById(Integer clienteId) {
		
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !clienteId.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		return repository.findById(clienteId)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " +clienteId
						+ ", Tipo: "+Categoria.class.getName()));
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente cliente = repository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " +user.getId()+ ", Tipo: "+Categoria.class.getName());
		}
		
		return cliente;
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
			throw new DataIntegrityException("Não é possível excluir o cliente "+clienteId+", pois há pedidos relacionados a ele");
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
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), encoder.encode(objDto.getSenha()));
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
	
	public URI uploadProfilePicture(MultipartFile file) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(file);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		String filename = prefix + user.getId() + ".jpg";
		return s3Service.uploadFile(filename, imageService.getInputStream(jpgImage, "jpg"), "image");
	}
}
