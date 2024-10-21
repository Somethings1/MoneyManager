package gui.components.transaction.form;

import java.time.LocalDateTime;
import java.awt.im.InputContext;

import org.tbee.javafx.scene.layout.MigPane;

import gui.components.transaction.list.TransactionListItem;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.model.Account;
import server.model.Category;
import server.model.Transaction;
import server.service.AccountService;
import server.service.CategoryService;

public abstract class TransactionForm extends MigPane {
	protected TransactionListItem item;
	
    protected Transaction transaction;
    protected Label activeLabel;
    protected DatePicker datePicker;
    protected TextField amountField;
    protected ComboBox<Account> sourceComboBox;
    protected ComboBox<Account> destinationComboBox;
    protected ComboBox<Category> incomeCategoryComboBox;
    protected ComboBox<Category> expenseCategoryComboBox;
    protected TextField noteField;
    protected MigPane formPane;

    public TransactionForm() {
        this(createDefaultTransaction());
    }

    public TransactionForm(Transaction transaction) {
        this.transaction = transaction.prototype();
        createElements();
        loadForm();
        InputContext.getInstance();
    }

    private static Transaction createDefaultTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDateTime(LocalDateTime.now());
        transaction.setType("Income");
        return transaction;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void createElements() {
        datePicker = new DatePicker(transaction.getDateTime().toLocalDate());
        amountField = new TextField(String.valueOf(transaction.getAmount()));
        sourceComboBox = new ComboBox(FXCollections.observableArrayList((new AccountService()).getAllAccounts()));
        sourceComboBox.setValue(new AccountService().getAccount(transaction.getSourceAccount()));
        destinationComboBox = new ComboBox(FXCollections.observableArrayList((new AccountService()).getAllAccounts()));

        incomeCategoryComboBox = new ComboBox(FXCollections.observableArrayList((new CategoryService()).getAllIncomeCategories()));
        expenseCategoryComboBox = new ComboBox(FXCollections.observableArrayList((new CategoryService()).getAllExpenseCategories()));

        if (!transaction.getType().equals("Transfer")) {
            expenseCategoryComboBox.setValue(transaction.getType().equals("Expense")
                    ? new CategoryService().getCategory(transaction.getCategory())
                    : new Category());
            incomeCategoryComboBox.setValue(transaction.getType().equals("Income")
                    ? new CategoryService().getCategory(transaction.getCategory())
                    : new Category());
        } else {
            destinationComboBox.setValue(new AccountService().getAccount(transaction.getDestinationAccount()));
        }

        noteField = new TextField(transaction.getNote());
        formPane = new MigPane();
        add(createTransactionTypeChoice(), "wrap");
        add(formPane, "wrap");
        add(createFunctionalButtons());
        // Attach listeners to update the transaction on user input
        attachListeners();
    }

    private void attachListeners() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            transaction.setDateTime(LocalDateTime.of(newValue, transaction.getDateTime().toLocalTime()));
        });

        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                transaction.setAmount(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                transaction.setAmount(0);
            }
        });

        sourceComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                transaction.setSourceAccount(newValue.getId()); // Assuming Account has a method getId()
            }
        });

        destinationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                transaction.setDestinationAccount(newValue.getId());
            }
        });

        incomeCategoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                transaction.setCategory(newValue.getId()); // Assuming Category has a method getId()
            }
        });

        expenseCategoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                transaction.setCategory(newValue.getId());
            }
        });

        noteField.textProperty().addListener((observable, oldValue, newValue) -> {
            transaction.setNote(newValue);
        });
    }

    private void loadForm() {
        formPane.getChildren().clear();
        formPane.add(createDateField(), "wrap");
        formPane.add(createAmountField(), "wrap");
        formPane.add(createSourceAccountField(), "wrap");
        
        if (activeLabel.getText().equals("Transfer")) {
            formPane.add(createDestinationAccountField(), "wrap");
        } else {
            formPane.add(createCategoryField(), "wrap");
        }
        
        formPane.add(createNoteField(), "wrap");
    }

    private MigPane createTransactionTypeChoice() {
        MigPane pane = new MigPane(); // Vertical spacing of 10
        Label label1 = createLabel("Income");
        Label label2 = createLabel("Expense");
        Label label3 = createLabel("Transfer");
        pane.add(label1);
        pane.add(label2);
        pane.add(label3);
        return pane;
    }

    private MigPane createDateField() {
        MigPane pane = new MigPane("", "[60][]", "");
        pane.add(new Label("Date"));
        pane.add(datePicker);
        return pane;
    }

    private MigPane createAmountField() {
        MigPane pane = new MigPane("", "[60][]", "");
        pane.add(new Label("Amount"));
        pane.add(amountField);
        return pane;
    }

    private MigPane createSourceAccountField() {
        MigPane pane = new MigPane("", "[60][]", "");
        pane.add(new Label("Account"));
        pane.add(sourceComboBox);
        return pane;
    }

    private MigPane createDestinationAccountField() {
        MigPane pane = new MigPane("", "[60][]", "");
        pane.add(new Label("Destination"));
        pane.add(destinationComboBox);
        return pane;
    }

    private MigPane createCategoryField() {
        MigPane pane = new MigPane("", "[60][]", "");
        pane.add(new Label("Category"));
        if (activeLabel.getText().equals("Income")) {
            pane.add(incomeCategoryComboBox);
        } else {
            pane.add(expenseCategoryComboBox);
        }
        return pane;
    }

    private MigPane createNoteField() {
        MigPane pane = new MigPane("", "[60][]", "");
        pane.add(new Label("Note"));
        pane.add(noteField);
        return pane;
    }
    
    protected abstract MigPane createFunctionalButtons ();

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1;");
        label.setTextFill(Color.BLACK);

        if (text.equals(transaction.getType())) {
            activateLabel(label);
        }

        // Set up the click event
        label.setOnMouseClicked(event -> {
            activateLabel(label);
            loadForm();
        });

        return label;
    }

    private void activateLabel(Label clickedLabel) {
        // Deactivate the currently active label
        if (activeLabel != null) {
            activeLabel.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1;"); // Reset style
            activeLabel.setTextFill(Color.BLACK); // Reset text color
        }

        // Activate the clicked label
        activeLabel = clickedLabel;
        if (activeLabel.getText().equals("Income")) {
            activeLabel.setStyle("-fx-padding: 10; -fx-border-color: blue; -fx-border-width: 2;"); // Change style for active
            activeLabel.setTextFill(Color.BLUE); // Change text color for active
        } else if (activeLabel.getText().equals("Expense")) {
            activeLabel.setStyle("-fx-padding: 10; -fx-border-color: red; -fx-border-width: 2;"); // Change style for active
            activeLabel.setTextFill(Color.RED); // Change text color for active
        } else {
            activeLabel.setStyle("-fx-padding: 10; -fx-border-color: brown; -fx-border-width: 2;"); // Change style for active
            activeLabel.setTextFill(Color.BROWN); // Change text color for active
        }
        
        transaction.setType(activeLabel.getText());
    }
    
    public void setItem (TransactionListItem item) {
    	this.item = item;
    }
    
    protected void handleCancel (ActionEvent event) {
    	Stage stage = (Stage) this.getScene().getWindow(); // Get the stage from the node's scene
        stage.close(); // Close the stage
    }
}
