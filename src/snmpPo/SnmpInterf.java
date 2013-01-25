package snmpPo;
import java.io.IOException;

public interface SnmpInterf {

	//取单个属性值方法
	public String getSnmpRequest(GetSnmpPo po)  throws IOException;
	
	//设置某个属性值方法
	public  boolean setSnmpRequest(SetSnmpPo po) throws IOException;

}
