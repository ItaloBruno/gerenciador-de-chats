package Mensageria;

import java.rmi.Remote; 
import java.rmi.RemoteException;

public interface ServicoMensageria extends Remote {

  void EnviarMensagemProMiddlewareDeMensagem(String remetente, String destinatario, String conteudo) throws  RemoteException;

}
