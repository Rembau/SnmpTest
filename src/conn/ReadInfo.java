package conn;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadInfo {
	static Properties p;
	static {
		p=new Properties();
		  InputStream in = null;
	      try {
	          in = ReadInfo.class.getResource("sqldata.properties").openStream();
	      } catch (IOException ioe) {
	          ioe.printStackTrace();
	      }
	      try {
			p.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static String getString(String property){
		return 	p.getProperty(property);
	}
	static int getInt(String property){
		return Integer.parseInt(p.getProperty(property));
	}
}
