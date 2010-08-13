import java.io.*;
import java.util.*;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;

public class GPSd extends MIDlet {

	public void destroyApp(boolean unconditional) {
	}

	public void pauseApp() {
	}

	public void startApp() {
		try {
			ServerSocketConnection scn = (ServerSocketConnection)Connector.open("socket://:2947");
			StreamConnection sc = scn.acceptAndOpen();
			OutputStream os = sc.openOutputStream();
			String s;

			s = "{\"class\":\"VERSION\",\"proto_major\":3}";
			os.write(s.getBytes());

			long timestamp = new Date().getTime();
			s = "{\"class\":\"POLL\",\"timestamp\":"+timestamp/1000.0+",\"fixes\":|{\"mode\":0}|,\"skyviews\":||}";
			os.write(s.getBytes());

			os.close();
			sc.close();
			scn.close();
		} catch (IOException e) {
			Display.getDisplay(this).setCurrent(new Alert(e.getMessage()));
		}
	}

}
