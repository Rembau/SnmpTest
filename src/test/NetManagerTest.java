package test;

import java.io.IOException;
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

@SuppressWarnings("unused")
public class NetManagerTest {
	private String ip="192.168.28.2";
	PDU pdu;
	Snmp snmp;
	CommunityTarget target=null;
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
	
	private LinkedList<InfoOfPort> interfaces = new LinkedList<InfoOfPort>();
	public NetManagerTest(String ip){
		this.ip=ip;
		connect();
	}
	public void handle(){
		Hashtable<String,String> list=new Hashtable<String,String>();
		list = getListByOid(oid_1);
		for(String str:list.values()){
			InfoOfPort inf=new InfoOfPort(str.trim());
			interfaces.add(inf);
			String speed=getByOid(oid_2);
			System.out.println(speed);
		}
	}
	public void close(){
		try {
			snmp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化
	 */
	public void connect(){
		Address targetAddress = GenericAddress.parse("udp:"+ip+"/161");  
        target = new CommunityTarget();    
        target.setCommunity(new OctetString("public"));    
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
        pdu.setType(PDU.GETNEXT); 
	}
	/**
	 * 获取单个对象值
	 * @param oid
	 * @return
	 */
	public String getByOid(String oid){
		VariableBinding var = new VariableBinding(new OID(oid));
        pdu.add(var);
        pdu.setType(PDU.GETNEXT);
        ResponseEvent responseEvent=null;
		try {
			responseEvent = snmp.send(pdu, target);
		} catch (IOException e) {
			e.printStackTrace();
		}
        PDU response=responseEvent.getResponse();

        if(response != null){
            VariableBinding vb = response.get(0);
            System.out.println(vb);  
            String v=vb.toString();
            int i=v.indexOf("=");
            int j=v.length();
            //String content=v.substring(i+1,j-1);
            return v;
        }
        return null;
	}
	/**
	 * 获取对象值列表
	 * @param oid : OID
	 * @return : 关键字为OID,记录信息为值的Hashtable
	 */
	public Hashtable<String,String> getListByOid(String oid){
		PDU response=null;
		Vector<?> variableBindings=null;
		ResponseEvent response_=null;
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
	            		  System.out.println("到"+this.ip+"的连接失败，连接终止。");
	            		  try {
	            				snmp.close();
	            			} catch (IOException e1) {
	            				e.printStackTrace();
	            			}
	            		 return list;
	            	  }
	            	  System.out.println(this.ip+"连接丢失，重复连接。"+mark);
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
            System.out.print(oidstring.replace(oid+".", ""));
            System.out.println(content);
            pdu.remove(0);
		}
		list.remove(oidstring);
		return list;
	}
	public static void main(String[] args) {
		new NetManagerTest("192.168.28.2").handle();
	}
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
