package Executores;

import java.awt.HeadlessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import MVP.Espiao;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

public class EspiaoPeloTerminal {

	public static void main(String[] args) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException, NotBoundException {
		Espiao espiao = new Espiao();
	}
}
