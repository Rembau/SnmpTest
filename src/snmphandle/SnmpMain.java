package snmphandle;
	import java.io.IOException;    
	   
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
	import org.snmp4j.smi.Variable;    
	   
	public class SnmpMain {    
	   
	    public static void main(String[] args) {    	    	
	         //Address targetAddress = GenericAddress.parse("udp:127.0.0.1/161");    
	         Address targetAddress = GenericAddress.parse("udp:192.168.21.1/161");   
	         //Address targetAddress = GenericAddress.parse("udp:198.9.1.57/161");   
	         CommunityTarget target = new CommunityTarget();    
	         target.setCommunity(new OctetString("public"));    
	         target.setAddress(targetAddress);    
	         target.setTimeout(10000);    
	         target.setVersion(SnmpConstants.version1);    
	        // creating PDU    
	         PDU pdu = new PDU();    
	   
	         pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1")));    
	         pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.2")));    
	         pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.3.1.1.1.1630.1.192.168.21.158")));    
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.25506.1.52.1.2.1.1.4")));    
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.2010.11.9.0")));    
	         pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.3.1.1.3.1630.1.1")));   
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.9.2.4.9.1.3")));
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.4976.2.2.1")));
	         pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.4.20.1.1")));
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.9.1.4")));
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.9.1.2")));
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.9.1.3")));
	         pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.9.1.1")));
	        // pdu.add(new VariableBinding(new OID(new int[] {1,3,6,1,2,1,1,2})));    
	         pdu.setType(PDU.GETNEXT);    
	   
	        try {    
	             TransportMapping transport = new DefaultUdpTransportMapping();    
	             transport.listen();    
	             Snmp snmp = new Snmp(transport);    
	   
	             ResponseEvent response = snmp.send(pdu, target);    
	            if (response != null) {    
	                 PDU respPdu = response.getResponse();   
	                 System.out.println("respPdu.size()=" + respPdu.size());
	                for (int i = 0; i < respPdu.size(); i++) {    
	                     VariableBinding varBinding = respPdu.get(i);    
	                     Variable var = varBinding.getVariable();    
	                     System.out.println(i+" "+var.toString());    
	                 }    
	             }    
	         } catch (IOException e) {    
	             e.printStackTrace();    
	         }    
	     }    
	}   


