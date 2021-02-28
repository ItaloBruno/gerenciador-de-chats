package Executores;

import java.awt.HeadlessException;
import java.rmi.RemoteException;

import MVP.Usuario;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

public class ChatPeloTerminal {

	public static void main(String[] args) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Usuario vai = new Usuario();
	}

}
