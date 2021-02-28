package MVP;

import java.awt.HeadlessException;
import java.rmi.RemoteException;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

public class EspiaoPeloTerminal {

	public static void main(String[] args) throws HeadlessException, RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Espiao espiao = new Espiao();
	}
}
