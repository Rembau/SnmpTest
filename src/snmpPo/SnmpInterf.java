package snmpPo;
import java.io.IOException;

public interface SnmpInterf {

	//ȡ��������ֵ����
	public String getSnmpRequest(GetSnmpPo po)  throws IOException;
	
	//����ĳ������ֵ����
	public  boolean setSnmpRequest(SetSnmpPo po) throws IOException;

}
