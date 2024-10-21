package server.model;

public class Account {
	private int id;
	private String name;
	private String group;
	private double balance;
	private double goal;

	// Constructors
	public Account() {
	}

	public Account(int id, String name, String group, double balance) {
		this.id = id;
		this.name = name;
		this.group = group;
		this.balance = balance;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
