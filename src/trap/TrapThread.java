package trap;

/** *
 * ����snmp4j ��trap�Ľ��շ���.����ֻ������v1��v2��trap.
 * trap����ԭ��:snmpʵ����ע����ʵ��CommandResponder��listener֮��,����ͨ���첽���õķ���
 * ���յ��������.
 * listen()���������߳�,���߳��еĲ����Ǽ���ָ���˿�,���յ�trap�澯֮�󽫵���
 * listener.processPdu(CommandResponderEvent event)����,��processPdu������trap��Ϣ.
 */

import java.net.InetAddress;
import java.util.Date;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import snmpPo.TrapPo;
import util.UtilTrap;
import mib.MibDao;
public class TrapThread {

	// Trap������
	public static void TrapActionRun() {
		try {
			// ȡ��������IP��ַ
			InetAddress inet = InetAddress.getLocalHost();
			String IP = inet.getHostAddress();
			//IP="192.168.21.1";
			// snmp4jͨ��transportmapping�ļ����˿ڽ���SNMP��Ϣ,���������ʼ��һ��transportmapping
			// ע��������IP��ַ������trap�Ķ˿�.
			TransportMapping transport = new DefaultUdpTransportMapping(
					new UdpAddress(IP + "/162"));
			// ����һ��������Ϣ��snmpʵ��
			Snmp snmp = new Snmp(transport);

			// CommandResponder��һ��listener,���Դ����ȡ��trap��Ϣ
			CommandResponder trapPrinter = new CommandResponder() {
				public synchronized void processPdu(CommandResponderEvent e) {
					// ����һ��TrapPo����
					TrapPo po = new TrapPo();

					// ȡ�豸��IP��ַ
					String IP = e.getPeerAddress().toString();
					System.out
							.println("*****************************************************************");

					String ip[] = IP.split("/");
					System.out.println("�豸��IP��ַ-------------------" + ip[0]);
					po.setAddressIP(ip[0]);
					System.out.println("Community name----------------"
							+ new String(e.getSecurityName()));
					PDU command = e.getPDU();
					if (command != null) {
						// ����ʾ�����trap������.�����trap�����ȹ������������
						System.out.println(" PDU��Ϣ-----------"
								+ command.toString());
						// ��ȡ��ص���Ϣ
						String s[] = command.toString().split(",");
						for (int i = 0; i < s.length; i++) {
							String xx = s[i];
							String x[] = xx.split("=");
							// System.out.println("--------------"+x[0]);
							// System.out.println("--------------"+x[1]);
							if (x[0].equals("enterprise")) {
								System.out.println("enterprise------" + x[1]);
								po.setEnterprise(x[1]);
							}
							if (x[0].equals("genericTrap")) {
								System.out.println("genericTrap------" + x[1]);
								System.out.println("genericTrap content------" + UtilTrap.parseTrapContent(x[1]));
								po.setGenericType(Integer.parseInt(x[1]));
							}
							if (x[0].equals("specificTrap")) {
								System.out.println("specificTrap------" + x[1]);
								po.setSpecificType(Integer.parseInt(x[1]));
							}
						}

						System.out.println("message----------------"
								+ command.getVariableBindings().toString()
										.replace("[", ".").replace("]", ""));						
						String message = command.getVariableBindings()
								.toString().replace("[", ".").replace("]", "");
						if(message.length()>2)
						{
						   System.out.println("message content----------------" + MibDao.getIsmName(new String(message.substring(0,message.indexOf("=")))) + "=" + new String(message.substring(message.indexOf("=")+1,message.length())));
						}
						po.setMessage(message);

						java.text.SimpleDateFormat date = new java.text.SimpleDateFormat(
								"MM/dd/yyyy, hh:mm:ss");
						String receivedTime = date.format(new Date(System
								.currentTimeMillis()));
						System.out.println("trap����ʱ��-----------------"
								+ receivedTime);
						po.setTimeReceived(receivedTime);

//						// ������صķ�������trap��Ϣ�Ĵ���
//						System.out.println("TrapPo-----" + po.getAddressIP()
//								+ "-------" + po.getGenericType() + "---"
//								+ po.getMessage() + "---"
//								+ po.getSpecificType() + "----"
//								+ po.getTimeReceived() + "---"
//								+ po.getTrapName());

					}
				}

			};

			// ��snmpʵ�������CommandResponder listener
			snmp.addCommandResponder(trapPrinter);
			System.out.println(" start listening!");
			// ��ʼ����trap����.listen()�����ڲ�������һ���߳�,����̼߳������͵�transport�ж���Ķ˿�
			// ����Ϣ.
			transport.listen();
			System.out.println(" ���Լ���" + transport.isListening());// ���Լ����Ƿ�����
			// �ȴ�һ�β���ʱ��,�����ʱ����Է���trap��Ϣ����.
			Thread.sleep(1800000);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		TrapActionRun();
	}
}

/**
 * ���н������: start listening! true
 * V1TRAP[reqestID=0,timestamp=0:00:00.00,enterprise=0.0,genericTrap=2,specificTrap=0,
 * VBS[]]
 * V1TRAP[reqestID=0,timestamp=0:00:00.00,enterprise=0.0,genericTrap=2,specificTrap=0,
 * VBS[]] TRAP[requestID=1762734632, errorStatus=Success(0), errorIndex=0,
 * VBS[1.3.6.1.2.1.1.3.0 = 0:00:00.00; 1.3.6.1.6.3.1.1.4.1.0 =
 * 1.3.6.1.6.3.1.1.5.3]] TRAP[requestID=1762734633, errorStatus=Success(0),
 * errorIndex=0, VBS[1.3.6.1.2.1.1.3.0 = 0:00:00.00; 1.3.6.1.6.3.1.1.4.1.0 =
 * 1.3.6.1.6.3.1.1.5.3]] TRAP[requestID=1762734634, errorStatus=Success(0),
 * errorIndex=0, VBS[1.3.6.1.2.1.1.3.0 = 0:00:00.00; 1.3.6.1.6.3.1.1.4.1.0 =
 * 1.3.6.1.6.3.1.1.5.3]] TRAP[requestID=1762734635, errorStatus=Success(0),
 * errorIndex=0, VBS[1.3.6.1.2.1.1.3.0 = 0:00:01.11; 1.3.6.1.6.3.1.1.4.1.0 =
 * 1.3.6.1.6.3.1.1.5.3]]
 * 
 * ������Է�����6��trap,��1,2��Ϊv1trap,��3,4,5,6��Ϊv2trap.���һ��ʱ�����иı�.
 * trap����������ݾ���Щ,�������ݰ���ʵ���������.
 */
