package server.dao;

import java.sql.Connection;
import java.sql.SQLException;

import server.utils.DBConnection;

public abstract class BaseDAO {
	protected Connection connection;
	
	public void openConnection () {
		connection = DBConnection.getConnection();
	}
	
	public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
