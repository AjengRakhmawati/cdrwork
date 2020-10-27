package com.solusi247.klas.jawa;
import java.awt.List;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class CDRWorker extends Thread {

	Map<String, SubscriberPrefix> ref = new TreeMap<String, SubscriberPrefix>();
	String src_name = null;
	String dest_name = null;
	 int max_len = 0;
	 
	public CDRWorker(String source_fname, String dest_fname, Map<String, SubscriberPrefix> ref, int _max_len) {
		this.ref = ref;
		this.max_len = _max_len;
		this.src_name = source_fname;
		this.dest_name = dest_fname;
	}

	
	private  SubscriberPrefix getSubscriber(String msisdn) {
		
		int idx = max_len;;
		String key = msisdn.substring(0,idx);
		
		//System.out.println("_i_ "+ key);
		
		
		while (!ref.containsKey(key)) {
			if (idx>5) idx = idx - 1;
			else break;
			
			key = msisdn.substring(0,idx);
			
			//System.out.println("_0_ "+ key);
		}
		
		return  ref.get(key);
	}
	
	// TODO
	// MODIFY process, untuk membaca data file dan mengkopi sebagian data ke file lain
	// contoh nama file source  data2008xxxx_1000xxx_00xx.unl, dan
	// contoh nama file target   data2008xxxx_1000xxx_00xx.unl.done
	
	/* bentuk content dari data file:
	sms|11500492113089093937|servicec|20080331103712|628388000197|628388880000|628310002101|0|60||0|0|0|0|0|197710|0|0|0|0|
	sms|7646456642302845264|servicec|20080331103720|628382862245|6285294573555|628310003101|0|60||0|0|0|0|0|5790|0|0|0|0|
*/
	
	// file baru/target berisi 
	//  data kolom ke 2, 4,5,7, dari file source, dgn terkecualian kolom ke-2-nya
	// 11500492113089093937|20080331103712|628388000197|628310002101
	// dgn data  dimana kolom keduanya 20080331103712 diganti
	// dengan data unix timenya (long, epoch unix time, miliis start 1 juni '71)

	public void process() throws Exception {
		
		LineNumberReader lineCounter = new LineNumberReader(
				new InputStreamReader(new FileInputStream(src_name)));
		
		String nextLine = null;

		FileOutputStream fos = new FileOutputStream(dest_name);
		FileChannel fco = fos.getChannel();
			
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		try {
			
			while ((nextLine = lineCounter.readLine()) != null) {
			
				//System.out.println(nextLine);
				
				String ss[] = nextLine.split("[|]");
				
				String msisdn = ss[4];
				
				SubscriberPrefix hh = getSubscriber(msisdn);
				
				//System.out.println("ss[4]=" + msisdn +" "+ hh._prefix_id +" "+ hh.get_city());

				//if () {
				nextLine = nextLine + "|"+ hh.get_city() +"\n";
				//}

				byte bb[] = nextLine.getBytes();

				buffer = ByteBuffer.wrap(bb);

				fco.write(buffer);

				buffer.clear();

			}
			
		
			System.out.println("Total number of line in this file " + lineCounter.getLineNumber());
			
		} catch (Exception done) {
			done.printStackTrace();
		} finally {
			// closed open stream
		}
	}
 
	public void run() {
		try {
		 process();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
  
	
	
}
