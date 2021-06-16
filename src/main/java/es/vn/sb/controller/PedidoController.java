package es.vn.sb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import brave.Span;
import brave.Tracer;
import es.vn.sb.model.Pedido;
import es.vn.sb.service.PedidoService;
import es.vn.sb.utils.Constants;
import es.vn.sb.utils.Utils;

@RestController
@RequestMapping("/api")
public class PedidoController {

	private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

	@Autowired
	PedidoService pedidoServicio;

	@Value("${spring.application.name}")
	private String appName;

	@Autowired
	Tracer tracer;

	
	@RequestMapping(path = "/pedido", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public HttpEntity<String> createPedido(@RequestBody Pedido pedido) {
		
		logger.info("peticion_iniciada: {}", pedido.toString());
		
		Span span = tracer.currentSpan();
		if (Constants.ERROR == 0) {
			span.annotate("Inicio de la peticion sin error en el controller del servicio-c");
			return new ResponseEntity<String>(
					String.format("OK - %s\n%s", appName, pedidoServicio.createPedido(pedido)),
					HttpStatus.OK);
		}

//		if (Utils.getRandomInt() == 1) {
			try {
				this.generaError(null);				
			} catch(Exception e) {
				logger.info("peticion_ko");
				logger.error("ERROR controlado", e);
			}
			span.annotate("Generamos error en el servicio-c");
			return new ResponseEntity<String>(String.format("KO - %s", appName),
					HttpStatus.INTERNAL_SERVER_ERROR);
//		} else {
//			span.annotate("Inicio de la peticion sin error en el controller del servicio-c");
//			return new ResponseEntity<String>(String.format("OK - %s\n%s", appName,
//					pedidoServicio.createPedido(pedido)), HttpStatus.OK);
//		}
	}
	
	private void generaError(Object obj) {
		obj.toString();
	}

}
