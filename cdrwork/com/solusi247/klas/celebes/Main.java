package com.solusi247.klas.celebes;

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

public class Main {
	
	
	String ref_city = "/app/xl/nts/ref/dim_subscriber_prefix.dat";
	
	static int max_len = 0;
		
	//static String data_file = "/app/xl/nts/data/sms_ocs_20110518.unl";
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static void main(String[] args) throws Exception {
	
		
		System.out.println("start: " +  dateFormat.format(new Date(System.currentTimeMillis())));
		
		Main test = new Main();
		
			
		ArrayList<String>  nn = test.recurseDirectory("/app/xl/nts/data", "sms_ocs_20110518_aa_unl.x");

		test.verrijkt(nn);
		
		System.out.println("ended: " +  dateFormat.format(new Date(System.currentTimeMillis())));
	}
	
	
	// starting concurent thread 
  public void verrijkt(ArrayList<String> ll) {
		  
	  // TODO
	  //  create and starting instan dari thread, 
	  // beserta parameter yg dibutuhkan (construktor)
	 // di akhir, dipastikan nggak ada thread yg gantung 9thread betul2 mati)
	  
	  
		  Map<String, ODSWorker> deLijst = new HashMap<String, ODSWorker>();
			  
		  for (String dd: ll) {
			 // Map<String, SubscriberPrefix> ref0 = 

			  //  create instan dari thread, beserta parameter yg dibutuhkan (construktor)
	 		  ODSWorker _worker = null;
			  
	 				  
	 				  // simpan, untuk shuting down thread
			  deLijst.put(dd, _worker);
			  System.out.println("started: "+ dd);
		  }
			  
		  // berhentikan tread, 
		  for (String dd: ll) {
			  
			  ODSWorker tt = deLijst.get(dd);
			  
			  
			  if (tt != null && tt.isAlive()) {
				  
				  try {
					  
					  System.out.println("waiting join: "+ dd);
					  tt.join();
					  System.out.println("joined: "+ dd);
					  
					  
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
				  
			  }
			  
		  }
		  
		  
		  
	  }
	  
	  
	
	
	// scanning direktori
	
	  public  ArrayList<String> recurseDirectory(String dir_path, String qm) throws Exception {
		  
		  File file = new File(dir_path);
		 
		  
		  ArrayList<String> ll = new ArrayList<String>();
		  
		  for (String s: file.list() ) {
			  
			 if( s.contains(qm))
				 ll.add(dir_path +"/"+ s);
		  }
        
        return ll;
    }

	
	
}
