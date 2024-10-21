import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.model.Account;
import server.model.Category;
import server.model.Transaction;
import server.service.AccountService;
import server.service.CategoryService;
import server.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private AccountService accountService;
    private CategoryService categoryService;
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService();
        categoryService = new CategoryService();
        transactionService = new TransactionService();
    }

    @AfterEach
    public void tearDown() {
        // Clean up resources if necessary
    }

    // Account tests
    @Test
    public void testAddAccount() {
        Account account = new Account();
        account.setName("Savings");
        account.setGroup("Bank");
        account.setBalance(1000.0);

        accountService.addAccount(account);

        List<Account> accounts = accountService.getAllAccounts();
        assertTrue(accounts.size() > 0);
        Account addedAccount = accounts.get(accounts.size() - 1);
        assertEquals("Savings", addedAccount.getName());
        assertEquals(1000.0, addedAccount.getBalance());
    }

    @Test
    public void testEditAccount() {
        Account account = new Account();
        account.setName("Savings");
        account.setGroup("Bank");
        account.setBalance(1000.0);
        accountService.addAccount(account);

        List<Account> accounts = accountService.getAllAccounts();
        Account addedAccount = accounts.get(accounts.size() - 1);

        addedAccount.setName("Updated Savings");
        addedAccount.setBalance(2000.0);
        accountService.updateAccount(addedAccount);

        Account updatedAccount = accountService.getAccount(addedAccount.getId());
        assertEquals("Updated Savings", updatedAccount.getName());
        assertEquals(2000.0, updatedAccount.getBalance());
    }

    @Test
    public void testRemoveAccount() {
        Account account = new Account();
        account.setName("Savings");
        account.setGroup("Bank");
        account.setBalance(1000.0);
        accountService.addAccount(account);

        List<Account> accounts = accountService.getAllAccounts();
        Account addedAccount = accounts.get(accounts.size() - 1);

        accountService.removeAccount(addedAccount.getId());
        assertNull(accountService.getAccount(addedAccount.getId()));
    }

    // Category tests
    @Test
    public void testAddCategory() {
        Category category = new Category();
        category.setName("Salary");
        category.setType("income");
        category.setBudget(5000.0);

        categoryService.addCategory(category);

        List<Category> categories = categoryService.getAllCategories();
        assertTrue(categories.size() > 0);
        Category addedCategory = categories.get(categories.size() - 1);
        assertEquals("Salary", addedCategory.getName());
        assertEquals(5000.0, addedCategory.getBudget());
    }

    @Test
    public void testEditCategory() {
        Category category = new Category();
        category.setName("Salary");
        category.setType("income");
        category.setBudget(5000.0);
        categoryService.addCategory(category);

        List<Category> categories = categoryService.getAllCategories();
        Category addedCategory = categories.get(categories.size() - 1);

        addedCategory.setName("Updated Salary");
        addedCategory.setBudget(6000.0);
        categoryService.updateCategory(addedCategory);

        Category updatedCategory = categoryService.getCategory(addedCategory.getId());
        assertEquals("Updated Salary", updatedCategory.getName());
        assertEquals(6000.0, updatedCategory.getBudget());
    }

    @Test
    public void testRemoveCategory() {
        Category category = new Category();
        category.setName("Salary");
        category.setType("income");
        category.setBudget(5000.0);
        categoryService.addCategory(category);

        List<Category> categories = categoryService.getAllCategories();
        Category addedCategory = categories.get(categories.size() - 1);

        categoryService.removeCategory(addedCategory.getId());
        assertNull(categoryService.getCategory(addedCategory.getId()));
    }

    // Transaction tests
    @Test
    public void testAddTransaction() {
        Account sourceAccount = new Account();
        sourceAccount.setName("Checking");
        sourceAccount.setGroup("Bank");
        sourceAccount.setBalance(5000.0);
        accountService.addAccount(sourceAccount);

        Category category = new Category();
        category.setName("Food");
        category.setType("expense");
        category.setBudget(500.0);
        categoryService.addCategory(category);

        List<Account> allAccounts = accountService.getAllAccounts();
        int sourceAccountId = allAccounts.get(allAccounts.size() - 1).getId();

        List<Category> allCategories = categoryService.getAllCategories();
        int categoryId = allCategories.get(allCategories.size() - 1).getId();

        Transaction transaction = new Transaction();
        transaction.setDateTime(LocalDateTime.now());
        transaction.setAmount(100.0);
        transaction.setSourceAccount(sourceAccountId);
        transaction.setCategory(categoryId);
        transaction.setNote("Grocery shopping");
        transaction.setType("Expense");

        transactionService.addTransaction(transaction);

        List<Transaction> transactions = transactionService.getTransactionsForCurrentMonth();
        assertTrue(transactions.size() > 0);
        Transaction addedTransaction = transactions.get(transactions.size() - 1);
        assertEquals(100.0, addedTransaction.getAmount());
        assertEquals("expense", addedTransaction.getType());
        assertEquals(sourceAccountId, addedTransaction.getSourceAccount());
        assertEquals(categoryId, addedTransaction.getCategory());
        assertEquals("Grocery shopping", addedTransaction.getNote());
    }

    @Test
    public void testEditTransaction() {
        // Add transaction first
        testAddTransaction();

        List<Transaction> transactions = transactionService.getTransactionsForCurrentMonth();
        Transaction addedTransaction = transactions.get(transactions.size() - 1);

        addedTransaction.setAmount(150.0);
        addedTransaction.setNote("Updated grocery shopping");
        transactionService.updateTransaction(addedTransaction);

        Transaction updatedTransaction = transactionService.getTransaction(addedTransaction.getId());
        assertEquals(150.0, updatedTransaction.getAmount());
        assertEquals("Updated grocery shopping", updatedTransaction.getNote());
    }

    @Test
    public void testRemoveTransaction() {
        // Add transaction first
        testAddTransaction();

        List<Transaction> transactions = transactionService.getTransactionsForCurrentMonth();
        Transaction addedTransaction = transactions.get(transactions.size() - 1);

        transactionService.removeTransaction(addedTransaction.getId());
        assertNull(transactionService.getTransaction(addedTransaction.getId()));
    }
}
