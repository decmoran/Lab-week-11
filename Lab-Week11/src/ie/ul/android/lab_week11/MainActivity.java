/* Student name: 
 * Student id:
 * Partner name:
 * Partner id:
 */

package ie.ul.android.lab_week11;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button guiDownloadButton;
	Button threadDownloadButton;
	ProgressBar myProgress;
	ProgressBar downloadProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		guiDownloadButton= (Button) findViewById(R.id.gui_download_button);
		threadDownloadButton= (Button) findViewById(R.id.thread_download_button);
		myProgress = (ProgressBar) findViewById(R.id.progressBar);
		downloadProgress = (ProgressBar) findViewById(R.id.downloadProgress);
		
		
		guiDownloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), R.string.downloadStarted, Toast.LENGTH_SHORT).show();
				Downloader.download("http://androidnetworktester.googlecode.com/files/100kb.txt");
		        Toast.makeText(getApplicationContext(), R.string.downloadCompleted, Toast.LENGTH_SHORT).show();
		           
			}
			
		});

		threadDownloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				DownloadFile downloadFile = new DownloadFile();
				downloadFile.execute("http://androidnetworktester.googlecode.com/files/100kb.txt");
				
				/*
				 * Alternative method: using a new class which extends the Thread class.
				 */
				//DownloadThread downloadThread = new DownloadThread("http://www.rovingnetworks.com/resources/download/111/HTTP_mode");
				//downloadThread.start();
				
				/*
				 * Alternative method: instantiate a new Thread object by passing a runnable object 
				 */
				/*
				new Thread(new Runnable() {

					@Override
					public void run() {
						Downloader.download("http://www.rovingnetworks.com/resources/download/111/HTTP_mode");
						
					}
					
				}).start();
				*/
			}
			
		});

		
		myProgress.setIndeterminate(true);
	}
	
	
	private class DownloadFile extends AsyncTask<String, Integer, Void> {
		
		@Override
	    protected Void doInBackground(String... sUrl) {
			
			try {
	            URL url = new URL(sUrl[0]);
	            URLConnection connection = url.openConnection();
	            connection.connect();
	            
	            
	            
	            
	            /* Note: 
	             * Below code extracts the filename from the content-disposition. This is useful if the name of the file to be down loaded 
	             * is not directly contained in the URL, but the existence of a content-disposition is not guaranteed! 
	             */
	            Map<String, List<String>> Map = connection.getHeaderFields();
	            List<String> content_disposition = Map.get("content-disposition");
	            String currentMember;
	            String filename = "defaultName";
	            for (int i=0;i<content_disposition.size();i++) {
	            	currentMember=content_disposition.get(i);
	            	if (currentMember.contains("filename=")) {
	            		int start = currentMember.indexOf('"')+1;
	            		int end = currentMember.lastIndexOf('"'); 
	            		filename = content_disposition.get(i).substring(start, end);
	            	}
	            }
	            
	            InputStream input = new BufferedInputStream(url.openStream());
	            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mydownloads" + File.separator + filename);
	            f.mkdirs();
	            f.delete();
	           

	            OutputStream output = new FileOutputStream(f);

	            byte data[] = new byte[1024];
	            long total = 0;
	            int count;
	            int fileLength = connection.getContentLength();
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                publishProgress((int)((100*total)/fileLength));
	                output.write(data, 0, count);
	            }

	            output.flush();
	            output.close();
	            input.close();
	        } catch (Exception e) {
	        	//simulate download
	        	for (int i=1; i<100; i++) {
	        		try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        	}
	        }
			return null;
			
	    }

		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			//III. Download started toast
            Toast.makeText(getApplicationContext(), R.string.downloadStarted, Toast.LENGTH_SHORT).show();
			
		}


		@Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result );
			//IV. Download Completed toast
			Toast.makeText(getApplicationContext(), R.string.downloadCompleted, Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			
			super.onProgressUpdate(values);
			downloadProgress.setProgress(values[0]);
		}
		
		
	}
	
	
}
