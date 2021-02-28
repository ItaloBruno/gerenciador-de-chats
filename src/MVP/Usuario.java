package MVP;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.Scanner;

import ControladorTupla.Controlador;
import Tuplas.TuplaMensagem;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

public class Usuario {

	public Controlador controladora;
	private String meuNomeDeUsuario;
	private String destinatario;
	
	public Usuario() throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		this.controladora = new Controlador();
		this.setarMeuNomeDeUsuario(this.iniciarChat(controladora));
		this.setarNomeDoDestinatario("");
		this.controladora.GravarUsuarioNoEspaco(obterMeuNomeDeUsuario());
		
		this.EnviarMensagem();
		this.ReceberMensagem();
	}
	
	public String obterMeuNomeDeUsuario() {
		return meuNomeDeUsuario;
	}
	
	public void setarMeuNomeDeUsuario(String meuNomeDeUsuario) {
		this.meuNomeDeUsuario = meuNomeDeUsuario;
	}
	
	public String obterNomeDoDestinatario() {
		return destinatario;
	}

	public void setarNomeDoDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	private String iniciarChat(Controlador controlador) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
	    String nomeUsuario = "";    
		do {
			nomeUsuario = showInputDialog(null, "Digite seu nome", "", PLAIN_MESSAGE);
	    } while ("!!!@@@###$$$!@#$".equals(nomeUsuario) || nomeUsuario.isEmpty() || controlador.UsuarioJaExisteNoEspaco(nomeUsuario));
	    
	    return nomeUsuario;
	}
	
    private void ReceberMensagem(){
        new Thread(() -> {
            System.out.println("\nRodando a thread de recepção de mensagens\n");
            while (true){
                try {
                	TuplaMensagem mensagemRecebida = controladora.LerMensagemDoEspaco(this.obterMeuNomeDeUsuario());
                	if (mensagemRecebida != null) {
                      System.out.println("\nDe: " + mensagemRecebida.remetente);
                      System.out.println("Para: " + mensagemRecebida.destinatario);
                      System.out.println("Conteúdo: " + mensagemRecebida.conteudo);
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
    
    private void EnviarMensagem() {
        new Thread(() -> {
            System.out.println("\nRodando a thread de envio de mensagens");
            Scanner scanner = new Scanner(System.in);
            while (true){
                try {
                	String destinatario = "";
            		do {
            			System.out.print("\n\nDigite o destinatário da mensagem: ");
                        destinatario = scanner.nextLine();
            	    } while ("!!!@@@###$$$!@#$".equals(destinatario) || destinatario.isEmpty());
            		
                    System.out.print("Digite o texto da mensagem: ");
                    String mensagem = scanner.nextLine();
                    if (mensagem == null || mensagem.equals("")) {
                        System.exit(0);
                    }
                    this.controladora.GravarMensagemNoEspaco(this.obterMeuNomeDeUsuario(), destinatario, mensagem);
                } catch (TransactionException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (UnusableEntryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }).start();
    }

}
