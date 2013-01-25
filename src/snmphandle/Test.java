package snmphandle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Test {
	//private static final String IP="198.9.1.190";
	private static final String IP="192.168.21.1";
	private static final String COMMUNITY="public";
	
	public static void main(String[] args) {
		try {
//			SnmpManager mgr = new SnmpManager();
			//oid,name
			Map<String,String> map = new HashMap<String,String>();
			map.put("1.3.6.1.2.1.1.1", "系统描述");
			//map.put("1.3.6.1.2.1.25.1.7", "本机物理总内存");//???
			map.put("1.3.6.1.2.1.25.2.2", "本机物理总内存");//windows下正确			
			//map.put("1.3.6.1.2.1.25.4.2.1.2", "列出系统进程");
			//map.put("1.3.6.1.4.1.77.1.2.25.1.1", "列出系统用户列表");
			map.put("1.3.6.1.4.1.77.1.4.1", "列出域名(工作组)");
			map.put("1.3.6.1.2.1.25.6.3.1.2", "列出安装的软件");
			
			map.put("1.3.6.1.2.1.1.2", "sysObjectID");
			map.put("1.3.6.1.2.1.1.3", "sysUpTime");
			map.put("1.3.6.1.2.1.1.4", "sysContact");
			map.put("1.3.6.1.2.1.1.5", "sysName");
			map.put("1.3.6.1.2.1.1.6", "sysLocation");
			//map.put("1.3.6.1.2.1.2.2.1.6","ifPhysAddress");
			//map.put(".1.3.6.1.4.1.2021.4.3","Total Swap Size");
			map.put("1.3.6.1.2.1.25.3.3.1.2.1","CPU使用率");
			//map.put("1.3.6.1.4.1.9.9.48.1.1.1.5","已用内存");
		    //map.put("1.3.6.1.4.1.9.9.48.1.1.1.6","空闲内存");
			Iterator<?> it = map.keySet().iterator();
			int i = 0 ; 
			while(it.hasNext()){
				String oid = it.next().toString();
				i++;				
				String[] sysDescs = SnmpManager.snmpWalk(IP, COMMUNITY,oid);
				System.out.println("========"+i+"====="+map.get(oid)+"======"+oid+"===============================================================================================");
				for(String sysdesc:sysDescs)				
					System.out.println(sysdesc);				
			}
//			
//			System.out.println("=======get============================================================================================/n");	
			
//			String[] sysDescs = mgr.snmpWalk("198.9.1.190", "public", "1.3.6.1.4.1.2021.11.9");
//			System.out.println(sysDescs.length);
//			for(String sysdesc:sysDescs)				
//				System.out.println(sysdesc);
			//1.3.6.1.4.1下的节点，都是私有节点，节点之下的实现是属于每个公司自己的规定了
			
//			map.put("1.3.6.1.4.1.9.2.1.58.0","CPU使用率");
//			map.put("1.3.6.1.4.1.9.9.48.1.1.1.5","已用内存");
//			map.put("1.3.6.1.4.1.9.9.48.1.1.1.6","空闲内存");
//		
 		    //System.out.println("test:"+mgr.snmpGet(IP, "private", "1.3.6.1.4.1.9.9.48.1.1.1.6"));
//			
			
			
			
//			System.out.println("内存总数:						"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.2.0"));
//			System.out.println("Disk/分区 Information OID:	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.3.1"));
//			System.out.println("也叫每个箸/块的大小:         	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.4.1"));
//			System.out.println("C盘分为多少块/箸, 总大小:    	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.5.1"));
//			System.out.println("C盘已经使用的块/箸:         	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.6.1"));
//			System.out.println("分配失败的块/箸:            	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.7.1"));
			//for(int i = 0 ; i < 10; i ++){
				//String aa = mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.3.1");
				//System.out.println(aa);
			//}
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
