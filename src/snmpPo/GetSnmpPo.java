package snmpPo;

public class GetSnmpPo {

	private String ismName;//��������
	private int modelID;//�豸��
	private String ism3IpAddr;//IP��ַ
	private String community = "public";//������
	private int ism3Version = 0;//�汾��
	private int timeOut = 10000;//��ʱʱ��	
	private String OID;//����OID�������ű�ismName���ߵ�Ȩ�ޣ�
	
	public String getOID() {
		return OID;
	}
	public void setOID(String oid) {
		OID = oid;
	}
	public String getIsmName() {
		return ismName;
	}
	public void setIsmName(String ismName) {
		this.ismName = ismName;
	}
	
	public int getModelID() {
		return modelID;
	}
	public void setModelID(int modelID) {
		this.modelID = modelID;
	}
	public String getIsm3IpAddr() {
		return ism3IpAddr;
	}
	public void setIsm3IpAddr(String ism3IpAddr) {
		this.ism3IpAddr = ism3IpAddr;
	}
	public int getIsm3Version() {
		return ism3Version;
	}
	public void setIsm3Version(int ism3Version) {
		this.ism3Version = ism3Version;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}

	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

}
