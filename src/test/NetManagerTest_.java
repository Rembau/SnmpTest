package test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class NetManagerTest_ {
	private LinkedList<InfoOfPort> interfaces = new LinkedList<InfoOfPort>();
	//1.3.6.1.2.1.17.4.3.1.2.0.15.226.24.234.206 49
	private String oid_1="1.3.6.1.2.1.2.2.1.1";  //索引
	private String oid_2="1.3.6.1.2.1.2.2.1.5";  //speed
	private String oid_3="1.3.6.1.2.1.2.2.1.6";  //physaddr
	private String oid_4="1.3.6.1.2.1.2.2.1.8";  //state
	private String oid_5="1.3.6.1.2.1.2.2.1.2";  //name
	
	private String oid_6="1.3.6.1.2.1.17.1.4.1.2";    //索引1
	//1.3.6.1.2.1.17.1.4.1.2.19 4227770
	private String oid_7="1.3.6.1.2.1.17.4.3.1.2";    //索引2
	//1.3.6.1.2.1.17.4.3.1.2.0.15.226.24.234.206 49
	private String oid_8="1.3.6.1.2.1.17.4.3.1.1";    //转发表
	//1.3.6.1.2.1.17.4.3.1.1.0.35.137.162.15.180 00:23:89:a2:0f:b4
	private String ip="192.168.28.2";
	private String groupStr="public";
	protected PDU response=null;
	protected Vector<?> variableBindings=null;
	protected ResponseEvent response_=null;
	protected CommunityTarget target=null;
	protected PDU pdu;
	protected Snmp snmp;
	public NetManagerTest_(String ip,String group){
		this.ip=ip;
		this.groupStr=group;
	}
	/**
	 * 获取转发表信息
	 * @return 返回转发表信息的二维数组
	 * @throws Exception 出现的任何异常
	 */
	public String[][] handle_2() throws Exception{
		Hashtable<String,String> list=new Hashtable<String,String>();
		list = search(oid_1);
		for(String str:list.values()){         //获取接口的索引，并建立接口实体、赋值
			interfaces.add(new InfoOfPort(str.trim()));
		}
		Hashtable<String,String> list_1=new Hashtable<String,String>();
		list_1 = search(oid_6);					//获取转发记录对象的索引1
		Hashtable<String,String> list_=new Hashtable<String,String>();
		list_=search(oid_7);					//获取转发记录对象的索引1
		
		list=search(oid_8);
		Enumeration<String> keys = null;
		keys=list.keys();
		int length=0;
		while(keys.hasMoreElements()){			//通过索引2获取索引1在获取对应的索引，与相应接口建立连接
			String str = keys.nextElement();
			InfoOfPort f=null;
			try{
				System.out.println(str);
				System.out.println(list_.get(str));
				System.out.println(list_1.get(list_.get(str)));
				f=getInter(list_1.get(list_.get(str)));
			}catch(Exception e){
				continue;
			}
			if(f!=null){
				f.addrs.add(list.get(str));			
				length++;
			}	
			else {
				System.out.println("xxxxxxxxxxxxxxx");
			}
		}	
		list = search(oid_5);			//把某一接口对应的转发表列表加到该接口实体上。
		keys = list.keys();
		while(keys.hasMoreElements()){
			String str = keys.nextElement();
			InfoOfPort f=getInter(str);
			if(f!=null){
				f.setName(list.get(str));
			} else{
				System.out.println(str+"====================");
			}
		}
		String result[][]=new String[length][4];
		int i=0;
		for(InfoOfPort f:interfaces){  			//获取转发表信息对应的二维数组
			System.out.println("==\nname:"+f.name);
			System.out.println(f.addrs);
			for(String pa:f.addrs){
				result[i][0]=i+1+"";
				result[i][1]=f.name;
				result[i][2]=f.index;
				result[i][3]=pa;
				i++;
			}
		}
		return result;
	}	
	/**
	 * 根据索引获取对应的实体
	 * @param index 索引
	 * @return 接口实体
	 */
	public InfoOfPort getInter(String index){
		for(InfoOfPort i:interfaces){
			if(i.getIndex().equals(index)){
				return i;
			}
		}
		return null;
	}
	/**
	 * 获取接口信息对应的二维数组
	 * @return 
	 * @throws Exception
	 */
	public String[][] handle_1() throws Exception{
		Hashtable<String,String> list=new Hashtable<String,String>();
		list = search(oid_1);
		for(String str:list.values()){ 			//获取接口的索引并建立接口实体
			interfaces.add(new InfoOfPort(str.trim()));
		}
		Enumeration<String> keys = null;
		list = search(oid_2);
		keys=list.keys();
		while(keys.hasMoreElements()){			//获取接口的速度，并添加到相应的接口信息中
			String str = keys.nextElement();
			InfoOfPort f=getInter(str);
			if(f!=null){
				f.setSpeed(list.get(str));
			}	
			else {
				System.out.println("xxxxxxxxxxxxxxx");
			}
		}
		
		list = search(oid_3);					//获取接口的物理地址，并添加到相应的接口信息中
		keys = list.keys();
		while(keys.hasMoreElements()){
			String str = keys.nextElement();
			InfoOfPort f=getInter(str);
			if(f!=null){
				f.setAddr(list.get(str));
			}			
		}
		
		list = search(oid_4);					//获取接口的状态，并添加到相应的接口信息中
		keys = list.keys();
		while(keys.hasMoreElements()){
			String str = keys.nextElement();
			InfoOfPort f=getInter(str);
			if(f!=null){
				f.setState(list.get(str));
			}		
		}
		
		list = search(oid_5);					//获取接口的名称，并添加到相应的接口信息中
		keys = list.keys();
		while(keys.hasMoreElements()){
			String str = keys.nextElement();
			InfoOfPort f=getInter(str);
			if(f!=null){
				f.setName(list.get(str));
			} else{
				System.out.println(str+"====================");
			}
		}
		String result[][]=new String[interfaces.size()][5];
		int i=0;
		for(InfoOfPort f:interfaces){			//根据已有接口信息，构造二维数组
			System.out.println("==\nname:"+f.name+"\nspeed:"+f.transSpeed+"\nstate:"+
					f.linkState+"\nphysAddr"+f.physAddr);
			result[i][0]=i+1+"";
			result[i][1]=f.name;
			if(f.linkState.equals("1")){
				result[i][2]="UP";
			} else if(f.linkState.equals("2")){
				result[i][2]="DOMN";
			}
			if(f.transSpeed.equals("10000000")){
				result[i][3]=f.transSpeed+"(100M)";
			} else if(f.transSpeed.equals("1000000000")){
				result[i][3]=f.transSpeed+"(1G)";
			}
			result[i][4]=f.physAddr;
			i++;
		}
		return result;
	}
	/**
	 * 初始化
	 * @param ip : 要初始化的路由IP
	 */
	public void init(String ip){
		//System.out.println(ip+"初始化开始");
		Address targetAddress = GenericAddress.parse("udp:"+ip+"/161");   
        target = new CommunityTarget();    
        target.setCommunity(new OctetString(this.groupStr));    
        target.setAddress(targetAddress);    
        target.setTimeout(3000);    
        target.setVersion(SnmpConstants.version1);
        pdu = new PDU();
        TransportMapping transport=null;
		try {
			transport = new DefaultUdpTransportMapping();
            transport.listen();    
		} catch (IOException e) {
			e.printStackTrace();
		}
        snmp = new Snmp(transport);     
        pdu.setType(PDU.GETNEXT);     //获取snmp信息的方法类型
	}
	/**
	 * 获取记录
	 * @param oid : OID
	 * @return : 关键字为OID,记录信息为值的Hashtable
	 * @throws Exception 
	 */
	public Hashtable<String,String> search(String oid) throws Exception{
		init(ip);
		Hashtable<String,String> list=new Hashtable<String,String>();
		String content=null;
		String oidstring=oid;
		int mark=0,x=0;
		while(true) {
			x++;
            pdu.add(new VariableBinding(new OID(oidstring)));
            while(true){
            	try{ 				  
            		response_ = snmp.send(pdu, target);
					response =response_.getResponse();
	            	variableBindings=response.getVariableBindings();
	            	mark=0;
	                break;
	              }catch(Exception e){
	            	  mark++;
	            	  if(mark>3){
	            		  System.out.println(this.ip+"==========error=========");
	            		  try {
	            				snmp.close();
	            			} catch (IOException e1) {
	            				e.printStackTrace();
	            			}
	            			throw new Exception("");
	            	  }
	            	  System.out.println(this.ip+"==========重复========="+mark);
	              }
			}
			String variable=variableBindings.toString();
            int i=variable.indexOf("=");
            int j=variable.length();
            oidstring=variable.substring(1,i-1);
            content=variable.substring(i+1,j-1);
            if(oidstring.startsWith(oid)){
            	list.put(oidstring.replace(oid+".", "").trim(), content.trim());
            }
            else {
            	break;
            }
			System.out.print(x+" ");
			//System.out.print(oidstring.replace(oid+".", ""));
            //System.out.println(content);
            pdu.remove(0);
		}
		list.remove(oidstring);
		//System.out.println(list);
		try {
			snmp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void main(String[] args) {
		try {
			new NetManagerTest_("192.168.28.2","public").handle_2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 接口实体
	 * @author Rembau
	 *
	 */
	class InfoOfPort{
		String index;
		String name;
		String transSpeed;
		String linkState;
		String physAddr;
		LinkedList<String> addrs = new LinkedList<String>();
		String index_1;
		public InfoOfPort(String index){
			this.index=index;
		}
		public String getIndex(){
			return this.index;
		}
		public void setName(String name){
			this.name = name;
		}
		public void setSpeed(String speed){
			this.transSpeed=speed;
		}
		public void setState(String state){
			this.linkState = state;
		}
		public void setAddr(String addr){
			this.physAddr = addr;
		}
		public void setIndex_1(String index_1){
			this.index_1=index_1;
		}
	}
}
