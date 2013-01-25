package snmpPo;

import mib.MibDao;

public class TrapPo {

	private String addressIP;//�豸IP
	private String timeReceived;//trap����ʱ��
	private String message;//trap����
	private String enterprise;//�豸����OID
	private int genericType;
	private int specificType;
	private String OID;//����trap��OID
	private String trapName;
	
	
	public String getTrapName() {
		//����MibDao()������ȡtrap������
		OID = "."+this.getEnterprise()+".6."+this.getSpecificType();
//		MibDao dao = new MibDao();
		try {
			System.out.println("-OID------------------"+OID);
			trapName = MibDao.getIsmName(OID);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trapName;
	}
	
	
	public String getAddressIP() {
		return addressIP;
	}
	public void setAddressIP(String addressIP) {
		this.addressIP = addressIP;
	}
	public String getTimeReceived() {
		return timeReceived;
	}
	public void setTimeReceived(String timeReceived) {
		this.timeReceived = timeReceived;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	public int getGenericType() {
		return genericType;
	}
	public void setGenericType(int genericType) {
		this.genericType = genericType;
	}
	public int getSpecificType() {
		return specificType;
	}
	public void setSpecificType(int specificType) {
		this.specificType = specificType;
	}
		
}
