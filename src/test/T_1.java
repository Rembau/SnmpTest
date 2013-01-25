package test;

import java.io.IOException;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * Description:获取变量树.
 * @author Administrator
 *
 */
public class T_1 {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Address targetAddress = GenericAddress.parse("udp:192.168.28.2/161");   
        //Address targetAddress = GenericAddress.parse("udp:198.9.1.57/161");   
        CommunityTarget target = new CommunityTarget();    
        target.setCommunity(new OctetString("public"));    
        target.setAddress(targetAddress);    
        target.setTimeout(10000);    
        target.setVersion(SnmpConstants.version1);
        PDU pdu = new PDU();
        String oidstring="1.3.6.1.2.1.17.4.3.1.1";
        TransportMapping transport=null;
		try {
			transport = new DefaultUdpTransportMapping();
            transport.listen();    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Snmp snmp = new Snmp(transport);  
        for (int x=0;x<1000;x++) {
              pdu.add(new VariableBinding(new OID(oidstring)));
              pdu.setType(PDU.GETNEXT);

	            PDU response=null;
				try {
					response = snmp.sendPDU(pdu, target);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              Vector<?> variableBindings=response.getVariableBindings();
              String variable=variableBindings.toString();
              //System.out.println(variable);
              int i=variable.indexOf("=");
              int j=variable.length();
              oidstring=variable.substring(1,i-1);
              String routetype=variable.substring(i+1,j-1);
              System.out.print(oidstring);
              System.out.println(routetype);
              pdu.remove(0);
               }
	}

}
