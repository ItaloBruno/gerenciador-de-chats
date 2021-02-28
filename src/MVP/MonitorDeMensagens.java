package MVP;

import java.io.IOException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeoutException;

import Mensageria.ControladorMensageria;
import Mensageria.ServicoMensageria;
import Tuplas.TuplaEspiao;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import com.rabbitmq.client.Channel;


public class MonitorDeMensagens {
	private ControladorMensageria controladora;
	private Channel canal;
	
	public MonitorDeMensagens() throws IOException, TimeoutException {
		this.controladora = new ControladorMensageria();
		this.canal = this.controladora.ConectarComOTopico();
		this.ReceberMensagem();
	}

    private void ReceberMensagem(){
        new Thread(() -> {
            System.out.println("\nRodando a thread de recepção de mensagens do serviço de mensageria\n");
            while (true){
            	try {
					this.controladora.LerMensagemDoTopico(this.canal);;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }).start();
    }
}
