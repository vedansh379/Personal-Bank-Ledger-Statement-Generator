import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

// ============================================================
// PERSONAL BANK LEDGER & STATEMENT GENERATOR
// ============================================================

public class leger {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("     PERSONAL BANK LEDGER & STATEMENT");
        System.out.println("==============================================");

        System.out.print("Enter account holder name: ");
        String name = scanner.nextLine().trim();

        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Enter again: ");
            name = scanner.nextLine().trim();
        }

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        while (!accountNumber.matches("\\d{8,16}")) {
            System.out.print("Enter a valid account number (8-16 digits): ");
            accountNumber = scanner.nextLine().trim();
        }

        BankAccount account = new BankAccount(name, accountNumber);

        System.out.println("\nAccount created successfully!");
        System.out.println("Welcome, " + name + "!");

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    showAccountDetails(account);
                    break;

                case 2:
                    depositMoney(account);
                    break;

                case 3:
                    withdrawMoney(account);
                    break;

                case 4:
                    transferMoney(account);
                    break;

                case 5:
                    displayBalance(account);
                    break;

                case 6:
                    account.printStatement();
                    break;

                case 7:
                    searchTransactions(account);
                    break;

                case 8:
                    System.out.println("\nThank you for using Personal Bank Ledger!");
                    System.out.println("Have a great day!");
                    running = false;
                    break;

                default:
                    System.out.println("\nInvalid choice! Please select 1-8.");
            }
        }

        scanner.close();
    }

    // ============================================================
    // MENU
    // ============================================================

    private static void displayMenu() {

        System.out.println("\n==============================================");
        System.out.println("                 MAIN MENU");
        System.out.println("==============================================");
        System.out.println("1. Account Details");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer Money");
        System.out.println("5. Check Balance");
        System.out.println("6. Generate Bank Statement");
        System.out.println("7. Search Transactions");
        System.out.println("8. Exit");
        System.out.println("==============================================");
    }

    // ============================================================
    // ACCOUNT DETAILS
    // ============================================================

    private static void showAccountDetails(BankAccount account) {

        System.out.println("\n--------------- ACCOUNT DETAILS ---------------");

        System.out.println("Account Holder : " + account.getAccountHolderName());
        System.out.println("Account Number : " + account.getMaskedAccountNumber());
        System.out.println("Balance        : ₹" + account.getBalance());

        System.out.println("-----------------------------------------------");
    }

    // ============================================================
    // DEPOSIT
    // ============================================================

    private static void depositMoney(BankAccount account) {

        System.out.println("\n--------------- DEPOSIT MONEY ----------------");

        BigDecimal amount = readAmount("Enter amount to deposit: ₹");

        if (account.deposit(amount)) {
            System.out.println("\nDeposit successful!");
            System.out.println("Amount deposited : ₹" + amount);
            System.out.println("New balance      : ₹" + account.getBalance());
        } else {
            System.out.println("Deposit failed.");
        }
    }

    // ============================================================
    // WITHDRAW
    // ============================================================

    private static void withdrawMoney(BankAccount account) {

        System.out.println("\n--------------- WITHDRAW MONEY ----------------");

        BigDecimal amount = readAmount("Enter amount to withdraw: ₹");

        if (account.withdraw(amount)) {

            System.out.println("\nWithdrawal successful!");
            System.out.println("Amount withdrawn : ₹" + amount);
            System.out.println("Remaining balance: ₹" + account.getBalance());

        } else {

            System.out.println("\nTransaction failed!");

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be greater than zero.");
            } else {
                System.out.println("Insufficient balance.");
            }
        }
    }

    // ============================================================
    // TRANSFER
    // ============================================================

    private static void transferMoney(BankAccount account) {

        System.out.println("\n--------------- TRANSFER MONEY ----------------");

        System.out.print("Enter recipient name: ");
        String recipient = scanner.nextLine().trim();

        while (recipient.isEmpty()) {
            System.out.print("Recipient name cannot be empty: ");
            recipient = scanner.nextLine().trim();
        }

        System.out.print("Enter recipient account number: ");
        String recipientAccount = scanner.nextLine().trim();

        if (!recipientAccount.matches("\\d{8,16}")) {
            System.out.println("Invalid recipient account number.");
            return;
        }

        BigDecimal amount = readAmount("Enter transfer amount: ₹");

        if (account.transfer(amount, recipient, recipientAccount)) {

            System.out.println("\nTransfer successful!");
            System.out.println("Recipient : " + recipient);
            System.out.println("Amount    : ₹" + amount);
            System.out.println("Balance   : ₹" + account.getBalance());

        } else {

            System.out.println("\nTransfer failed!");

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be greater than zero.");
            } else {
                System.out.println("Insufficient balance.");
            }
        }
    }

    // ============================================================
    // BALANCE
    // ============================================================

    private static void displayBalance(BankAccount account) {

        System.out.println("\n--------------- CURRENT BALANCE ----------------");

        System.out.println("Available Balance: ₹" + account.getBalance());

        System.out.println("-----------------------------------------------");
    }

    // ============================================================
    // SEARCH TRANSACTIONS
    // ============================================================

    private static void searchTransactions(BankAccount account) {

        System.out.println("\n--------------- SEARCH TRANSACTIONS ------------");
        System.out.println("1. Search by transaction type");
        System.out.println("2. Search by description");
        System.out.println("3. Show all transactions");

        int choice = readInt("Enter choice: ");

        switch (choice) {

            case 1:

                System.out.println("\nTransaction Types:");
                System.out.println("1. DEPOSIT");
                System.out.println("2. WITHDRAWAL");
                System.out.println("3. TRANSFER");

                int typeChoice = readInt("Select type: ");

                TransactionType type = null;

                if (typeChoice == 1) {
                    type = TransactionType.DEPOSIT;
                } else if (typeChoice == 2) {
                    type = TransactionType.WITHDRAWAL;
                } else if (typeChoice == 3) {
                    type = TransactionType.TRANSFER;
                } else {
                    System.out.println("Invalid transaction type.");
                    return;
                }

                account.searchByType(type);
                break;

            case 2:

                System.out.print("Enter keyword: ");
                String keyword = scanner.nextLine().trim();

                account.searchByDescription(keyword);
                break;

            case 3:

                account.printStatement();
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // ============================================================
    // INPUT METHODS
    // ============================================================

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static BigDecimal readAmount(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                BigDecimal amount = new BigDecimal(input);

                amount = amount.setScale(2, RoundingMode.HALF_UP);

                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    return amount;
                }

                System.out.println("Amount must be greater than zero.");

            } catch (NumberFormatException e) {

                System.out.println("Please enter a valid amount.");
            }
        }
    }
}


