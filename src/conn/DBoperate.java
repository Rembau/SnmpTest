package conn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBoperate {
	private Conn conn;
	public DBoperate(){
		conn = new Conn();
	}
	public void insert(String sql){
		try {
			conn.getOldStmt().executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(sql+"³ö´í");
			//e.printStackTrace();
		}
	}
	public void update(String sql){
		try {
			conn.getOldStmt().executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(sql+"³ö´í");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet select(String sql){
		try {
			return conn.getNewStmt().executeQuery(sql);
		} catch (SQLException e) {
			System.out.println(sql+"³ö´í");
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {

	}

}
