package Mensageria;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;


public class ControladorMensageria {
	private  final  static String QUEUE_NAME = "monitoramento" ;
	
	public ControladorMensageria() {}
	
	public void EscreverMensagemNoTopico(String remetente, String destinatario, String conteudo) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setHost("localhost");
		
		String mensagem = "\nRemetente: " + remetente + ".\nDestinatário: " + destinatario + ".\nConteúdo da mensagem: " + conteudo;
		
		try (Connection connection = factory.newConnection()) {
		  Channel channel = connection.createChannel();
		  channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		  channel.basicPublish("", QUEUE_NAME, null, mensagem.getBytes("UTF-8"));
		}
		
	}

	public Channel ConectarComOTopico() throws IOException, TimeoutException {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println("Esperando por mensagens....");
	    
	    return channel;
	}
	
	public void LerMensagem(Channel channel) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
	}
}
