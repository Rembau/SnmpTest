package snmpPo;

public class SetSnmpPo {

	private String ismName;//属性名字
	private String ism3IpAddr;//IP地址
	private int modelID;//设备号
	private String community = "public";//团体字
	private String writeCommunity = "public";//写团体字
	private String value;//属性的值
	private String OID;//属性OID（它有着比ismName更高的权限）
	

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
