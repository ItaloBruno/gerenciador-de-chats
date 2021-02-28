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
            System.out.println("\nO usuário " + nomeUsuario + " já existe! Tente um outro nome!\n");
        	return true;
        }else {
            System.out.println("\nEsse usuário ainda não existe! Pode criar um usuário com esse nome ;)");
            return false;
        }
	}
	
	public String[] ListarUsuariosQueJaExistem() throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		// o retorno tem que ser um array de strings
		// tem que pegar todos os usuário da tupla, armazenar numa lista e
		// depois preciso devolver todos para o espaço de tupla
//		List lista = new ArrayList();
//		String[] strings = (String[]) lista.toArray (new String[lista.size()]);
//		ArrayList<TuplaUsuario> usuariosExistentes= new ArrayList<TuplaUsuario>();
//		ArrayList<String> nomes = new ArrayList<String>();
//		boolean terminou = false;
//		while (!terminou) {
//			TuplaUsuario usuario = LerUsuariosDoEspaco();
//			if (usuario != null) {
//				usuariosExistentes.add(usuario);
//				nomes.add(usuario.nome);
//			}else {
//				terminou = true;
//			}
//		}
//		
//		for (TuplaUsuario usuarioParaAdicionar : usuariosExistentes) {
//			GravarUsuarioNoEspaco(usuarioParaAdicionar.nome);
//		}
//		String[] nomesParaRetornar = new String[nomes.size()];
//		nomes.toArray(nomesParaRetornar);
//		return nomesParaRetornar;
		return new String[] {"maria", "xuxa"};
	}
	
	public void GravarUsuarioNoEspaco(String nomeUsuario) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		TuplaUsuario modelo = new TuplaUsuario();
	    modelo.nome = nomeUsuario;
	    this.conexaoComOEspaco.write(modelo, null, Lease.FOREVER);
        System.out.println("Usuário " + nomeUsuario + " gravado no espaço com sucesso!");
	}
	
    public TuplaUsuario LerUsuariosDoEspaco() throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
		TuplaUsuario modelo = new TuplaUsuario();
	              
		TuplaUsuario usuario = (TuplaUsuario) this.conexaoComOEspaco.take(modelo, null, 1000);

        if (usuario != null) {
            return usuario;
        }
		return usuario;
    }
	
	public void GravarMensagemNoEspaco(String remetente, String destinatario, String conteudo) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		TuplaMensagem modelo = new TuplaMensagem();
	    
		modelo.remetente = remetente;
	    modelo.destinatario = destinatario;
	    modelo.conteudo = conteudo;
	    
	    this.conexaoComOEspaco.write(modelo, null, Lease.FOREVER);
        System.out.println("Mensagem gravada no espaço com sucesso!");
	}
	
    public TuplaMensagem LerMensagemDoEspaco(String nomeUsuario) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
		TuplaMensagem modelo = new TuplaMensagem();
	    
	    modelo.destinatario = nomeUsuario;
	           

        TuplaMensagem mensagem = (TuplaMensagem) this.conexaoComOEspaco.take(modelo, null, 10000);

        if (mensagem != null) {
            return mensagem;
        }
		return mensagem;
    }
}
