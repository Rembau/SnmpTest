package snmpDao;

import java.io.IOException;
import com.adventnet.snmp.beans.DataException;
import com.adventnet.snmp.beans.SnmpTarget;
import mib.MibDao;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.smi.Variable;

import snmpPo.SnmpInterf;
import snmpPo.GetSnmpPo;
import snmpPo.SetSnmpPo;


public class SnmpDao implements SnmpInterf {

	// 取单个属性值方法
	public String getSnmpRequest(GetSnmpPo po) throws IOException {
		String message = null;
		// 通过属性名字获取OID
		String OID = null;
		OID = po.getOID();
		if (OID == null) {
			try {
				OID = MibDao.getOID(po.getIsmName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Address targetAddress = GenericAddress.parse("udp:"
				+ po.getIsm3IpAddr() + "/161");
		// 解析设备IP
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(po.getCommunity()));
		target.setAddress(targetAddress);
		target.setTimeout(po.getTimeOut());
		target.setVersion(po.getIsm3Version());
		// System.out.println(community+"--------------"+OID+"--------------"+versionId+"--------------"+IP);

		// creating PDU
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(OID + "." + po.getModelID())));
		// pdu.add(new VariableBinding(new OID(new int[] {1,3,6,1,2,1,1,2})));
		pdu.setType(PDU.GET);

		TransportMapping transport = new DefaultUdpTransportMapping();
		transport.listen();
		Snmp snmp = new Snmp(transport);
		ResponseEvent response = snmp.send(pdu, target);
		if (response != null) {
			PDU respPdu = response.getResponse();
			for (int i = 0; i < respPdu.size(); i++) {
				VariableBinding varBinding = respPdu.get(i);
				Variable var = varBinding.getVariable();

				// 字符转换问题
				message = var.toString();
			}
		}
		return message;
	}

	// 设置某个属性值方法
	public boolean setSnmpRequest(SetSnmpPo po) throws IOException {
		boolean flag = false;
		// 通过属性名字获取OID
		String OID = null;
		OID = po.getOID();
		if (OID == null) {
			try {
				OID = MibDao.getOID(po.getIsmName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		SnmpTarget target = new SnmpTarget();
		// 设置设备属性
		target.setTargetHost(po.getIsm3IpAddr());// IP地址
		target.setTargetPort(161);// 端口号161
		target.setObjectID(OID + "." + po.getModelID());
		target.setCommunity(po.getCommunity());
		target.setWriteCommunity(po.getWriteCommunity());

		// // 内部库的设置
		// MibOperations mibOps = new MibOperations();
		// try {
		// mibOps.loadMibModule("ism3602.mib");
		// } catch (Exception e1) {
		// System.err.println("Error loading MIBs: " + e1);
		// }
		// 进行编译的MIB库
		target.setMibOperations(MibDao.mibOps);

		if (po.getWriteCommunity().equals("") || po.getValue().equals("")) {
			System.out.println("No WriteCommunity Or Set Value !,Error!");
		} else {
			try {
				String result = target.snmpSet(po.getValue());
				if (result == null) {
					System.err.println("Failed: " + target.getErrorString());
					if (target.getErrorString().length() > 80) {
						flag = true;
						if (target.getErrorIndex() == 1) {
							// 端口好有问题
							flag = false;
						}
					}
					// 连接失败
				} else {
					System.out.println("Response: " + result);
					System.out.println("属性修改成功");
					// 操作成功
				}

			} catch (DataException e) {
				System.out.println("严重警告：此项属性不能修改！");
				e.printStackTrace();
				// 属性不可修改
			}
		}

		return flag;
	}

}
