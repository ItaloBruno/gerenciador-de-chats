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
import Tuplas.TuplaEspiao;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

public class Espiao {

	public Controlador controladora;
	private ArrayList<String> palavrasMonitoradas = new ArrayList<String>();
	
	public Espiao() throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		this.IniciarMonitoramento();
		this.controladora = new Controlador();
		this.controladora.GravarUsuarioNoEspaco("espiao");
		
		this.ReceberMensagem();
	}
	
	private void IniciarMonitoramento() {
		String palavra = new String();
        Scanner scanner = new Scanner(System.in);
        boolean validacao = true;
       
        while (validacao) {
        	System.out.println("\nAdicione a palavra que você que monitorar: ");
        	palavra = (String) scanner.nextLine();

        	if (!palavra.isEmpty() && !palavra.equals("iniciar monitoramento")) {
             	System.out.println("\nPalavra " + palavra + " adicionada");
        		 this.palavrasMonitoradas.add(palavra);
        	 }else {
        		 validacao = false;
        	 }
        }
        
        System.out.println("\nPalavras a serem monitoradas: ");
        for (String p : this.palavrasMonitoradas) {
        	System.out.println("\n" + p);
        }
	}
	
    private void ReceberMensagem(){
        new Thread(() -> {
            System.out.println("\nRodando a thread de recepção de mensagens\n");
            while (true){
                try {
                	TuplaEspiao mensagemRecebida = controladora.LerMensagemParaOEspiaoDoEspaco();
                	if (mensagemRecebida != null) {
                	  System.out.println("\nMensagem para o espião...");
                      System.out.println("\nDe: " + mensagemRecebida.remetente);
                      System.out.println("Para: " + mensagemRecebida.destinatario);
                      System.out.println("Conteúdo: " + mensagemRecebida.conteudo);
                      
                      if (this.MensagemTemPalavraMonitorada(mensagemRecebida.conteudo)){
                    	  System.out.println("TEM PALAVRA A SER MONITORADA");
                    	  
            		      Registry registry = LocateRegistry.getRegistry(20394); 
            		 
            		      // Looking up the registry for the remote object 
            		      
            		      InverterItf stub = (InverterItf) registry.lookup("Inverterltf");
            		 
            		      System.out.println("Objeto Localizado!");
            		      String resultado = stub.inverter(mensagemRecebida.conteudo);
            		      stub.EnviarMensagemProMiddlewareDeMensagem(mensagemRecebida.conteudo);

            		      System.out.println(resultado);
            		      
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
                } catch (NotBoundException e) {
					// TODO Auto-generated catch block
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
