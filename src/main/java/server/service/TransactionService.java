package server.service;

import server.dao.TransactionDAO;
import server.model.Transaction;

import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionService() {
        transactionDAO = new TransactionDAO();
    }

    public void addTransaction(Transaction transaction) {
        transactionDAO.insert(transaction);
    }

    public void updateTransaction(Transaction transaction) {
    	System.out.println(transaction.getType());
        transactionDAO.update(transaction);
    }

    public void removeTransaction(int transactionId) {
        transactionDAO.remove(transactionId);
    }

    public Transaction getTransaction(int id) {
        return transactionDAO.findById(id);
    }

    public List<Transaction> getTransactionsForCurrentMonth() {
        return transactionDAO.findByCurrentMonth();
    }
    
    public List<Transaction> getTransactionsByMonth (int month, int year) {
    	return transactionDAO.findByMonth(month, year);
    }
    
    public List<Transaction> getTransactionsByYear (int year) {
    	return transactionDAO.findByYear(year);
    }
    // Additional methods for filtering and sorting transactions can be added here
}
