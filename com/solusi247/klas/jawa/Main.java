package com.solusi247.klas.jawa;//cdrworker

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
import java.security.Key;
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

//	String ref_city = "/home/s247/Documents/cdrwork/cdr/ref/dim_subscriber_prefix.dat";
	// Map<String, SubscriberPrefix> ref = new TreeMap<String, SubscriberPrefix>();
	Map<String, SubscriberPrefix> ref = new TreeMap<String, SubscriberPrefix>();
	static int max_len = 0;

	static SubscriberPrefix het = new SubscriberPrefix("", "", "");

	// static String data_file = "/app/xl/nts/data/sms_ocs_20110518.unl";
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static void main(String[] args) throws Exception {

		System.out.println("start: " + dateFormat.format(new Date(System.currentTimeMillis())));

		Main test = new Main();
//----------------------------------------------------------------------------------------------------//
		String dir_path = "/home/s247/Documents/cdrwork/cdr/data";
		String qm = "data20080331_1000007";
		File myFile = new File("/home/s247/Documents/cdrwork/cdr/data");
		File[] listOfFiles = myFile.listFiles();
		for (File inifile : listOfFiles) {
			if (inifile.isFile()) {
				ArrayList<String> nn = test.recurseDirectory(dir_path, inifile.getName());
				test.verrijkt(nn);
			}

		}

		System.out.println("ended: " + dateFormat.format(new Date(System.currentTimeMillis())));
	}

//-----------------------------------------------------------------------------------------------------//		

	// ArrayList<String> nn = test.recurseDirectory("/app/xl/nts/data",
	// "sms_ocs_20110518_aa_unl.x");
	// ArrayList<String> nn =
	// test.recurseDirectory("/home/s247/Documents/cdrwork/cdr/data",
	// "data20080331_1000007");

	// -- ArrayList<ArrayList<String>> filedir = new
	// ArrayList<ArrayList<String>>();--//
//		System.out.print(filedir);

	// starting multithread workernya
	// test.verrijkt(nn);

//--------------------------------closed dulu--------------------------------------------//		
//		String file_src = "/home/s247/Documents/cdrwork/cdr/data";
//		String file_dst = "/home/s247/Documents/cdrwork/cdr/dest/";
//
//		File folder = new File("/home/s247/Documents/cdrwork/cdr/data");
//		File[] listOfFiles = folder.listFiles();
//		
//		for(File file : listOfFiles) {
//			if(file.isFile()) {
//				ArrayList<String> filepath = test.recurseDirectory(file_src, file.getName());
//				test.verrijkt(filepath);
//				filedir.add(filepath);
//				Map<String, SubscriberPrefix> ref = new TreeMap<String, SubscriberPrefix>();
//				
//				String complete = filepath.get(0);
//				int start = complete.lastIndexOf("/")+1;
//				int end = complete.lastIndexOf(".");
//				end = start < end ? end : complete.length();
//				String name = complete.substring(start, end);
//				
//				CDRWorker cdrw = new CDRWorker(filepath.get(0), file_dst+name+".done", ref, max_len);
//				cdrw.process();
//				
//			}
//			
//		}
//			
//		System.out.println("ended: " + dateFormat.format(new Date(System.currentTimeMillis())));
//	}
//---------------------------------------------------------------------------------------------------//	

	// starting concurent thread
	public void verrijkt(ArrayList<String> ll) throws Exception {

		Map<String, CDRWorker> deLijst = new HashMap<String, CDRWorker>();
		String file_dst = "/home/s247/Documents/cdrwork/cdr/dest1/";
		File myFile = new File("/home/s247/Documents/cdrwork/cdr/data");
		File[] listOfFiles = myFile.listFiles();

		ArrayList<ArrayList<String>> aa = new ArrayList<ArrayList<String>>();

		// File myFile = new File("/home/s247/Documents/cdrwork/cdr/data");
		// File myFile_dst = new File("/home/s247/Documents/cdrwork/cdr/done");

		// File[] listOfFiles = folder.listFiles();

//		System.out.print(ll);
//
//		for (File file : listOfFiles) {
//			if (file.isFile()) {
//				System.out.println(file.getName());
//			}
//		}

//		for (String s : folder.list()) {
//			if (s.contains(s))
//				System.out.println(s);
//		}
//		

//		int total = 10;
//		Thread thread = new Thread() {
//			public void run() {
//				try {
//					for (int i = 0; i < ll.size(); i++) {
//						System.out.println("");
//					}
//				} catch (Exception e) {
//
//				}
//			}
//		};
//		thread.start();

		// TODO
		// create and starting instan dari thread,
		// beserta parameter yg dibutuhkan (construktor)
		// di akhir, dipastikan nggak ada thread yg gantung 9thread betul2 mati)

		// starting

//-------------------------try this code here-------------------------------------------------------------//

		for (String dd : ll) {

			CDRWorker _worker = null;

//			CDRWorker _worker = new CDRWorker(dd, dd+".done", ref, dd.length());
//			
//			_worker.start();

//			System.out.println(dd.length());

			// simpan, untuk shuting down thread
			deLijst.put(dd, _worker);
			System.out.println("started: " + dd);
		}

		for (File file : listOfFiles) {
			if (file.isFile()) {

				aa.add(ll);

				String namefile = ll.get(0);
				int start = namefile.lastIndexOf("/") + 1;
				int end = namefile.lastIndexOf(".");
				end = start < end ? end : namefile.length();
				String name = namefile.substring(start, end);
				CDRWorker cdrw = new CDRWorker(ll.get(0), file_dst + name + ".extracted", ref, max_len);
				cdrw.process();
			}
		}

		// berhentikan tread,
		for (String dd : ll) {

			CDRWorker tt = deLijst.get(dd);

			if (tt != null && tt.isAlive()) {

				try {

					System.out.println("waiting join: " + dd);
					tt.join();
					System.out.println("joined: " + dd);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	// scanning direktori

	public ArrayList<String> recurseDirectory(String dir_path, String qm) throws Exception {

		File file = new File(dir_path);
//		if (file.exists()) {
//			System.out.println("Foldernya ada");
//			return null;
//		} else {
//			System.out.println("foldernya ga ada");
//		}

		ArrayList<String> ll = new ArrayList<String>();

		for (String s : file.list()) {
//			System.out.println("File saat ini:" + s);
			if (s.contains(qm))
				ll.add(dir_path + "/" + s);
		}

		return ll;

	}

}
