package br.com.cursomc.sbinc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.ItemPedido;
import br.com.cursomc.sbinc.domain.PagamentoComBoleto;
import br.com.cursomc.sbinc.domain.Pedido;
import br.com.cursomc.sbinc.domain.enums.EstadoPagamento;
import br.com.cursomc.sbinc.repositories.ItemPedidoRepository;
import br.com.cursomc.sbinc.repositories.PagamentoRepository;
import br.com.cursomc.sbinc.repositories.PedidoRepository;
import br.com.cursomc.sbinc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRespository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido finById(Integer pedidoId) {
		return repository.findById(pedidoId)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " +pedidoId
						+ ", Tipo: "+Categoria.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.findById(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repository.save(obj);
		pagamentoRespository.save(obj.getPagamento());
		
		for (ItemPedido i : obj.getItens()) {
			i.setDesconto(0.0);
			i.setProduto(produtoService.finById(i.getProduto().getId()));
			i.setPreco(i.getProduto().getPreco());
			i.setPedido(obj);
		}
		
		itemRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}
}
