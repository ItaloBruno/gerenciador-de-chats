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
	
	public static void main(String args[]) { 
		try {
						
			// Instantiating the implementation class 
	         Servidor obj = new Servidor(); 
	         
	         // Exporting the object of implementation class  
	         // (here we are exporting the remote object to the stub) 
	         ServicoMensageria stub = (ServicoMensageria) UnicastRemoteObject.exportObject(obj, 0);  
	         
	         // Binding the remote object (stub) in the registry 
	         Registry registry = LocateRegistry.createRegistry(20394); 
	         
	         registry.bind("servidor-de-mensageria", stub);
	         
	         System.err.println("Servidor configurado!!!"); 
	      } catch (Exception e) { 
	         System.err.println("Server exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	   }
}

