package th.co.thiensurat.toss_installer.contract.printer.utils;

import java.net.Socket;

public interface PrinterServerListener {
    public void onConnect(Socket socket);
}
