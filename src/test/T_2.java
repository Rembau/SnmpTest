package test;

import java.io.IOException;
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

public class T_2 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Address targetAddress = GenericAddress.parse("udp:192.168.28.2/161"); 
													//172.16.2.2 192.168.0.252
        //Address targetAddress = GenericAddress.parse("udp:198.9.1.57/161");   
        CommunityTarget target = new CommunityTarget();    
        target.setCommunity(new OctetString("public"));    
        target.setAddress(targetAddress);    
        target.setTimeout(3000);    
        target.setVersion(SnmpConstants.version1);
        PDU pdu = new PDU();
        //String oidstring="1.3.6.1.2.1.3.1.1";
        String oidstring="1.3.6.1.2.1.2.2.1.1";//1.3.6.1.4.1//1.3.6.1.2.1.4.21.1.7
        String oidstring_8="1.3.6.1.2.1.4.21.1.8";
        String oid=oidstring;
        TransportMapping transport=null;
		try {
			transport = new DefaultUdpTransportMapping();
            transport.listen();    
		} catch (IOException e) {
			e.printStackTrace();
		}
        Snmp snmp = new Snmp(transport);  
        PDU response=null;
		Vector<?> variableBindings=null;
		ResponseEvent response1=null;
        pdu.setType(PDU.GETNEXT);
        int direct=0;
        for (int x=0;x<600;x++) {
              pdu.add(new VariableBinding(new OID(oidstring)));
			  while(true){
				  try{ 				  
					  response1 = snmp.send(pdu, target);
					  response =response1.getResponse();
	            	  variableBindings=response.getVariableBindings();
	            	  break;
	              }catch(Exception e){
	            	  System.out.println("==========重复=========");
	              }
			  }
              String variable=variableBindings.toString();
              //System.out.println(variable);
              int i=variable.indexOf("=");
              int j=variable.length();
              oidstring=variable.substring(1,i-1);
              String value=variable.substring(i+1,j-1);
              System.out.print(x+" "+oidstring);
              System.out.println(value);
              //System.out.println(oidstring.replace(oid, ""));
             /* if(!oidstring.startsWith(oid) && !oidstring.startsWith(oidstring_8)){
            	  break;
              }
              if(oidstring.replace(oid, "").startsWith(".0") ||
            		  oidstring.replace(oid, "").startsWith(".10.") ||
            		  oidstring.replace(oid, "").startsWith(".172") ||
            		  oidstring.replace(oid, "").startsWith(".192")){
                  System.out.print(x+" "+oidstring);
                  System.out.println(value);  
              }
              if(oidstring.replace(oidstring_8, "").startsWith(".0") ||
            		  oidstring.replace(oidstring_8, "").startsWith(".10.") ||
            		  oidstring.replace(oidstring_8, "").startsWith(".172") ||
            		  oidstring.replace(oidstring_8, "").startsWith(".192")){
                  System.out.print(x+" "+oidstring);
                  System.out.println(value);  
              }
              if(oidstring.startsWith(oidstring_8) && value.equals(" 3")){
            	  direct++;
              }*/
              pdu.remove(0);
        }
        System.out.println("直接路由的数量有："+direct);
	}
}
