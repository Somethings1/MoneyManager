package gui.components.transaction.list;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import server.model.Transaction;
import gui.components.transaction.form.EditTransactionForm;
import gui.components.util.BalanceLabel;
import gui.components.util.Modal;

public class TransactionListItem extends MigPane {
	private Transaction transaction;
	private Label categoryLabel = new Label();
	private Label noteLabel = new Label();
	private Label accountLabel = new Label();
	private BalanceLabel amountLabel = new BalanceLabel(0);
	private TransactionListView view;
	
	public TransactionListItem (Transaction transaction) {
		super("", "[70]20[grow]20[]", "[][]");
		this.transaction = transaction;
		addComponents();
	}
	
	public void updateView (Transaction transaction) {
		this.transaction = transaction;
		updateCategoryLabel();
		updateNoteLabel();
		updateAccountLabel();
		updateAmountLabel();
	}
	
	private void addComponents () {
		add(categoryLabel, "span 1 2");
		add(noteLabel);
		add(amountLabel, "span 1 2");
		add(accountLabel, "newline");
		updateView(transaction);
		// Set TransactionForm as content

        // Set the double-click event
        setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
            	EditTransactionForm form = new EditTransactionForm(this.transaction);
            	form.setItem(this);
                Modal modal = new Modal(form, "Edit transaction");
                modal.show();
            }
        });
		setStyle("-fx-border-color: black; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 10px;");
	}
	
	private void updateCategoryLabel () {
		categoryLabel.setText(transaction.getCategoryName());
	}
	
	private void updateNoteLabel () {
		noteLabel.setText(transaction.getNote());
	}
	
	private void updateAmountLabel () {
		if (transaction.getType().equals("Transfer"))
			amountLabel.update(transaction.getAmount(), Color.GREY);
		else if (transaction.getType().equals("Expense"))
			amountLabel.update(-transaction.getAmount());
		else
			amountLabel.update(transaction.getAmount());
	}
	
	private void updateAccountLabel () {
		if (transaction.getType().equals("Transfer"))
			accountLabel.setText(transaction.getSourceAccountName() + "->" + transaction.getDestinationAccountName());
		else
			accountLabel.setText(transaction.getSourceAccountName());
	}
	
	public void setListView (TransactionListView view) {
		this.view = view;
	}
	
	public void terminate () {
		view.remove(transaction);
	}
}
