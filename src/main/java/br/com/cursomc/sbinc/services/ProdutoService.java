package br.com.cursomc.sbinc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.Produto;
import br.com.cursomc.sbinc.domain.dto.ProdutoDTO;
import br.com.cursomc.sbinc.repositories.CategoriaRepository;
import br.com.cursomc.sbinc.repositories.ProdutoRepository;
import br.com.cursomc.sbinc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto finById(Integer produtoId) {
		return repository.findById(produtoId)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " +produtoId
						+ ", Tipo: "+Produto.class.getName()));
	}
	
	public Page<ProdutoDTO> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest).map( obj -> new ProdutoDTO(obj) );
	}
}
