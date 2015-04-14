package ie.ul.android.lab_week11;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.widget.Toast;

public class DownloadThread extends Thread {

	private String fileLocation;
	
	public DownloadThread(String url) {
		fileLocation = url;
	}
	
	@Override
	public void run() {
		
		Downloader.download(fileLocation);
	}

	
}
