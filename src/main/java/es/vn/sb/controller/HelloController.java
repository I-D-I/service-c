package es.vn.sb.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import brave.Span;
import brave.Tracer;
import es.vn.sb.service.PedidoService;
import es.vn.sb.utils.Constants;

@RestController
@RequestMapping("/api")
public class HelloController {

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@Autowired
	PedidoService helloService;

	@Value("${spring.application.name}")
	private String appName;

	@Value("${spring.application.version}")
	private String appVersion;

	@Autowired
	Tracer tracer;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public HttpEntity<String> hello(@RequestHeader Map<String, String> headers) {
		logger.info("START hello():");
		Span span = tracer.currentSpan();
		span.tag("controller", "entrada al controller");
		headers.forEach((key, value) -> {
			logger.info(String.format("Header '%s' = %s", key, value));
		});
		span.annotate("Enviando respuesta desde el servicio-c");
		return new ResponseEntity<String>(String.format("OK - %s\n", appName),
				HttpStatus.OK);
	}

	@RequestMapping(path = "/version", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public HttpEntity<String> version(
			@RequestHeader(value = "sprint", required = false, defaultValue = "0") String sprint) {
		logger.info("START hello(): sprint: " + sprint);

		return new ResponseEntity<String>(
				String.format("OK - '%s' in sprint: '%s', version: '%s'", appName, sprint, appVersion),
				HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@RequestMapping(path = "/error", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public HttpEntity<String> error() {
		logger.info("START error():");
		return new ResponseEntity<String>(String.format("%s", Constants.ERROR), HttpStatus.OK);
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.OPTIONS })
	@RequestMapping(path = "/error/{error}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public HttpEntity<String> helloError(@PathVariable int error) {
		logger.info("START helloError():");
		Constants.ERROR = error;

		return new ResponseEntity<String>(
				String.format("Valor de ERROR en '%s': '%d'", appName, error),
				HttpStatus.OK);
	}
	
}
