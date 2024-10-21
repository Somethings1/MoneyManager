package server.service;

import server.dao.CategoryDAO;
import server.model.Category;

import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        categoryDAO = new CategoryDAO();
    }

    public void addCategory(Category category) {
        categoryDAO.insert(category);
    }

    public void updateCategory(Category category) {
        categoryDAO.update(category);
    }

    public void removeCategory(int categoryId) {
        categoryDAO.delete(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }
    
    public List<Category> getAllIncomeCategories() {
        return categoryDAO.findAllIncome();
    }
    public List<Category> getAllExpenseCategories() {
        return categoryDAO.findAllExpense();
    }
    
    public Category getCategory (int id) {
    	return categoryDAO.findCategory(id);
    }
}
