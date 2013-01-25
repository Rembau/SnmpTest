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

	// ȡ��������ֵ����
	public String getSnmpRequest(GetSnmpPo po) throws IOException {
		String message = null;
		// ͨ���������ֻ�ȡOID
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
		// �����豸IP
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

				// �ַ�ת������
				message = var.toString();
			}
		}
		return message;
	}

	// ����ĳ������ֵ����
	public boolean setSnmpRequest(SetSnmpPo po) throws IOException {
		boolean flag = false;
		// ͨ���������ֻ�ȡOID
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
		// �����豸����
		target.setTargetHost(po.getIsm3IpAddr());// IP��ַ
		target.setTargetPort(161);// �˿ں�161
		target.setObjectID(OID + "." + po.getModelID());
		target.setCommunity(po.getCommunity());
		target.setWriteCommunity(po.getWriteCommunity());

		// // �ڲ��������
		// MibOperations mibOps = new MibOperations();
		// try {
		// mibOps.loadMibModule("ism3602.mib");
		// } catch (Exception e1) {
		// System.err.println("Error loading MIBs: " + e1);
		// }
		// ���б����MIB��
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
							// �˿ں�������
							flag = false;
						}
					}
					// ����ʧ��
				} else {
					System.out.println("Response: " + result);
					System.out.println("�����޸ĳɹ�");
					// �����ɹ�
				}

			} catch (DataException e) {
				System.out.println("���ؾ��棺�������Բ����޸ģ�");
				e.printStackTrace();
				// ���Բ����޸�
			}
		}

		return flag;
	}

}
