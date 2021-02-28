package MVP;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry; 
import java.rmi.server.UnicastRemoteObject;

public class Servidor implements InverterItf {

	private static final long serialVersionUID = 1L;

	public Servidor() throws RemoteException{
		super();
		System.out.println("Servidor criado!");
	}

	public String inverter(String msg) {

		StringBuffer strbuf = new StringBuffer(msg);

		System.out.println("Recebido: "+msg);

		String retorno = (strbuf.reverse()).toString();
    
		return retorno;
	}
	
	public void EnviarMensagemProMiddlewareDeMensagem(String conteudo) {
		// enviar pro rabbit mq
	}
	
	public static void main(String args[]) { 
		try {
						
			// Instantiating the implementation class 
	         Servidor obj = new Servidor(); 
	         
	         // Exporting the object of implementation class  
	         // (here we are exporting the remote object to the stub) 
	         InverterItf stub = (InverterItf) UnicastRemoteObject.exportObject(obj, 0);  
	         
	         // Binding the remote object (stub) in the registry 
	         Registry registry = LocateRegistry.createRegistry(20394); 
	         
	         registry.bind("Inverterltf", stub);
	         
	         System.err.println("Server ready"); 
	      } catch (Exception e) { 
	         System.err.println("Server exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	   } 

}

