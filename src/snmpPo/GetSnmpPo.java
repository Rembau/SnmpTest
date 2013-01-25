package snmpPo;

public class GetSnmpPo {

	private String ismName;//属性名字
	private int modelID;//设备号
	private String ism3IpAddr;//IP地址
	private String community = "public";//团体字
	private int ism3Version = 0;//版本号
	private int timeOut = 10000;//超时时间	
	private String OID;//属性OID（它有着比ismName更高的权限）
	
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
