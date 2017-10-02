
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class controller
{
	NodeHost[] hostNode;
	NodeRouter[] routerX;
	Timer time;
	int i;
	String linePrint;
	
	public controller(int[] hostInput,int[] routerInput,int[] lanId)
	{
		time = new Timer();
		this.hostNode=new NodeHost[hostInput.length];
		int counter = 0;
		while(counter < hostInput.length){
			this.hostNode[counter]=new NodeHost(hostInput[counter],0);
			counter++;
		}
		
		this.routerX=new NodeRouter[routerInput.length];
		counter = 0;
		while(counter < routerInput.length){
			this.routerX[counter]=new NodeRouter(routerInput[counter],0);
			counter++;
		}
		time.schedule(new TimeFunction(),0);
	}
	
	class TimeFunction extends TimerTask
	{
		
		private void rdFileTypeRouter(){
		
			int lenRx = routerX.length;
			int countRx = 0;
			while( countRx < lenRx ){
				FileReader fileReader = null;
				BufferedReader ReadFile  = null;
				try{
					int new1=0;
					fileReader = new FileReader("rout"+routerX[countRx].inputVal);
					ReadFile = new BufferedReader(fileReader);
					for(;(linePrint = ReadFile.readLine()) != null;)
					{
						++new1;
                                                 if(new1 > routerX[countRx].oldVal)
						{
							String[] token=linePrint.split(" ");
							host node = new host();
							node.putData("lan"+token[1], linePrint);
						}
					}
					routerX[countRx].oldVal = new1;
					
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
				countRx++;
			}
		}
		
		private void rdFileTypeHost(){
			
			int lenHt = hostNode.length;
			int countHt = 0;
			while( countHt < lenHt ){
				FileReader fileReader = null;
				BufferedReader ReadFile  = null;
				try{
					int new1=0;
					fileReader = new FileReader("hout"+hostNode[countHt].inputVal);
					ReadFile = new BufferedReader(fileReader);
					for(;(linePrint = ReadFile.readLine()) != null;)
					{
						++new1;
                                                  if(new1 > hostNode[countHt].oldVal)
						{	
							host node = new host();
							String[] inputToken=linePrint.split(" ");
							node.putData("lan"+inputToken[1], linePrint);
						}
					}
					hostNode[countHt].oldVal = new1;
					
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
				countHt++;
			}
		}
		
		private void checkTimeCounter(){
			i++;
			if(i >= 100)
				time.cancel();
			else
				time.schedule(new TimeFunction(),1000);
		}
		
		public void run() 
		{
			/*reading routX files */
			rdFileTypeRouter();
			
			/*reading routX files */
			rdFileTypeHost();
			
			checkTimeCounter();
		}
	}
	
	public static void main(String args[]){

		
		List<String> arrayList = Arrays.asList(args);
		int x = arrayList.indexOf("router");
		int dataArray[] = new int[x - 1];
		int j = 0;
		while( j + 2 <= x){
			int temp = -1;
			try{
				temp = Integer.parseInt(args[j + 1]);
			}catch(Exception e){
				temp = -1;
			}
			dataArray[j] = temp;
			j++;
		}
		

		int y = arrayList.indexOf("lan");
		int lengthToCount = y - x;
		int dataArr2[] = new int[lengthToCount - 1];
		int cnt1 = x + 1;
		int k = 0;
		while( cnt1  < y){
			int temp = -1;
			try{
				temp = Integer.parseInt(args[cnt1]);
			}catch(Exception e){
				temp = -1;
			}
			dataArr2[k] = temp;
		    k++;
			cnt1++;
		}
		
		
		

		int length2 = args.length - y;
		int dataArr3[] = new int[length2 - 1];
		cnt1 = y + 1;
		k = 0;
		while( cnt1  < args.length){
			int temp = -1;
			try{
				temp = Integer.parseInt(args[cnt1]);
			}catch(Exception e){
				temp = -1;
			}
			dataArr3[k] = temp;
		    k++;
			cnt1++;
		}
		

		new controller(dataArray,dataArr2,dataArr3);

	}
}

class NodeHost
{
	int inputVal,oldVal;
	public NodeHost(int inValue,int inOValue)
	{
		this.oldVal=inOValue;
		this.inputVal=inValue;
	}
}

class NodeRouter
{	
	int inputVal,oldVal;
	public NodeRouter(int inValue,int inOValue)
	{
		this.oldVal=inOValue;
		this.inputVal=inValue;
	}
}