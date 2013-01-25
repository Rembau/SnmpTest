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
			map.put("1.3.6.1.2.1.1.1", "ϵͳ����");
			//map.put("1.3.6.1.2.1.25.1.7", "�����������ڴ�");//???
			map.put("1.3.6.1.2.1.25.2.2", "�����������ڴ�");//windows����ȷ			
			//map.put("1.3.6.1.2.1.25.4.2.1.2", "�г�ϵͳ����");
			//map.put("1.3.6.1.4.1.77.1.2.25.1.1", "�г�ϵͳ�û��б�");
			map.put("1.3.6.1.4.1.77.1.4.1", "�г�����(������)");
			map.put("1.3.6.1.2.1.25.6.3.1.2", "�г���װ�����");
			
			map.put("1.3.6.1.2.1.1.2", "sysObjectID");
			map.put("1.3.6.1.2.1.1.3", "sysUpTime");
			map.put("1.3.6.1.2.1.1.4", "sysContact");
			map.put("1.3.6.1.2.1.1.5", "sysName");
			map.put("1.3.6.1.2.1.1.6", "sysLocation");
			//map.put("1.3.6.1.2.1.2.2.1.6","ifPhysAddress");
			//map.put(".1.3.6.1.4.1.2021.4.3","Total Swap Size");
			map.put("1.3.6.1.2.1.25.3.3.1.2.1","CPUʹ����");
			//map.put("1.3.6.1.4.1.9.9.48.1.1.1.5","�����ڴ�");
		    //map.put("1.3.6.1.4.1.9.9.48.1.1.1.6","�����ڴ�");
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
			//1.3.6.1.4.1�µĽڵ㣬����˽�нڵ㣬�ڵ�֮�µ�ʵ��������ÿ����˾�Լ��Ĺ涨��
			
//			map.put("1.3.6.1.4.1.9.2.1.58.0","CPUʹ����");
//			map.put("1.3.6.1.4.1.9.9.48.1.1.1.5","�����ڴ�");
//			map.put("1.3.6.1.4.1.9.9.48.1.1.1.6","�����ڴ�");
//		
 		    //System.out.println("test:"+mgr.snmpGet(IP, "private", "1.3.6.1.4.1.9.9.48.1.1.1.6"));
//			
			
			
			
//			System.out.println("�ڴ�����:						"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.2.0"));
//			System.out.println("Disk/���� Information OID:	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.3.1"));
//			System.out.println("Ҳ��ÿ����/��Ĵ�С:         	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.4.1"));
//			System.out.println("C�̷�Ϊ���ٿ�/��, �ܴ�С:    	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.5.1"));
//			System.out.println("C���Ѿ�ʹ�õĿ�/��:         	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.6.1"));
//			System.out.println("����ʧ�ܵĿ�/��:            	"+mgr.snmpGet(IP, COMMUNITY, "1.3.6.1.2.1.25.2.3.1.7.1"));
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
