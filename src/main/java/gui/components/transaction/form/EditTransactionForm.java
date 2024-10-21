package gui.components.transaction.form;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import server.model.Transaction;
import server.service.TransactionService;

public class EditTransactionForm extends TransactionForm {
	public EditTransactionForm (Transaction transaction) {
		super(transaction);
	}
	
	@Override
	protected MigPane createFunctionalButtons() {
	    MigPane pane = new MigPane("", "[60][]", "");
	    
	    // Create the buttons
	    Button cancelButton = new Button("Cancel");
	    Button saveButton = new Button("Save");
	    Button removeButton = new Button("Remove");

	    // Set up action handlers
	    cancelButton.setOnAction(this::handleCancel);
	    saveButton.setOnAction(this::handleSave);
	    removeButton.setOnAction(this::handleRemove); // Added the handler for remove

	    // Add buttons to the pane
	    pane.add(cancelButton);
	    pane.add(saveButton);
	    pane.add(removeButton); // Added the remove button to the pane

	    return pane;
	}

	private void handleRemove (ActionEvent event) {
		(new TransactionService()).removeTransaction(transaction.getId());
		item.terminate();
		handleCancel(event);
	}
	// Action handler for the save button
    private void handleSave(ActionEvent event) {
        item.updateView(transaction);
        (new TransactionService()).updateTransaction(this.transaction);
        handleCancel(event);
    }
	
}