// ============================================================
// BANK ACCOUNT CLASS
// ============================================================

class BankAccount {

    private final String accountHolderName;
    private final String accountNumber;

    private BigDecimal balance;

    private final ArrayList<Transaction> transactions;

    public BankAccount(String accountHolderName, String accountNumber) {

        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO.setScale(2);
        this.transactions = new ArrayList<>();
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    // ============================================================
    // MASK ACCOUNT NUMBER
    // ============================================================

    public String getMaskedAccountNumber() {

        if (accountNumber.length() <= 4) {
            return accountNumber;
        }

        String lastFour =
                accountNumber.substring(accountNumber.length() - 4);

        return "XXXX-XXXX-" + lastFour;
    }

    // ============================================================
    // DEPOSIT
    // ============================================================

    public boolean deposit(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        balance = balance.add(amount);

        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                amount,
                balance,
                "Cash deposit"
        );

        transactions.add(transaction);

        return true;
    }

    // ============================================================
    // WITHDRAW
    // ============================================================

    public boolean withdraw(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (amount.compareTo(balance) > 0) {
            return false;
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        balance = balance.subtract(amount);

        Transaction transaction = new Transaction(
                TransactionType.WITHDRAWAL,
                amount,
                balance,
                "Cash withdrawal"
        );

        transactions.add(transaction);

        return true;
    }

    // ============================================================
    // TRANSFER
    // ============================================================

    public boolean transfer(
            BigDecimal amount,
            String recipient,
            String recipientAccount
    ) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (amount.compareTo(balance) > 0) {
            return false;
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        balance = balance.subtract(amount);

        String description =
                "Transfer to " +
                recipient +
                " (" +
                maskNumber(recipientAccount) +
                ")";

        Transaction transaction = new Transaction(
                TransactionType.TRANSFER,
                amount,
                balance,
                description
        );

        transactions.add(transaction);

        return true;
    }

    // ============================================================
    // PRINT BANK STATEMENT
    // ============================================================

    public void printStatement() {

        System.out.println("\n==============================================================");
        System.out.println("                     BANK STATEMENT");
        System.out.println("==============================================================");

        System.out.println("Account Holder : " + accountHolderName);
        System.out.println("Account Number : " + getMaskedAccountNumber());

        System.out.println("--------------------------------------------------------------");

        if (transactions.isEmpty()) {

            System.out.println("No transactions available.");

        } else {

            System.out.printf(
                    "%-5s %-20s %-15s %-15s%n",
                    "No.",
                    "Date & Time",
                    "Type",
                    "Amount"
            );

            System.out.println("--------------------------------------------------------------");

            int number = 1;

            for (Transaction transaction : transactions) {

                System.out.printf(
                        "%-5d %-20s %-15s ₹%-14s%n",
                        number,
                        transaction.getFormattedDateTime(),
                        transaction.getType(),
                        transaction.getAmount()
                );

                System.out.println(
                        "      " + transaction.getDescription()
                );

                System.out.printf(
                        "      Balance after transaction: ₹%s%n",
                        transaction.getBalanceAfter()
                );

                System.out.println();

                number++;
            }
        }

        System.out.println("--------------------------------------------------------------");

        System.out.println(
                "CURRENT BALANCE : ₹" + balance
        );

        System.out.println("==============================================================");
    }

    // ============================================================
    // SEARCH BY TYPE
    // ============================================================

    public void searchByType(TransactionType type) {

        boolean found = false;

        System.out.println("\nSearch Results: " + type);
        System.out.println("-----------------------------------------------");

        for (Transaction transaction : transactions) {

            if (transaction.getType() == type) {

                found = true;

                System.out.println(
                        transaction.getFormattedDateTime()
                                + " | ₹"
                                + transaction.getAmount()
                                + " | "
                                + transaction.getDescription()
                );
            }
        }

        if (!found) {
            System.out.println("No transactions found.");
        }

        System.out.println("-----------------------------------------------");
    }

    // ============================================================
    // SEARCH BY DESCRIPTION
    // ============================================================

    public void searchByDescription(String keyword) {

        boolean found = false;

        System.out.println("\nSearch Results:");
        System.out.println("-----------------------------------------------");

        for (Transaction transaction : transactions) {

            if (transaction.getDescription()
                    .toLowerCase()
                    .contains(keyword.toLowerCase())) {

                found = true;

                System.out.println(
                        transaction.getFormattedDateTime()
                                + " | "
                                + transaction.getType()
                                + " | ₹"
                                + transaction.getAmount()
                                + " | "
                                + transaction.getDescription()
                );
            }
        }

        if (!found) {
            System.out.println("No matching transactions found.");
        }

        System.out.println("-----------------------------------------------");
    }

    // ============================================================
    // MASK ACCOUNT NUMBER
    // ============================================================

    private String maskNumber(String number) {

        if (number.length() <= 4) {
            return number;
        }

        return "XXXX" +
                number.substring(number.length() - 4);
    }
}


// ============================================================
// TRANSACTION CLASS
// ============================================================

class Transaction {

    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final String description;
    private final LocalDateTime dateTime;

    public Transaction(
            TransactionType type,
            BigDecimal amount,
            BigDecimal balanceAfter,
            String description
    ) {

        this.type = type;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.balanceAfter =
                balanceAfter.setScale(2, RoundingMode.HALF_UP);

        this.description = description;
        this.dateTime = LocalDateTime.now();
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getFormattedDateTime() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        return dateTime.format(formatter);
    }
}


// ============================================================
// TRANSACTION TYPE ENUM
// ============================================================

enum TransactionType {

    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}