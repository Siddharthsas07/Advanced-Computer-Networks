import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class router
{
	int rid,i;
	TableStructure[] tableStorage;
	LanStructure[] lansList;
	Timer time;
	ArrayList<Sources> sourcesList;
	String line;
	public router(int rid,int[] lids)
	{
		sourcesList = new ArrayList<Sources>();
		i=0;
		this.rid = rid;
		lansList = new LanStructure[lids.length];
		tableStorage = new TableStructure[10];
		int count = 0;
		while ( count < 10 ) {
			tableStorage[count] = new TableStructure(10, 10, 10, new ArrayList<SubClassChilds>(), false, 0);
			tableStorage[count].re = false;
			tableStorage[count].re_time = 0;
			count++;
		}
		
		count = 0;
		while( count < lids.length) {
			tableStorage[lids[count]].hops = 0;
			tableStorage[lids[count]].next_lan = lids[count];
			lansList[count] = new LanStructure(lids[count], 0, true);
			count++;
		}

		time = new Timer();
		time.schedule(new TimeFunction(), 0);
	}
	
	private static int[] definingArrayofparameters(String data[]){
		
		int lengthData = data.length;
		int ArrayOfDt[] = new int[lengthData - 1];
		int numberCount = 0;
		while( numberCount + 2 <= lengthData){
			int x = -1;
			try{
				x = Integer.parseInt(data[numberCount + 1]);
			}catch(Exception e){
				x = -1;
			}
			ArrayOfDt[numberCount] = x;
			numberCount++;
		}
		
		return ArrayOfDt;
	}
	
	public static void main(String args[]){
		
		int[] a = definingArrayofparameters(args);
		int intialization = -1;
		try{
			intialization = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			intialization = -1;
		}
		new router(intialization,a);
	}
	
	void putMsgtoFiles(String putToFiles, String myMsg){
		
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		try {
			fileWriter = new FileWriter(putToFiles, true);
			writer = new BufferedWriter(fileWriter);
			writer.write(myMsg+"\n");
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
	
	class TimeFunction extends TimerTask
	{
		public void run()
		{

				String temp=null;
				if(i%5==0)   
				{
					temp=String.valueOf(rid);
					int syncCounter = 0;
					int tableLength = tableStorage.length;
					while(syncCounter < tableLength)
					{
						if(tableStorage[syncCounter].next_router==10)
							temp=temp.concat(" "+tableStorage[syncCounter].hops+" -");
						else
							temp=temp.concat(" "+tableStorage[syncCounter].hops+" "+tableStorage[syncCounter].next_router);
						syncCounter++;
					}
					String dv=null;
					syncCounter = 0;
					int lanLength = lansList.length;
					while(syncCounter < lanLength)
					{
						try
						{
							dv="DV "+lansList[syncCounter].parameter+" "+temp;
							putMsgtoFiles("rout"+rid, dv);
						}
						catch(Exception e)
						{
							System.out.println("execption occurred for DV");
						}
						syncCounter++;
					}
				}
				
				int dataSyncCounter = 0;
				int lanLength = lansList.length;
				while(dataSyncCounter<lanLength)
				{
					int val = lansList[dataSyncCounter].parameter;
					if(tableStorage[val].re==true)
						tableStorage[val].re_time++;
					if(tableStorage[val].re_time==20)
					{
						tableStorage[val].re=false;
						tableStorage[val].re_time=0;
					}
					dataSyncCounter++;
				}
				
				int zCnt = 0;
				int sourceLength = sourcesList.size(); 
				while(zCnt < sourceLength)
				{
					sourcesList.get(zCnt).countOfNMRMessage=true;
					List<SubClassChilds>  tableListMap = tableStorage[sourcesList.get(zCnt).value].childs;
					int countInnerLoop = 0;
					while(countInnerLoop <  tableListMap.size())
					{
						if( tableListMap.get(countInnerLoop).status==false)
						{
							sourcesList.get(zCnt).countOfNMRMessage=false;
						}
						if( tableListMap.get(countInnerLoop).status==true)
						{
							 tableListMap.get(countInnerLoop).timeToNMRMsg++;
							if( tableListMap.get(countInnerLoop).timeToNMRMsg==20)
							{
								 tableListMap.get(countInnerLoop).timeToNMRMsg=0;
								 tableListMap.get(countInnerLoop).status=false;//this will turn nmr off if no nmr received with 20 sec for that child of specific child
							}	
						}	
						else{ tableListMap.get(countInnerLoop).timeToNMRMsg=0;}
						countInnerLoop++;
					}	
					if(sourcesList.get(zCnt).countOfNMRMessage==true)
					{
						sourcesList.get(zCnt).countOfNMRMessageTime++;
						if(sourcesList.get(zCnt).countOfNMRMessageTime==10)
						{
							try
							{
								String msgStr = "NMR "+tableStorage[sourcesList.get(zCnt).value].next_lan+" "+rid+" "+sourcesList.get(zCnt).value;
								putMsgtoFiles("rout"+rid, msgStr);
								sourcesList.get(zCnt).countOfNMRMessageTime=0;
							}
							catch(Exception e){
								System.out.println("execption occurred for SOURCES NMR");
							}
						}
					}
					else{sourcesList.get(zCnt).countOfNMRMessageTime=0;}
					zCnt++;
				}

				int lanVal,hostValueOflan,routerVlaueOFId;
				for(int i=0;i<lansList.length;i++)
				{
					FileReader fileReader = null;
					BufferedReader ReadFile  = null;
					try{
						fileReader = new FileReader("lan"+lansList[i].parameter);
						ReadFile = new BufferedReader(fileReader);
						int new1=0;
						while((line= ReadFile.readLine()) != null)
						{
							++new1;
							if(new1 > lansList[i].valOld)
							{
								String[] splitedData=line.split(" ");
					switch(splitedData[0])
					{
					
					
						
					case "NMR":
						
						try{
							hostValueOflan=Integer.parseInt(splitedData[3]);
							lanVal=Integer.parseInt(splitedData[1]);
						}catch (NumberFormatException e) {
							lanVal = 0;
							hostValueOflan = 0;
						}
						
						List<SubClassChilds> tableList = tableStorage[hostValueOflan].childs;
						int nmrCnt = 0;
						while(nmrCnt<tableList.size())
						{
							if(tableList.get(nmrCnt).parameter==lanVal)
							{
								tableList.get(nmrCnt).status=true;
								tableList.get(nmrCnt).timeToNMRMsg=0;
								break;
							}	
							nmrCnt++;
						}
						break;
						
						
					case "receiver":
						try{
							lanVal=Integer.parseInt(splitedData[1]);
						}catch (Exception e) {
							lanVal = 0; 
						}
						tableStorage[lanVal].re=true;
						tableStorage[lanVal].re_time=0;
						break;
						
						
					case "DV": 
						
						try{
							lanVal=Integer.parseInt(splitedData[1]);
							routerVlaueOFId=Integer.parseInt(splitedData[2]);
						}catch (Exception e) {
							lanVal = 0;
							routerVlaueOFId = 0;
						}
						if(routerVlaueOFId!=rid)
						{

							for(int k=0;k<lansList.length;k++)
							{
								if(lansList[k].parameter==lanVal)
								{
									lansList[k].leaf=false;
									break;
								}
							}
							
							for(int x=3,y=3;x<=21;x+=2,y++)
							{

								int mapping=Integer.parseInt(splitedData[x]);
								int path=100;
								if(!splitedData[x+1].equals("-"))
									path=Integer.parseInt(splitedData[x+1]);
								

								if((mapping+1)<tableStorage[x-y].hops || ((mapping+1)==tableStorage[x-y].hops && tableStorage[x-y].next_router>routerVlaueOFId))
								{
									tableStorage[x-y].hops=(mapping+1);
									tableStorage[x-y].next_lan=Integer.parseInt(splitedData[1]);
									tableStorage[x-y].next_router=Integer.parseInt(splitedData[2]);        							
								}
								
								
								
								List<SubClassChilds> ChildOfTables = tableStorage[x-y].childs;
								if(path==rid && tableStorage[x-y].next_lan!=lanVal )
								{
									boolean parentFlag=false;
									for(int j=0;j<ChildOfTables.size();j++)
									{
										if(ChildOfTables.get(j).parameter==lanVal)
										{
											parentFlag=true;
											break;
										}	
									}
									if(ChildOfTables.isEmpty() ||parentFlag==false)
										ChildOfTables.add(new SubClassChilds(lanVal,false,0));                							
								}
								
								
								if(lanVal!=tableStorage[x-y].next_lan && tableStorage[lanVal].re==true)
								{
									if(mapping>tableStorage[x-y].hops ||(mapping==tableStorage[x-y].hops && routerVlaueOFId>rid))
									{
										boolean isChildPresent1=false;
										for(int j=0;j<ChildOfTables.size();j++)
										{
											if(ChildOfTables.get(j).parameter==lanVal)
											{
												isChildPresent1=true;
												break;
											}	
										}
										if(ChildOfTables.isEmpty() ||isChildPresent1==false)
											ChildOfTables.add(new SubClassChilds(lanVal,false,0));
									}
								}
							}
						}
						break;
						
					case "data":
						
						try{
							hostValueOflan=Integer.parseInt(splitedData[2]);
							lanVal=Integer.parseInt(splitedData[1]);
						}catch (NumberFormatException e) {
							lanVal = 0;
							hostValueOflan = 0;
						}
						if(lanVal==tableStorage[hostValueOflan].next_lan)
						{

							boolean contains=false;
							int hostSize = sourcesList.size();
							int innerCnts = 0;
							while(innerCnts < hostSize)
							{
								if(sourcesList.get(innerCnts).value==hostValueOflan)
								{
									contains=true;
									break;
								}	
								innerCnts++;
							}
							
							if(hostSize == 0 || contains==false)
								sourcesList.add(new Sources(hostValueOflan,true,0));
							
							List<SubClassChilds> childOfTables = tableStorage[hostValueOflan].childs;
							for(int j=0;j<childOfTables.size();j++)
							{
								if(childOfTables.get(j).status==false || (childOfTables.get(j).status==true 
										&& tableStorage[childOfTables.get(j).parameter].re==true))
								{
									try
									{
										String dataStart = splitedData[0]+" "+childOfTables.get(j).parameter+" "+splitedData[2];
										putMsgtoFiles("rout"+rid, dataStart);
									}
									catch(Exception e){
										System.out.println("execption occurred for DATA FORWARDING");
									}
								}
							}
						}
						break;
						
								}
								
							}
							
						}
						lansList[i].valOld = new1;
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
				}			
				
				
				
				for(int i=0;i<lansList.length;i++)
				{
					if(lansList[i].leaf==true)
					{
						int findLeaf=lansList[i].parameter;
						if(tableStorage[findLeaf].re==true)
						{
							for(int j=0;j<sourcesList.size();j++)
							{
								boolean childExistence=false;
								List<SubClassChilds> datas = tableStorage[sourcesList.get(j).value].childs;
     							for(int k=0;k<datas.size();k++)
								{
									if(datas.get(k).parameter==findLeaf)
									{
										childExistence=true;
										break;
									}	
								}
     							if(datas.isEmpty() ||childExistence==false)
     								datas.add(new SubClassChilds(findLeaf,false,0));
     						}
						}	
					}
				}
				
				
				
			i++;
			if(i<100)
				time.schedule(new TimeFunction(),1000);
			else 
				time.cancel();
		}
		
	}
}

class Sources
{
	boolean countOfNMRMessage;
	int countOfNMRMessageTime,value;
	public Sources(int value,boolean nmr_total,int nmr_total_time)
	{
		this.countOfNMRMessage=nmr_total;
		this.value=value;
		this.countOfNMRMessageTime=nmr_total_time;
	}
}

class LanStructure
{
	int parameter,valOld;
	boolean leaf;
	public LanStructure(int parameter,int valOld,boolean leaf)
	{
		this.parameter=parameter;
		this.valOld=valOld;
		this.leaf=leaf;
	}
}

class SubClassChilds
{
	int parameter,timeToNMRMsg;
	boolean status;
	public SubClassChilds(int parameter,boolean status,int timeToNMRMsg) 
	{
		this.parameter=parameter;
		this.status=status;
		this.timeToNMRMsg=timeToNMRMsg;
	}
}


class TableStructure
{
	int hops,next_lan,next_router,re_time;
	ArrayList<SubClassChilds> childs;
	boolean re;
	public TableStructure(int hops,int next_lan,int next_router,ArrayList<SubClassChilds> childs,boolean re,int re_time)
	{
		this.hops=hops;
		this.next_lan=next_lan;
		this.next_router=next_router;
		this.childs=childs;
		this.re=re;
		this.re_time=re_time;
	}
	
}


