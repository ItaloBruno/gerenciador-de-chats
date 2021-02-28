package MVP;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;

import java.awt.HeadlessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import ControladorTupla.Controlador;
import Mensageria.ServicoMensageria;
import Tuplas.TuplaEspiao;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

public class Espiao {

	public Controlador controladora;
	private ServicoMensageria servidorRemoto;
	private ArrayList<String> palavrasMonitoradas = new ArrayList<String>();
	
	public Espiao() throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, NotBoundException {
		this.IniciarMonitoramento();
		this.ConectarComServidorRemoto();
		this.controladora = new Controlador();
		this.controladora.GravarUsuarioNoEspaco("espiao");
		
		this.ReceberMensagem();
	}
	
	private void IniciarMonitoramento() {
		String palavra = new String();
        Scanner scanner = new Scanner(System.in);
        boolean validacao = true;
       
        while (validacao) {
        	System.out.println("\nAdicione a palavra que você que monitorar (Aperte ENTER sem digitar algo para rodar o espião): ");
        	palavra = (String) scanner.nextLine();

        	if (!palavra.isEmpty()){
             	System.out.println("Palavra " + palavra + " adicionada");
        		 this.palavrasMonitoradas.add(palavra);
        	 }else {
        		 validacao = false;
        	 }
        }
        
        System.out.println("\nPalavras a serem monitoradas: ");
        for (String p : this.palavrasMonitoradas) {
        	System.out.println(p);
        }
        System.out.println("\n\n");
	}
	
	private void ConectarComServidorRemoto() throws RemoteException, NotBoundException {
      Registry registrador = LocateRegistry.getRegistry(20394);           		      
      this.servidorRemoto = (ServicoMensageria) registrador.lookup("servidor-de-mensageria");
	}
	
    private void ReceberMensagem(){
        new Thread(() -> {
            System.out.println("Rodando a thread de recepção de mensagens");
            while (true){
                try {
                	TuplaEspiao mensagemRecebida = controladora.LerMensagemParaOEspiaoDoEspaco();
                	if (mensagemRecebida != null) {
                	  System.out.println("\nMensagem para o espião...");
                      System.out.println("De: " + mensagemRecebida.remetente);
                      System.out.println("Para: " + mensagemRecebida.destinatario);
                      System.out.println("Conteúdo: " + mensagemRecebida.conteudo);
                      
                      if (this.MensagemTemPalavraMonitorada(mensagemRecebida.conteudo)){
                    	  System.out.println("A MENSAGEM TEM ALGUMAS DAS PALAVRAS QUE ESTÃO SENDO MONITORADAS");
                    	              		 
            		      System.out.println("Servidor Remoto Localizado!\n");
            		      this.servidorRemoto.EnviarMensagemProMiddlewareDeMensagem(mensagemRecebida.remetente, mensagemRecebida.destinatario, mensagemRecebida.conteudo);            		      
                      }
                	}
                } catch (TransactionException e) {
                    e.printStackTrace();
                } catch (UnusableEntryException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
	private boolean MensagemTemPalavraMonitorada(String mensagem) {
		boolean resultadovalidacao = false;

		for (String palavraMonitorada : this.palavrasMonitoradas) {
			if (mensagem.contains(palavraMonitorada)) {
				resultadovalidacao = true;
			}
		}

		return resultadovalidacao;
	}
}
