package ControladorTupla;
import Tuplas.TuplaUsuario;
import Tuplas.TuplaMensagem;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.core.lease.Lease;

import net.jini.space.JavaSpace;


public class Controlador {
	private JavaSpace conexaoComOEspaco;
	
	public Controlador(){
		conectarComOEspacoDeTupla();
	}
	
	public void conectarComOEspacoDeTupla() {
	    try {
	        System.out.println("Procurando pelo servico JavaSpace...");
	        Lookup finder = new Lookup(JavaSpace.class);
	        this.conexaoComOEspaco = (JavaSpace) finder.getService();
	        if (this.conexaoComOEspaco == null) {
	            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
	            System.exit(-1);
	        }
	        System.out.println("Conectado com o espaço de Tupla");
	        System.out.println(this.conexaoComOEspaco);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean UsuarioJaExisteNoEspaco(String nomeUsuario) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		TuplaUsuario modelo = new TuplaUsuario();
	    modelo.nome = nomeUsuario;
	    
        TuplaUsuario usuario = (TuplaUsuario) this.conexaoComOEspaco.read(modelo, null, 100);
        if (usuario != null) {
            System.out.println("\nO usuário " + nomeUsuario + " já existe\n");
        	return true;
        }else {
            System.out.println("\nEsse usuário ainda não existe");
            return false;
        }
	}
	
	public String[] ListarUsuariosQueJaExistem() throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		// o retorno tem que ser um array de strings
		// tem que pegar todos os usuário da tupla, armazenar numa lista e
		// depois preciso devolver todos para o espaço de tupla
//		List lista = new ArrayList();
//		String[] strings = (String[]) lista.toArray (new String[lista.size()]);
		return new String[] {"Maria", "João", "Xuxa", "Túlio Maravilha"};
	}
	
	public void GravarUsuarioNoEspaco(String nomeUsuario) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		TuplaUsuario modelo = new TuplaUsuario();
	    modelo.nome = nomeUsuario;
	    this.conexaoComOEspaco.write(modelo, null, Lease.FOREVER);
        System.out.println("Usuário gravado no espaço com sucesso!");
	}
	
	public void GravarMensagemNoEspaco(String remetente, String destinatario, String conteudo) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		TuplaMensagem modelo = new TuplaMensagem();
	    
		modelo.remetente = remetente;
	    modelo.destinatario = destinatario;
	    modelo.conteudo = conteudo;
	    
	    this.conexaoComOEspaco.write(modelo, null, Lease.FOREVER);
	    System.out.println(remetente + " > " + destinatario + " > " + conteudo);
        System.out.println("Mensagem gravada no espaço com sucesso!");
	}	
}
