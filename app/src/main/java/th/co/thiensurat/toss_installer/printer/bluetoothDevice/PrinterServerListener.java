package th.co.thiensurat.toss_installer.printer.bluetoothDevice;

import java.net.Socket;

public interface PrinterServerListener {
    public void onConnect(Socket socket);
}
