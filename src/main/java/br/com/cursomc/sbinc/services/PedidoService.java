package br.com.cursomc.sbinc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.Pedido;
import br.com.cursomc.sbinc.repositories.PedidoRepository;
import br.com.cursomc.sbinc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;
	
	public Pedido finById(Integer pedidoId) {
		return repository.findById(pedidoId)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " +pedidoId
						+ ", Tipo: "+Categoria.class.getName()));
	}
}
