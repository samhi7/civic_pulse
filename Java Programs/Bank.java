class BankAccount {
    String holderName;
    double balance;

    void deposit(double amount) {
        balance += amount;
    }

    void display() {
        System.out.println("Account Holder: " + holderName);
        System.out.println("Balance: " + balance);
    }
}

public class Bank {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount();

        acc.holderName = "Samhita";
        acc.deposit(5000);

        acc.display();
    }
} 
