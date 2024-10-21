package server.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import server.model.Category;
import server.utils.DBConnection;

public class CategoryDAO extends BaseDAO {
    private Connection connection;

    public CategoryDAO() {
        connection = DBConnection.getConnection();
    }

    public void insert(Category category) {
        String sql = "INSERT INTO categories (name, budget, type) VALUES (?, ?, ?)";
        try {
        	openConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, category.getName());
            stmt.setDouble(2, category.getBudget());
            stmt.setString(3, category.getType());
            stmt.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Category category) {
        String sql = "UPDATE categories SET name = ?, budget = ? WHERE id = ?";
        try {
        	openConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, category.getName());
            stmt.setDouble(2, category.getBudget());
            stmt.setInt(3, category.getId());
            stmt.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int categoryId) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try {
        	openConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try {
        	openConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categories.add(mapRowToCategory(rs));
            }
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public List<Category> findAllIncome() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE type='income'";
        try {
        	openConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categories.add(mapRowToCategory(rs));
            }
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public List<Category> findAllExpense() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE type='expense'";
        try {
        	openConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categories.add(mapRowToCategory(rs));
            }
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public Category findCategory (int id) {
    	String sql = "SELECT * FROM categories WHERE id = " + String.valueOf(id);
    	Category category = null;
        try {
        	openConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                category = mapRowToCategory(rs);
            }
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    private Category mapRowToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setBudget(rs.getDouble("budget"));
        category.setType(rs.getString("type"));
        return category;
    }

    // Additional methods as needed
}
