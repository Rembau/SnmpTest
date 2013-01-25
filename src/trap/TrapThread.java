package trap;

/** *
 * 测试snmp4j 中trap的接收方法.这里只测试了v1和v2的trap.
 * trap接收原理:snmp实例在注册了实现CommandResponder的listener之后,可以通过异步调用的方法
 * 将收到内容输出.
 * listen()启动监听线程,该线程中的操作是监听指定端口,在收到trap告警之后将调用
 * listener.processPdu(CommandResponderEvent event)方法,由processPdu来处理trap信息.
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

	// Trap处理方法
	public static void TrapActionRun() {
		try {
			// 取出本机的IP地址
			InetAddress inet = InetAddress.getLocalHost();
			String IP = inet.getHostAddress();
			//IP="192.168.21.1";
			// snmp4j通过transportmapping的监听端口接收SNMP信息,所以这里初始化一个transportmapping
			// 注明本机的IP地址及接收trap的端口.
			TransportMapping transport = new DefaultUdpTransportMapping(
					new UdpAddress(IP + "/162"));
			// 创建一个处理消息的snmp实例
			Snmp snmp = new Snmp(transport);

			// CommandResponder是一个listener,用以处理获取的trap消息
			CommandResponder trapPrinter = new CommandResponder() {
				public synchronized void processPdu(CommandResponderEvent e) {
					// 创建一个TrapPo对象
					TrapPo po = new TrapPo();

					// 取设备的IP地址
					String IP = e.getPeerAddress().toString();
					System.out
							.println("*****************************************************************");

					String ip[] = IP.split("/");
					System.out.println("设备的IP地址-------------------" + ip[0]);
					po.setAddressIP(ip[0]);
					System.out.println("Community name----------------"
							+ new String(e.getSecurityName()));
					PDU command = e.getPDU();
					if (command != null) {
						// 这里示例输出trap的内容.具体的trap解析等工作在这里进行
						System.out.println(" PDU信息-----------"
								+ command.toString());
						// 获取相关的信息
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
						System.out.println("trap接受时间-----------------"
								+ receivedTime);
						po.setTimeReceived(receivedTime);

//						// 调用相关的方法进行trap信息的处理
//						System.out.println("TrapPo-----" + po.getAddressIP()
//								+ "-------" + po.getGenericType() + "---"
//								+ po.getMessage() + "---"
//								+ po.getSpecificType() + "----"
//								+ po.getTimeReceived() + "---"
//								+ po.getTrapName());

					}
				}

			};

			// 在snmp实例中添加CommandResponder listener
			snmp.addCommandResponder(trapPrinter);
			System.out.println(" start listening!");
			// 开始启动trap监听.listen()方法内部启动了一个线程,这个线程监听发送到transport中定义的端口
			// 的消息.
			transport.listen();
			System.out.println(" 测试监听" + transport.isListening());// 测试监听是否正常
			// 等待一段测试时间,在这段时间可以发送trap信息测试.
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
 * 运行结果如下: start listening! true
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
 * 这里测试发送了6条trap,第1,2条为v1trap,第3,4,5,6条为v2trap.最后一条时间稍有改变.
 * trap的输出的内容就这些,具体内容按照实际情况而定.
 */
