package Executores;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import MVP.Servidor;
import Mensageria.ServicoMensageria;

public class ServidorPeloTerminal {
	public static void main(String args[]) {
		
		try {
	         Servidor servidorRemoto = new Servidor(); 
	         
	         ServicoMensageria stub = (ServicoMensageria) UnicastRemoteObject.exportObject(servidorRemoto, 0);  
	         
	         Registry registrador = LocateRegistry.createRegistry(20394); 
	         
	         registrador.bind("servidor-de-mensageria", stub);
	         
	         System.err.println("Servidor configurado com sucesso!!!"); 
	      } catch (Exception e) { 
	         System.err.println("Server exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	   }
}
