package MVP;

import java.rmi.Remote; 
import java.rmi.RemoteException;

public interface InverterItf extends Remote {

  String inverter(String msg) throws  RemoteException;
  void EnviarMensagemProMiddlewareDeMensagem(String conteudo);

}
