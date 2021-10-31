package pe.edu.pucp.mvc.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.repositories.PedidoRepository;

@Service
public class PedidoService {
	@Autowired
	PedidoService pedidoService;
	
	/*
	public List<Pedido> listarPedidos(){
		return List<PedidoModel> pedidoService.findAll();
	}
	*/
	
}
