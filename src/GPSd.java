import java.io.*;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.location.*;
import javax.microedition.midlet.*;

public class GPSd extends MIDlet implements LocationListener {

	private OutputStream os;

	public void destroyApp(boolean unconditional) {
	}

	public void pauseApp() {
	}

	public void startApp() {
		try {
			ServerSocketConnection scn = (ServerSocketConnection)Connector.open("socket://:2947");
			StreamConnection sc = scn.acceptAndOpen();
			os = sc.openOutputStream();
			os.write("{\"class\":\"VERSION\",\"proto_major\":3}\n".getBytes());
			os.flush();
		} catch (IOException e) {
			Display.getDisplay(this).setCurrent(new Alert(e.getMessage()));
		}

		Location l = LocationProvider.getLastKnownLocation();
		if (l != null)
			locationUpdated(null, l);
		try {
			LocationProvider.getInstance(null).setLocationListener(this, -1, -1, -1);
		} catch (LocationException e) {
			Display.getDisplay(this).setCurrent(new Alert(e.getMessage()));
		}
	}

	public void locationUpdated(LocationProvider provider, Location location) {
		Coordinates c = location.getQualifiedCoordinates();
		String s = "{"+
			"\"class\":\"POLL\","+
			"\"timestamp\":"+location.getTimestamp()/1000.0+","+
			"\"fixes\":[{"+
				"\"lat\":"+c.getLatitude()+","+
				"\"lon\":"+c.getLongitude()+","+
				"\"alt\":"+c.getAltitude()+","+
				"\"track\":"+location.getCourse()+","+
				"\"speed\":"+location.getSpeed()+","+
				"\"mode\":0}],"+
			"\"skyviews\":[]}\n";
		try {
			os.write(s.getBytes());
			os.flush();
		} catch (IOException e) {
			Display.getDisplay(this).setCurrent(new Alert(e.getMessage()));
		}	
	}

	public void providerStateChanged(LocationProvider provider, int newState) {
	}

}
