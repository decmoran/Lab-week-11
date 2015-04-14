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

import android.os.Environment;

public class Downloader {

	public static void download(String fileLocation) {
		
		try {
            URL url = new URL(fileLocation);
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
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"GUIdownloads" + File.separator + filename);
            f.mkdirs();
            f.delete();
           

            OutputStream output = new FileOutputStream(f);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
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
	}

}
