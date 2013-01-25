package snmphandle;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import snmp.SNMPObject;
import snmp.SNMPSequence;
import snmp.SNMPVarBindList;
import snmp.SNMPv1CommunicationInterface;

/**
 * SNMP������
 */
public class SnmpManager {

	private static final Log log = LogFactory.getLog(SnmpManager.class);
	
	private static int version = 0; // SNMP�汾, 0��ʾ�汾1
	
	private static String protocol = "udp"; // ���ʱʹ�õ�Э��
	
	private static String port = "161"; // ���ʱʹ�õĶ˿�
		
	/**
	 * ��ȡSNMP�ڵ�ֵ
	 * 
	 * @param ipAddress Ŀ��IP��ַ
	 * @param community ��ͬ��
	 * @param oid ����ID
	 * @return String ��ؽ������
	 * @throws AppException
	 */

	public static String snmpGet(String ipAddress, String community, String oid) throws AppException {
		String resultStat = null; // ��ؽ��״̬
		
		StringBuffer address = new StringBuffer();
		address.append(protocol);
		address.append(":");
		address.append(ipAddress);
		address.append("/");
		address.append(port);
		
//		Address targetAddress = GenericAddress.parse(protocol + ":" + ipAddress + "/" + port);
		Address targetAddress = GenericAddress.parse(address.toString());
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(oid)));
		pdu.setType(PDU.GET);

		// ������ͬ�����CommunityTarget
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(targetAddress);
		target.setVersion(SnmpConstants.version1);
		target.setTimeout(2000);
		target.setRetries(1);

		DefaultUdpTransportMapping udpTransportMap = null;
		Snmp snmp = null;
		try {
			// ����ͬ����Ϣ
			udpTransportMap = new DefaultUdpTransportMapping();
			udpTransportMap.listen();
			snmp = new Snmp(udpTransportMap);
			ResponseEvent response = snmp.send(pdu, target);
			PDU resposePdu = response.getResponse();

			if (resposePdu == null) {
				log.info(ipAddress + ": Request timed out.");
			} else {
				//errorStatus = resposePdu.getErrorStatus();
				Object obj = resposePdu.getVariableBindings().firstElement();
				VariableBinding variable = (VariableBinding) obj;
				resultStat = variable.getVariable().toString();
			}
		} catch (Exception e) {
			throw new AppException("��ȡSNMP�ڵ�״̬ʱ��������!", e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException e) {
					snmp = null;
				}
			}
			if (udpTransportMap != null) {
				try {
					udpTransportMap.close();
				} catch (IOException e) {
					udpTransportMap = null;
				}
			}
		}
		
		if (log.isInfoEnabled()) {
			log.info("IP:" + ipAddress + " resultStat:" + resultStat);
		}
		
		return resultStat;
	}
	
	
	/**
	 * �߷�SNMP�ڵ�
	 * 
	 * @param ipAddress Ŀ��IP��ַ
	 * @param community ��ͬ��
	 * @param oid �ڵ���ʼ�����־��
	 * @return String[] �߷����
	 * @throws AppException
	 */
	public static String[] snmpWalk(String ipAddress, String community, String oid) throws AppException {
		String[] returnValueString = null; // oid�߷ý������
		
		SNMPv1CommunicationInterface comInterface = null;
		try {
			InetAddress hostAddress = InetAddress.getByName(ipAddress);
			comInterface = new SNMPv1CommunicationInterface(
					version, hostAddress, community);
			comInterface.setSocketTimeout(2000);
			
			// ����������oid��ʼ�Ĺ�����Ϣ�����ֵ
			SNMPVarBindList tableVars = comInterface.retrieveMIBTable(oid);
			returnValueString = new String[tableVars.size()];
			
			// ѭ������������oid��ʼ�Ľڵ�ķ���ֵ
			for (int i = 0; i < tableVars.size(); i++) {
				SNMPSequence pair = (SNMPSequence) tableVars.getSNMPObjectAt(i); // ��ȡSNMP���ж���, ��(OID,value)��
				SNMPObject snmpValue = pair.getSNMPObjectAt(1); // ��ȡĳ���ڵ�ķ���ֵ
				String typeString = snmpValue.getClass().getName(); // ��ȡSNMPֵ������
				// ���÷���ֵ
				if (typeString.equals("snmp.SNMPOctetString")) {
					String snmpString = snmpValue.toString();
					int nullLocation = snmpString.indexOf('\0');
					if (nullLocation >= 0)
						snmpString = snmpString.substring(0, nullLocation);
					returnValueString[i] = snmpString;
				} else {
					returnValueString[i] = snmpValue.toString();
				}
			}
		} catch (SocketTimeoutException ste) {
			if (log.isErrorEnabled()) {
				log.error("�߷�IPΪ" + ipAddress + ", OIDΪ" + oid + " ʱ��ʱ!");
			}
			returnValueString = null;
		} catch (Exception e) {
			throw new AppException("SNMP�߷ýڵ�ʱ��������!", e);
		} finally {
			if (comInterface != null) {
				try {
					comInterface.closeConnection();
				} catch (SocketException e) {
					comInterface = null;
				}
			}
		}
		
		return returnValueString;
	}
} 

