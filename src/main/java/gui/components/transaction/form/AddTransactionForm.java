package gui.components.transaction.form;

import org.tbee.javafx.scene.layout.MigPane;

import gui.components.transaction.list.TransactionListView;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import server.service.TransactionService;

public class AddTransactionForm extends TransactionForm {
	
	TransactionListView view;
	
	public AddTransactionForm () {
		super();
	}
	
	public void setListView (TransactionListView view) {
		this.view = view;
	}
	
	@Override
	protected MigPane createFunctionalButtons () {
    	MigPane pane = new MigPane("", "[60][]", "");
    	Button cancelButton = new Button("Cancel");
        Button saveButton = new Button("Save");

        cancelButton.setOnAction(this::handleCancel);
        saveButton.setOnAction(this::handleSave);
        
        pane.add(cancelButton);
        pane.add(saveButton);
        
    	return pane;
    }
    
	private void handleSave (ActionEvent event) {
		(new TransactionService()).addTransaction(transaction);
		view.add(transaction);
		this.handleCancel(null);
	}
}
