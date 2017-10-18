package com.company.ex;

import com.company.ex.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.sun.management.OperatingSystemMXBean;



public class MyLogClient {

	public static void main(String[] args) {

		double cpu,mem,hdd;
		String macAddr = NetworkInfo.getShortMacAddress();
		
		saveComputer(macAddr);//컴퓨터 등록
		
	    while(true){
	      
	      cpu = showCpu();
	      mem = showMem();
	      hdd = showDisk();
	      
	      saveResource(macAddr, cpu, mem, hdd);//자원현황 등록
	      
	      try {
	        Thread.sleep(2000);
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      }
	    }
	}
	
	public static void saveComputer(String comId) {
		
		try {
			String url = String.format("http://130.211.254.77:3000/computer/id/%s/name/test",comId);
			URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("POST");
            
            System.out.println(String.format(">> URL : %s",url));
            
            // 응답 내용(BODY) 구하기        
            try (InputStream in = con.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                
                byte[] buf = new byte[1024 * 8];
                int length = 0;
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                System.out.println(new String(out.toByteArray(), "UTF-8"));            
            }
            
            con.disconnect();
            
		} catch (MalformedURLException e) {  
		    System.out.println("The URL address is incorrect.");  
		    e.printStackTrace();  
		} catch (IOException e) {  
		    System.out.println("It can't connect to the web page.");  
		    e.printStackTrace();  
		} 
	}

	public static void saveResource(String comId, double cpu, double mem, double hdd) {
		
		try {
			String url = String.format("http://130.211.254.77:3000/resource/com-id/%s/cpu/%.0f/mem/%.0f/hdd/%.0f",comId,cpu,mem,hdd);
			URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("POST");
            
            System.out.println(String.format(">> URL : %s",url));
            System.out.println(String.format("ID: %s CPU: %.0f MEM: %.0f HDD: %.0f",comId, cpu, mem, hdd));
            
            // 응답 내용(BODY) 구하기        
            try (InputStream in = con.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                
                byte[] buf = new byte[1024 * 8];
                int length = 0;
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                System.out.println(new String(out.toByteArray(), "UTF-8"));            
            }
            
            con.disconnect();
            
		} catch (MalformedURLException e) {  
		    System.out.println("The URL address is incorrect.");  
		    e.printStackTrace();  
		} catch (IOException e) {  
		    System.out.println("It can't connect to the web page.");  
		    e.printStackTrace();  
		} 
	}
	
	/*
	 * CPU 점유율
	*/
	public static double showCpu() {
		final OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
		double load = osBean.getSystemCpuLoad();
		load = Math.round(load*100);
		
		if(load < 0) load = 0;
		
		//System.out.print("CPU : " + load + " %  ");
		return load;
	}
	
	/*
	 * MEM 점유율
	*/
	public static double showMem() {
		final OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
		double mem = osBean.getFreePhysicalMemorySize() / Math.pow(1024, 2);
		mem = Math.round(mem);
		
		//System.out.print("MEM : " + mem + " MB  ");
		return mem;
	}
	
	/*
	 * 디스크용량
	*/
	public static double showDisk() {
		File root = null;
		try {
			
			root = File.listRoots()[0];//첫번째 드라이브명 가져오기
			double size = root.getUsableSpace() / Math.pow(1024, 3);
			size = Math.round(size*100)/100;

			//System.out.print("HDD : " + size + " GB  ");
			return size;
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
