package es.vn.sb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import brave.Span;
import brave.Tracer;
import es.vn.sb.model.Pedido;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${service-nodejs-1.pedido.url}")
    String serviceNodeJS1PedidoURL;

    @Autowired
    Tracer tracer;
    
	public String createPedido(Pedido pedido) {
		Span span = tracer.currentSpan();
		span.tag("service", "entrada al servicio");
		span.annotate(String.format("Llamada al servicio con url %s", serviceNodeJS1PedidoURL));
		return restTemplate.postForEntity(serviceNodeJS1PedidoURL, pedido, String.class).getBody();
	}
    
}   
