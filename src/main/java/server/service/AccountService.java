package server.service;

import server.dao.AccountDAO;
import server.model.Account;

import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public void addAccount(Account account) {
        accountDAO.insert(account);
    }

    public void updateAccount(Account account) {
        accountDAO.update(account);
    }

    public void removeAccount(int accountId) {
        accountDAO.delete(accountId);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }
    
    public Account getAccount (int id) {
    	return accountDAO.findAccount(id);
    }
    
    public void adjustBalance (int id, double amount) {
    	accountDAO.adjustAccountBalance(id, amount);
    }
}
