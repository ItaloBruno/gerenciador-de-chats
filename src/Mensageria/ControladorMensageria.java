package Mensageria;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;


public class ControladorMensageria {
	private final static String NOME_DA_FILA = "monitoramento";

	
	public ControladorMensageria() {}
	
	public void EscreverMensagemNoTopico(String remetente, String destinatario, String conteudo) throws IOException, TimeoutException {
		ConnectionFactory fabrica = new ConnectionFactory();
		fabrica.setUsername("guest");
		fabrica.setPassword("guest");
		fabrica.setHost("localhost");
		
		String mensagem = "\nRemetente: " + remetente + ".\nDestinatário: " + destinatario + ".\nConteúdo da mensagem: " + conteudo;
		
		try (Connection conexao = fabrica.newConnection()) {
		  Channel canal = conexao.createChannel();
		  canal.queueDeclare(NOME_DA_FILA, false, false, false, null);
		  canal.basicPublish("", NOME_DA_FILA, null, mensagem.getBytes("UTF-8"));
		}
    }

	public Channel ConectarComOTopico() throws IOException, TimeoutException {
	    ConnectionFactory fabrica = new ConnectionFactory();
	    fabrica.setHost("localhost");
	    Connection conexao = fabrica.newConnection();
	    Channel canal = conexao.createChannel();
	    canal.queueDeclare(NOME_DA_FILA, false, false, false, null);
	    System.out.println("Esperando por mensagens....");
	    
	    return canal;
	}
	
	public void LerMensagemDoTopico(Channel canal) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String mensagemRecebida = new String(delivery.getBody(), "UTF-8");
            System.out.println("\n[x] Mensagem recebida: '" + mensagemRecebida + "'");
        };
        canal.basicConsume(NOME_DA_FILA, true, deliverCallback, consumerTag -> { });
	}
}
