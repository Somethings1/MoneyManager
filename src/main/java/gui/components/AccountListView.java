package gui.components;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import server.model.Account;

public class AccountListView extends ScrollPane {
    private List<Account> accounts;
    private VBox contentPane;

    public AccountListView(List<Account> accounts) {
        this.accounts = accounts;
        contentPane = new VBox(10); // Spacing of 10 between elements
        contentPane.setPadding(new Insets(10)); // Add padding
        this.setContent(contentPane);
        groupAndDisplayAccounts();
    }

    private void groupAndDisplayAccounts() {
        // Group accounts by the group name using Collectors.groupingBy
        Map<String, List<Account>> groupedAccounts = accounts.stream()
                .collect(Collectors.groupingBy(Account::getGroup));

        // Loop through each group and display the group name with total balance
        for (Map.Entry<String, List<Account>> entry : groupedAccounts.entrySet()) {
            String groupName = entry.getKey();
            List<Account> groupAccounts = entry.getValue();

            // Calculate total balance for the group
            double totalBalance = groupAccounts.stream()
                    .mapToDouble(Account::getBalance)
                    .sum();

            // Display group name and total balance
            Label groupLabel = new Label(groupName + " (Total balance: " + String.format("%.2f", totalBalance) + ")");
            contentPane.getChildren().add(groupLabel);

            // Display each account in the group
            for (Account account : groupAccounts) {
                Label accountLabel = new Label(account.getName() + " - Balance: " + String.format("%.2f", account.getBalance()));
                contentPane.getChildren().add(accountLabel);
            }
        }
    }
}

