package server.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import server.model.Account;

public class AccountDAO extends BaseDAO {

    public AccountDAO() {}

    // Helper method to manage connection and perform the operation
    private <T> T executeWithConnection(SQLFunction<Connection, T> sqlFunction) {
        openConnection();
        try {
            return sqlFunction.apply(connection);
        } catch (SQLException e) {
            handleSQLException(e); // Handle the SQLException
            return null; // Return null or a default value if needed
        } finally {
            closeConnection();
        }
    }

    public void insert(Account account) {
        String sql = "INSERT INTO accounts (name, group_name, balance) VALUES (?, ?, ?)";
        executeWithConnection(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, account.getName());
            pstmt.setString(2, account.getGroup());
            pstmt.setDouble(3, account.getBalance());
            pstmt.executeUpdate();
            return null;
        });
    }

    public void update(Account account) {
        String sql = "UPDATE accounts SET name = ?, group_name = ?, balance = ? WHERE id = ?";
        executeWithConnection(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, account.getName());
            pstmt.setString(2, account.getGroup());
            pstmt.setDouble(3, account.getBalance());
            pstmt.setInt(4, account.getId());
            pstmt.executeUpdate();
            return null;
        });
    }

    public void delete(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        executeWithConnection(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            pstmt.executeUpdate();
            return null;
        });
    }

    public List<Account> findAll() {
        String sql = "SELECT * FROM accounts";
        return executeWithConnection(connection -> {
            List<Account> accounts = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                accounts.add(mapRowToAccount(rs));
            }
            return accounts;
        });
    }

    public Account findAccount(int id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        return executeWithConnection(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }
            return null;
        });
    }

    public void adjustAccountBalance(int accountId, double amount) {
        String updateAccountBalanceSQL = "UPDATE accounts SET balance = ? WHERE id = ?";
        executeWithConnection(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(updateAccountBalanceSQL);
            double currentBalance = getAccountBalance(accountId, connection);
            pstmt.setDouble(1, currentBalance + amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
            return null;
        });
    }

    // Helper function to get the current account balance
    private double getAccountBalance(int accountId, Connection connection) {
        String getBalanceSQL = "SELECT balance FROM accounts WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(getBalanceSQL);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                // Handle the case when the account is not found
                return 0.0; // Return a default value or handle as needed
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return 0.0; // Return a default value or handle as needed
        }
    }

    // Map result set row to account object
    private Account mapRowToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("id"));
        account.setName(rs.getString("name"));
        account.setGroup(rs.getString("group_name"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }

    // Handle SQLException (log it or take appropriate action)
    private void handleSQLException(SQLException e) {
        // Log the exception or handle it as needed
        System.err.println("SQL Exception: " + e.getMessage());
        e.printStackTrace(); // Print stack trace for debugging
    }

    // Functional interface for SQL operations
    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}
