package snmpPo;

public class SetSnmpPo {

	private String ismName;//��������
	private String ism3IpAddr;//IP��ַ
	private int modelID;//�豸��
	private String community = "public";//������
	private String writeCommunity = "public";//д������
	private String value;//���Ե�ֵ
	private String OID;//����OID�������ű�ismName���ߵ�Ȩ�ޣ�
	

	public String getOID() {
		return OID;
	}
	public void setOID(String oid) {
		OID = oid;
	}
	public int getModelID() {
		return modelID;
	}
	public void setModelID(int modelID) {
		this.modelID = modelID;
	}
	public String getIsmName() {
		return ismName;
	}
	public void setIsmName(String ismName) {
		this.ismName = ismName;
	}
	public String getIsm3IpAddr() {
		return ism3IpAddr;
	}
	public void setIsm3IpAddr(String ism3IpAddr) {
		this.ism3IpAddr = ism3IpAddr;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getWriteCommunity() {
		return writeCommunity;
	}
	public void setWriteCommunity(String writeCommunity) {
		this.writeCommunity = writeCommunity;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
