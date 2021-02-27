package View;
import Tuplas.TuplaMensagem;
import Tuplas.TuplaUsuario;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import ControladorTupla.Controlador;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class TelaUsuario {
	private String nomeUsuario;
	private String destinatario;
	private Controlador controlador;
	
	/**
	 * Create the application.
	 */
	public TelaUsuario(String nomeDoUsuario, Controlador controlador) {
		this.nomeUsuario = nomeDoUsuario;
		this.controlador = controlador;
		incializarComponentesDaTela();
	}
	
	private void incializarComponentesDaTela() {
		frameTelaUsuario = new JFrame();
		frameTelaUsuario.setBounds(100, 100, 594, 444);
		frameTelaUsuario.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameTelaUsuario.getContentPane().setLayout(null);
		
		lblListaDeUsuarios = new JLabel("Lista de Usuários");
		lblListaDeUsuarios.setBounds(30, 12, 133, 28);
		frameTelaUsuario.getContentPane().add(lblListaDeUsuarios);
			
		textMensagemParaEnviar = new JTextField();
		textMensagemParaEnviar.setText("Oi meu chapa");
		textMensagemParaEnviar.setBounds(30, 305, 410, 99);
		frameTelaUsuario.getContentPane().add(textMensagemParaEnviar);
		textMensagemParaEnviar.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Passos:
				// - Pegar o valor que tem na caixa de texto
				// - Pegar quem é o destinatario
				// - Criar uma tupla com as informações do remetente, destinatário e a mensagem para enviar (se não for vazia)
				// - Enviar a tupla de mensagem para o espaço
				// - Enviar para o tópico no rabbitMQ através de um método no RMI
				String mensagemDigitada = textMensagemParaEnviar.getText();
				System.out.println(mensagemDigitada);
				textMensagemParaEnviar.setText("");
				
				try {
					controlador.GravarMensagemNoEspaco(nomeUsuario, destinatario, mensagemDigitada);
				} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				txtHistorico.setText(nomeUsuario + " : " + mensagemDigitada);
			}
		});
		btnEnviar.setBounds(465, 305, 100, 27);
		frameTelaUsuario.getContentPane().add(btnEnviar);
		
		btnLimpar = new JButton("Limpar");
		btnLimpar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textMensagemParaEnviar.setText("");
			}
		});
		btnLimpar.setBounds(465, 377, 100, 27);
		frameTelaUsuario.getContentPane().add(btnLimpar);
		
		txtHistorico = new JTextArea();
		txtHistorico.setEditable(false);
		txtHistorico.setBounds(175, 46, 390, 241);
		frameTelaUsuario.getContentPane().add(txtHistorico);

		listaDeUsuarios = new JList();
		listaDeUsuarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				Passos:
//				- pegar o nome do usuario selecionado
//				- pegar todas as tuplas que contém 
				
				int index = obterIndiceDaListaDeUsuarios(e);
				JList list = (JList)e.getSource();
				Object listElement = listaDeUsuarios.getModel().getElementAt(index);
	            destinatario = listElement.toString();
	            System.out.println(destinatario);
			}
		});
		try {
			listaDeUsuarios.setModel(new AbstractListModel() {
				String[] values = controlador.ListarUsuariosQueJaExistem();
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		listaDeUsuarios.setBounds(30, 50, 133, 237);
		frameTelaUsuario.getContentPane().add(listaDeUsuarios);
		lblMeuNome = new JLabel(nomeUsuario);
		lblMeuNome.setBounds(175, 8, 115, 37);
		frameTelaUsuario.getContentPane().add(lblMeuNome);
	}

	private int obterIndiceDaListaDeUsuarios(java.awt.event.MouseEvent evento){
        JList listaDeUsuariosExistentes = (JList) evento.getSource();
        int indiceEncontrado = listaDeUsuariosExistentes.locationToIndex(evento.getPoint());
        return indiceEncontrado;
    }
	
	public void AtualizarChat(TuplaMensagem mensagem) {
		String novoTexto = this.txtHistorico.getText(); 
		novoTexto = novoTexto + "\n" + mensagem.remetente +" : " + mensagem.conteudo;
		this.txtHistorico.setText(novoTexto);
	}
	
	public JFrame frameTelaUsuario;
	private JTextField textMensagemParaEnviar;
	private JButton btnEnviar;
	private JLabel lblListaDeUsuarios;
	private JButton btnLimpar;
	private JTextArea txtHistorico;
	private JList listaDeUsuarios;
	private JLabel lblMeuNome;
}
