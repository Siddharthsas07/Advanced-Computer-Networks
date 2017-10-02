import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class host {

	void putData(String putFile, String dataWrite){
		
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		try {
			fileWriter = new FileWriter(putFile, true);
			writer = new BufferedWriter(fileWriter);
			writer.write(dataWrite+"\n");
		} catch (IOException e) {
			
		}finally {
			try{
				if(writer != null)
					writer.close();
			}catch (Exception e2) {
			}
			try{
				if(fileWriter != null)
					fileWriter.close();
			}catch (Exception e2) {
			}
		}
	}
	
	public static void main(String args[]) {
		/*
		 * String content = "2 2 receiver"; String args1[] = content.split(" ");
		 */
		int config1 = -1;
		int config2 = -1;
		
		try{
			config1 = Integer.parseInt(args[0]);
			config2 = Integer.parseInt(args[1]);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		
		if (args[2].equals("sender")){
			
			int config3 = -1;
			int config4 = -1;
			
			try{
				config3 = Integer.parseInt(args[3]);
				config4 = Integer.parseInt(args[4]);
			}catch(Exception exp){
				exp.printStackTrace();
			}
			
			new Sender("hout" + config1, "data " + config2 + " " + config2, config3, config4);
			
		}else if(args[2].equals("receiver")){
			
			new NodeReceiver("hout" + config1, "hin" + config1, "lan" + config2, "receiver " + config2);

		}
	}
}

/* sender functions */
class Sender {
	int timeToStart, gapTm;
	int i;
	Timer timerNode;
	String fileToSend, msgData;
	

	public Sender(String outPutFile, String msgData, int timeToStart, int gapTm) {
		this.fileToSend = outPutFile;
		this.msgData = msgData;
		this.timeToStart = timeToStart;
		this.gapTm = gapTm;
		i = timeToStart;
		timerNode = new Timer();
		timerNode.schedule(new Operate(), timeToStart * 1000);
	}

	class Operate extends TimerTask {
		
		private void checkTimer(){
			
			i += gapTm;
			if (i > 100)
				timerNode.cancel();
			else
				timerNode.schedule(new Operate(), gapTm * 1000);
		}
		
		public void run() {
			host host = new host();
			host.putData(fileToSend,msgData);
			checkTimer();
			
		}
	}
}
/* sender functions */

/* receiver functions */
class NodeReceiver {
	Timer timerNode;
	String fileToSend, fileToRecieve, readDataFrom, doAdvrt, line;
	int i, oldSave;

	public NodeReceiver(String outfile, String infile, String readfrom, String advertise) {
		this.fileToSend = outfile;
		this.fileToRecieve = infile;
		this.readDataFrom = readfrom;
		this.doAdvrt = advertise;
		timerNode = new Timer();
		i = 0;
		oldSave = 0;
		timerNode.schedule(new TimeOpr(), 0);
	}

	class TimeOpr extends TimerTask {
		
		private void checkTimer(){
			++i;
			if (i > 100)
				timerNode.cancel();
			else
				timerNode.schedule(new TimeOpr(), 1000);
		}
		

		public void run() {
			host node = new host();
			try {
				if (i % 10 == 0) {
					node.putData(fileToSend,doAdvrt);
				}
				FileReader fileReader = null;
				BufferedReader ReadFile  = null;
				try{
					int new1 = 0;
					fileReader = new FileReader(readDataFrom);
					ReadFile = new BufferedReader(fileReader);
					for (new1 = 0;(line = ReadFile.readLine()) != null; ) {
						String[] token = line.split(" ");
						++new1;
						if (new1 > oldSave && token[0].equals("data")) {
							node.putData(fileToRecieve, line);
						}
					}
					oldSave = new1;
				}catch(IOException ioe){
					
				}finally {
					try{
						if(ReadFile != null){
							ReadFile.close();
						}
					}catch(IOException ioe){
						
					}
					try{
						if(fileReader != null){
							fileReader.close();
						}
					}catch(IOException ioe){
						
					}
				}
				
			}finally {
				node = null;
			}
			
			checkTimer();
		}
	}
}
/* receiver functions */
