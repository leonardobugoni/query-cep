package io.github.leobugoni.cep.service;

import io.github.leobugoni.cep.wsdl.AtendeCliente;
import io.github.leobugoni.cep.wsdl.AtendeClienteService;
import io.github.leobugoni.cep.wsdl.EnderecoERP;
import io.github.leobugoni.cep.model.Endereco;

import javax.enterprise.inject.Vetoed;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;
import static javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY;

/**
 * @author Léo Bugoni leobugoni@gmail.com
 */
@Vetoed
public class ConsultaCepService {
    private final String endereco;

	public ConsultaCepService() {
		this(null);
	}

	public ConsultaCepService(final String endereco) {
		super();
		this.endereco = endereco;
	}

	/** Consultar e retorna o endereço */
	public Endereco consultar(final String cep) {
		final String cepSomenteNumero = cep.replaceAll("[^0-9]+", "");
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		final EnderecoERP enderecoERP = consulta(cepSomenteNumero, new SaveStringHandler(stream));
		final Endereco endereco = extractEndereco(enderecoERP);
		return endereco;
	}

	/** Extrai a consulta para um Endereço */
	private Endereco extractEndereco(final EnderecoERP enderecoERP){
		final String bairro = enderecoERP.getBairro();
		final Integer cep = new Integer(enderecoERP.getCep());
		final String cidade = enderecoERP.getCidade();
		final String rua = enderecoERP.getEnd();
		final String uf = enderecoERP.getUf();
		final Endereco endereco = new Endereco(bairro, cep, cidade, rua, uf);
		return endereco;
	}


	private EnderecoERP consulta(final String cep, final SOAPHandler<SOAPMessageContext> handler){
		EnderecoERP enderecoERP = new EnderecoERP();
		try{
			out.println("Correio - Iniciando consulta do CEP " + cep);
			final URL url = new URL(endereco);
			final Service ws = new AtendeClienteService(url);
			final AtendeCliente correio = ws.getPort(AtendeCliente.class);

			final BindingProvider binding = (BindingProvider) correio;
			final List<Handler> handlers = binding.getBinding().getHandlerChain();
			handlers.add(handler);
			binding.getBinding().setHandlerChain(handlers);
			enderecoERP = correio.consultaCEP(cep);
		} catch (final Exception e) {
		    e.printStackTrace();
		}
		return enderecoERP;
	}

	private static class SaveStringHandler implements SOAPHandler<SOAPMessageContext> {
		private final OutputStream stream;

		private SaveStringHandler(final OutputStream response) {
			super();
			this.stream = response;
		}

		public boolean handleMessage(SOAPMessageContext context) {
			final Boolean outboundProperty = (Boolean) context.get(MESSAGE_OUTBOUND_PROPERTY);
			if (!outboundProperty) {
				final SOAPMessage message = context.getMessage();
				try {
					message.writeTo(stream);
				} catch (final java.lang.Exception e) {
					throw new RuntimeException(e);
				}
			}

			return outboundProperty;
		}

		public Set<QName> getHeaders() {
			return new HashSet<>();
		}

		public boolean handleFault(final SOAPMessageContext context) {
			return (Boolean) context.get(MESSAGE_OUTBOUND_PROPERTY);
		}

		public void close(final MessageContext context) {

		}
	}
}
