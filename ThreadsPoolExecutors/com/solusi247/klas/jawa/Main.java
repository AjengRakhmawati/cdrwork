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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
	
	
	String ref_city = "/app/xl/nts/ref/dim_subscriber_prefix.dat";	
	Map<String, SubscriberPrefix> ref = new TreeMap<String, SubscriberPrefix>();	
	static int max_len = 0;
	static SubscriberPrefix het = new SubscriberPrefix("","","");
	//static String data_file = "/app/xl/nts/data/sms_ocs_20110518.unl";
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static void main(String[] args) throws Exception {
		System.out.println("start: " +  dateFormat.format(new Date(System.currentTimeMillis())));
		Main test = new Main();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		ArrayList<ArrayList<String>> nnmain = new ArrayList<ArrayList<String>>();
		
		// ambil data file dari direktori
		String filesrc = "/home/s247/Documents/cdrwork/cdr/data",
				filedst = "/home/s247/Documents/cdrwork/cdr/output/";
		
		File folder = new File(filesrc);
		
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
			
		    if (file.isFile()) {
		        ArrayList<String> filenametmp = test.recurseDirectory(filesrc, file.getName());
		        executor.submit(()->{
					  String threadName = Thread.currentThread().getName();
					  System.out.println(" "+threadName);
					  
				  });
		        test.verrijkt(filenametmp);
		        nnmain.add(filenametmp);
		        
		        Map<String, SubscriberPrefix> ref = new TreeMap<String, SubscriberPrefix>();
		        String complete = filenametmp.get(0);
		        int start = complete.lastIndexOf("/") + 1;
		        int end = complete.lastIndexOf(".");
		        end = start < end ? end : complete.length();
		        String name = complete.substring(start, end);
		        //String name = FilenameUtils.removeExtension(file.getName());
				CDRWorker cdrw = new CDRWorker(filenametmp.get(0), filedst+name+".extracted", ref, max_len);
				cdrw.process();
				
				  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		} 
		try {
		    System.out.println(" attempt to shutdown executor");
		    executor.shutdown();
		    executor.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
		    System.err.println("tasks interrupted");
		}
		finally {
		    if (!executor.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		    }
		    executor.shutdownNow();
		    System.out.println("shutdown finished");
		}

		System.out.println("ended: " +  dateFormat.format(new Date(System.currentTimeMillis())));
	}
	
	public void verrijkt(ArrayList<String> ll) { 
			  Map<String, CDRWorker> deLijst = new HashMap<String, CDRWorker>();
			  
			  // TODO
			  //  create and starting instan dari thread, 
			  // beserta parameter yg dibutuhkan (construktor)
			 // di akhir, dipastikan nggak ada thread yg gantung 9thread betul2 mati)
			  // starting
			  for (String dd: ll) {
		 		  CDRWorker _worker = null;
				
		 		  deLijst.put(dd, _worker);
		 		  System.out.print(" started: "+dd);
		 		   
		 		  // simpan, untuk shuting down thread
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
