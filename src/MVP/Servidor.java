package MVP;

import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry; 
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeoutException;

import Mensageria.ControladorMensageria;
import Mensageria.ServicoMensageria;

public class Servidor implements ServicoMensageria {

	private static final long serialVersionUID = 1L;
	private ControladorMensageria controladora = new ControladorMensageria();

	public Servidor() throws RemoteException{
		super();
		System.out.println("Servidor criado!");
	}

	@Override
	public void EnviarMensagemProMiddlewareDeMensagem(String remetente, String destinatario, String conteudo) {
		try {
			System.out.println("O método EnviarMensagemProMiddlewareDeMensagem foi chamado!!!");
			this.controladora.EscreverMensagemNoTopico(remetente, destinatario, conteudo);
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

