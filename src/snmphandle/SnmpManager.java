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
 * SNMP管理类
 */
public class SnmpManager {

	private static final Log log = LogFactory.getLog(SnmpManager.class);
	
	private static int version = 0; // SNMP版本, 0表示版本1
	
	private static String protocol = "udp"; // 监控时使用的协议
	
	private static String port = "161"; // 监控时使用的端口
		
	/**
	 * 获取SNMP节点值
	 * 
	 * @param ipAddress 目标IP地址
	 * @param community 公同体
	 * @param oid 对象ID
	 * @return String 监控结果代号
	 * @throws AppException
	 */

	public static String snmpGet(String ipAddress, String community, String oid) throws AppException {
		String resultStat = null; // 监控结果状态
		
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

		// 创建共同体对象CommunityTarget
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(targetAddress);
		target.setVersion(SnmpConstants.version1);
		target.setTimeout(2000);
		target.setRetries(1);

		DefaultUdpTransportMapping udpTransportMap = null;
		Snmp snmp = null;
		try {
			// 发送同步消息
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
			throw new AppException("获取SNMP节点状态时发生错误!", e);
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
	 * 走访SNMP节点
	 * 
	 * @param ipAddress 目标IP地址
	 * @param community 共同体
	 * @param oid 节点起始对象标志符
	 * @return String[] 走方结果
	 * @throws AppException
	 */
	public static String[] snmpWalk(String ipAddress, String community, String oid) throws AppException {
		String[] returnValueString = null; // oid走访结果数组
		
		SNMPv1CommunicationInterface comInterface = null;
		try {
			InetAddress hostAddress = InetAddress.getByName(ipAddress);
			comInterface = new SNMPv1CommunicationInterface(
					version, hostAddress, community);
			comInterface.setSocketTimeout(2000);
			
			// 返回所有以oid开始的管理信息库变量值
			SNMPVarBindList tableVars = comInterface.retrieveMIBTable(oid);
			returnValueString = new String[tableVars.size()];
			
			// 循环处理所有以oid开始的节点的返回值
			for (int i = 0; i < tableVars.size(); i++) {
				SNMPSequence pair = (SNMPSequence) tableVars.getSNMPObjectAt(i); // 获取SNMP序列对象, 即(OID,value)对
				SNMPObject snmpValue = pair.getSNMPObjectAt(1); // 获取某个节点的返回值
				String typeString = snmpValue.getClass().getName(); // 获取SNMP值类型名
				// 设置返回值
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
				log.error("走访IP为" + ipAddress + ", OID为" + oid + " 时超时!");
			}
			returnValueString = null;
		} catch (Exception e) {
			throw new AppException("SNMP走访节点时发生错误!", e);
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

