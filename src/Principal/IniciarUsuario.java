package Principal;

import View.TelaUsuario;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.rmi.RemoteException;

import ControladorTupla.Controlador;
import Tuplas.TuplaMensagem;


public class IniciarUsuario {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |javax.swing.UnsupportedLookAndFeelException ex) {
        	System.err.println(ex);
        }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Controlador controlador = new Controlador();
					String nome = iniciarTela(controlador);
					controlador.GravarUsuarioNoEspaco(nome);
					TelaUsuario window = new TelaUsuario(nome, controlador);
					window.frameTelaUsuario.setVisible(true);
					ReceberMensagem(controlador, window, nome);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		    private void ReceberMensagem(Controlador controlador, TelaUsuario tela, String nome){
		        new Thread(() -> {
		            System.out.println("Rodando a thread de recepção de mensagens");
		            while (true){
		                try {
		                	TuplaMensagem mensagemRecebida = controlador.LerMensagemDoEspaco(nome);
		                	if (mensagemRecebida != null) {
		                		tela.AtualizarChat(mensagemRecebida);
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

//		    private void AtualizarListaDeUsuario(Controlador controlador, TelaUsuario tela){
//		        new Thread(() -> {
//		            System.out.println("Rodando a thread de recepção de mensagens");
//		            while (true){
//		                try {
//		                	
//		                } catch (TransactionException e) {
//		                    e.printStackTrace();
//		                } catch (UnusableEntryException e) {
//		                    e.printStackTrace();
//		                } catch (RemoteException e) {
//		                    e.printStackTrace();
//		                } catch (InterruptedException e) {
//		                    e.printStackTrace();
//		                }
//		            }
//		        }).start();
		    }
		    
			private String iniciarTela(Controlador controlador) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
			    String nomeUsuario = "";

			    String address = "127.0.0.1";
			    int port = 5000;
			    
				do {
			        nomeUsuario = showInputDialog(null, "Digite seu nome", "", PLAIN_MESSAGE);
			    } while ("!!!@@@###$$$!@#$".equals(nomeUsuario) || nomeUsuario.isEmpty() || controlador.UsuarioJaExisteNoEspaco(nomeUsuario));
			    do {
			        try {
			            address = showInputDialog(null, "Digite o endereço do servidor", "", PLAIN_MESSAGE);
			        } catch (NumberFormatException e) {
			            showMessageDialog(null, "Digite um endereço e um número de porta válidos", "", ERROR_MESSAGE);
			        }
			    } while (!validateConection(address));
			    
			    return nomeUsuario;
			}
			
		    public boolean validateConection(String address) {
		        return true;
		    }
		});
	}
}
