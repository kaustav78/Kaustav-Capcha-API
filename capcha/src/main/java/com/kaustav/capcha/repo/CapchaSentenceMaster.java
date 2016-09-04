package com.kaustav.capcha.repo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.kaustav.capcha.pojo.CapchaData;

public class CapchaSentenceMaster {

	private static CapchaSentenceMaster _instance;
	private static Map<Integer, String> fileData = null;
	
	private CapchaSentenceMaster(){
		
	}
	
	public static synchronized CapchaSentenceMaster getInstance() {
		if (_instance == null) {
			_instance = new CapchaSentenceMaster();
		}
		return _instance;
	}
	
	public CapchaData getCapchaData(){
		CapchaData data = new CapchaData();
		if(fileData == null || fileData.isEmpty()){
			loadFileData();
			//System.out.println(fileData);
		}
		int numberOfSentence = fileData.size();
		//System.out.println("numberOfSentence:"+numberOfSentence);
		Random r = new Random();
		int random = r.nextInt(numberOfSentence)+1;
		//System.out.println("random:"+random);
		String fileLine = fileData.get(random);
		if(fileLine.indexOf("|") == -1){
			data.setSentence(fileLine);
		}else{
			int count = -1;
			List<String> excludes = new ArrayList<String>();
			StringTokenizer strToken = new StringTokenizer(fileLine, "|");
			
			while(strToken.hasMoreTokens()){
				String token = strToken.nextToken();
				//System.out.println("token:"+token);
				//System.out.println("count:"+count);
				if(count == -1){
					data.setSentence(token);
				}else{
					excludes.add(count, token);
				}
				count++;
			}
			if(excludes != null && !excludes.isEmpty()){
				data.setExcludes(excludes);
			}
		}
		return data;
	}
	
	private void loadFileData(){
		fileData = new TreeMap<Integer,String>();
		InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("capcha-sentences.txt");
		try{
			Scanner scanner = new Scanner(in);
			int count = 1;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				//System.out.println("line:"+line);
				if(line != null && line.trim().length() > 0){
					fileData.put(count, line);
					count++;
				}
			}
			scanner.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
