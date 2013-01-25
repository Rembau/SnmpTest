package getPCInformation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
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

import conn.DBoperate;

public class ScanRouter {
	/**
	 * IP组
	 */
	public static String oid_1="1.3.6.1.2.1.4.1"; 
	/**
	 * 网关索列表
	 */
	public static String oid_2="1.3.6.1.2.1.4.20.1.2";
	/**
	 * 子网掩码
	 */
	public static String oid_3="1.3.6.1.2.1.4.20.1.3"; 
	/**
	 * MAC地址
	 */
	public static String oid_5="1.3.6.1.2.1.4.22.1.2";
	/**
	 * 映射类型
	 */	
	public static String oid_6="1.3.6.1.2.1.4.22.1.4";
	private String routerIP="";
	private PDU response=null;
	private Vector<?> variableBindings=null;
	private ResponseEvent response_=null;
	private CommunityTarget target=null;
	private PDU pdu;
	private Snmp snmp;
	
	private DBoperate db;
	public ScanRouter(){
		db = new DBoperate();
	}
	public ScanRouter(String routerIP){
		this.routerIP=routerIP;

	}
	/**
	 * 获得ip地址Mac地址对照表
	 */
	public void getAllIp_Mac(){
		ResultSet rs=db.select("Select * from machine_router");
		try {
			if(!rs.next()){
				System.out.println("为空了");
				return;
			}
			else {
				rs.beforeFirst();
			}
			while(rs.next()){
				init(rs.getString("router_ip"));
				this.routerIP=rs.getString("router_ip");
				getIp_Mac(rs.getString("router_index"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getIp_Mac(String index){
		Hashtable<String,String> pc=search(ScanRouter.oid_5+"."+index);
		if(pc.size()==0){
			System.out.println("没有元素！");
			return;
		}
		Enumeration<String> keys=pc.keys();
		String key,value;
		while(keys.hasMoreElements()){
			key=keys.nextElement();
			value=pc.get(key);
			key=key.substring((ScanRouter.oid_5+"."+index).length()+1);
			System.out.println(key+" "+value);
			db.insert("insert into machine_computer(computer_ip) value('"+key+"')");
			db.update("update machine_computer set computer_mac='"+value.trim()+"' where computer_ip='"+key+"'");
		}
	}
	
	/**
	 * 获取与首路由有关的所有路由
	 */
	public void getAllRouter(){
		ResultSet rs;
		init(this.routerIP);
		getRouter();
		
		while(true){
			rs=db.select("Select * from machine_router where router_index=0");// where router_index='null'
			try {
				if(!rs.next()){
					System.out.println("为空了");
					return;
				}
				else {
					rs.beforeFirst();
				}
				while(rs.next()){
					init(rs.getString("router_ip"));
					this.routerIP=rs.getString("router_ip");
					getRouter();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void getRouter(){
		Hashtable<String,String> routers=search(ScanRouter.oid_2);
		if(routers.size()==0){
			System.out.println("没有元素！");
			return;
		}
		Enumeration<String> keys=routers.keys();
		String key,value;
		boolean flag=false;
		while(keys.hasMoreElements()){
			key=keys.nextElement();
			//System.out.println(key);
			value=routers.get(key);
			key=key.substring(21);
			//System.out.println(key+" "+value);
			if(key.startsWith("127")){
				continue;
			}
			if(key.startsWith(this.routerIP)){
				db.insert("insert into machine_router(router_ip) values('"+key+"')");
				db.update("update machine_router set router_index = '"+value+"' where router_ip='"+key+"'");
				flag=true;
			}
			else {
				db.insert("insert into machine_router(router_ip,router_index) values('"+key+"',0)");
			}
		}
		if(!flag){
			System.out.println(this.routerIP+"获取本段ip索引错误！");
		}
		else {
			System.out.println(this.routerIP+"获取本段ip索引ok！");
		}
	}
	public void init(String ip){
		System.out.println(ip+"初始化开始");
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
	public Hashtable<String,String> search(String oid){
		Hashtable<String,String> list=new Hashtable<String,String>();
		String content=null;
		String oidstring=oid;
		int mark=0;
		int x=0;
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
	            	  if(mark>20){
	            		  System.out.println("==========error=========");
	            		  return null;
	            	  }
	            	  System.out.println("==========重复========="+mark);
	              }
			}
			String variable=variableBindings.toString();
            int i=variable.indexOf("=");
            int j=variable.length();
            oidstring=variable.substring(1,i-1);
            content=variable.substring(i+1,j-1);
            if(oidstring.startsWith(oid)){
            	list.put(oidstring, content);
            }
            else {
            	break;
            }
			System.out.print(x+" ");
/*            System.out.print(oidstring);
            System.out.println(content);*/
            pdu.remove(0);
		}
		list.remove(oidstring);
		//System.out.println(list);
		return list;
	}
	public static void main(String[] args) {
		new ScanRouter().getAllIp_Mac();
		//new ScanRouter("192.168.21.1").getAllRouter();
	}
}
