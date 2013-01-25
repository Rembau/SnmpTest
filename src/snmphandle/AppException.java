package snmphandle;

public class AppException extends Exception {
	private static final long serialVersionUID = 4079844561327690387L;

	public AppException(String str) {
		new Exception(str);
	}

	public AppException(String str, Exception e) {
		new Exception(str, e);
		e.printStackTrace();
	}
}
