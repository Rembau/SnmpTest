package test;

import org.snmp4j.*;
import org.snmp4j.transport.*;
import java.io.*;
import org.snmp4j.smi.*;
import org.snmp4j.mp.*;
import org.snmp4j.event.*;

class SnmpGet{
    public static void main(String[] args){
    
    try{
        //设定CommunityTarget
        CommunityTarget myTarget = new CommunityTarget();
        Address deviceAdd = GenericAddress.parse("udp:192.168.21.1/161");
        myTarget.setAddress(deviceAdd);
        myTarget.setCommunity(new OctetString("public"));
        myTarget.setRetries(2);
        myTarget.setTimeout(5*60);    
        myTarget.setVersion(SnmpConstants.version2c);//org.snmp4j.mp.*;
        //设定采取的协议
        TransportMapping transport1  = new DefaultUdpTransportMapping();
        Snmp protocol = new Snmp(transport1);
        transport1.listen();
        //获取mib
        PDU request = new PDU();
        String oidstr ="1.3.6.1.2.1.2.2.1.5";
        VariableBinding var = new VariableBinding(new OID(oidstr));
        request.add(var);
        request.setType(PDU.GETNEXT);
        ResponseEvent responseEvent = protocol.send(request, myTarget);
        PDU response=responseEvent.getResponse();
        //输出
        if(response != null){
            System.out.println("request.size()="+request.size());
            System.out.println("response.size()="+response.size());
            VariableBinding vb = response.get(0);
            System.out.println(vb);  
        }
        
      }catch(IOException e){
          e.printStackTrace();
          System.out.println(e.getMessage());
          return;
      }
    }
}
