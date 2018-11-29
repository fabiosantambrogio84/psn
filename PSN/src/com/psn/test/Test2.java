package com.psn.test;

import com.psn.engine.Decoder;
import com.psn.engine.Encoder;

public class Test2 {
	
	public static void main(String[] args) throws Exception{
//		String command = "wmic path win32_pnpentity where \"deviceid like '%USB%'and name like '%archiviazione%'\" get PNPDeviceID";
//		
//		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
//
//		ProcessBuilder builder = new ProcessBuilder();
//		if (isWindows) {
//		    builder.command("cmd.exe", "/c", command);
//		} else {
////		    builder.command("sh", "-c", "ls");
//			throw new RuntimeException("Sistema operativo non supportato");
//		}
//		builder.directory(new File(System.getProperty("user.home")));
//		builder.inheritIO();
//		StringBuilder stringBuilder = new StringBuilder();
//		Process process = builder.start();
//		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println, stringBuilder);
//		Executors.newSingleThreadExecutor().submit(streamGobbler);
//		int exitCode = process.waitFor();
//		assert exitCode == 0;
//		
//		System.out.println(stringBuilder.toString());
//		
//		String result = StringUtils.upperCase(stringBuilder.toString());
//		List<String> vids = new ArrayList<String>();
//		List<String> pids = new ArrayList<String>();
//		
//		String vidRegex = "(VID_[a-zA-Z0-9]{4})";
//		String pidRegex = "(PID_[a-zA-Z0-9]{4})";
//		// (VID_[a-zA-Z0-9]{4})
//		// (PID_[a-zA-Z0-9]{4})
//		
//		Pattern vidPattern = Pattern.compile(vidRegex);
//        Matcher vidMatcher = vidPattern.matcher(result);
//        while (vidMatcher.find()) {
//            String match = vidMatcher.group(0);
//            vids.add(match);
//        }
//        
//        Pattern pidPattern = Pattern.compile(pidRegex);
//        Matcher pidMatcher = pidPattern.matcher(result);
//        while (pidMatcher.find()) {
//            String match = pidMatcher.group(0);
//            pids.add(match);
//        }
//        
//        for(int i=0; i<vids.size(); i++){
//        	System.out.println("Vid -> "+vids.get(i));
//        }
//        for(int i=0; i<pids.size(); i++){
//        	System.out.println("Pid -> "+pids.get(i));
//        }
//		
//        process.destroyForcibly();
//        
//        System.exit(0);
		String filePath = "C:\\Users\\Fabio Santambrogio\\Desktop\\DA_FARE_PUZZLE\\temp\\foto1.jpg";
		//Encoder.encode(filePath);
		Decoder.decode(filePath);
        
	}
	
}
