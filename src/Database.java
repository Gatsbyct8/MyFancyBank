import java.sql.Connection;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Currency;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class Database {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/MyFancyBank";
    static final String USER = "root";
    static final String PASS = "root";
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement ps=null;
    public List<Customer> link(Bank bank){
        List<Customer> customers=new ArrayList<Customer>();
        try{
            String sql="SELECT * FROM user";
            loaduser(sql,customers);
            sql="SELECT * FROM account";
            loadaccount(sql,customers);
            sql="SELECT * FROM loan";
            loadloan(sql,customers);
            sql="SELECT * FROM secureaccount";
            loadsecureaccount(sql,customers,bank);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return customers;
    }

    private void loadsecureaccount(String sql, List<Customer> customers,Bank bank) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String UserId=rs.getString("UserId");
            String id=rs.getString("id");
            Double buyprice=rs.getDouble("buyprice");
            int amount =rs.getInt("amount");
            for (Stock stock:bank.getStocks()){
                if(stock.id.equals(id)){
                    for (Customer customer:customers){
                        if(customer.getUserID().equals(UserId)){
                            customer.getSelfStocks().add(new SelfStock(stock,buyprice,amount));
                        }
                    }
                }
            }
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public TotalStock linkstock(){
        TotalStock totalStock=new TotalStock();
        try{
            String sql="SELECT * FROM stock";
            loadstock(sql,totalStock);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return totalStock;
    }
    private void loadstock(String sql, TotalStock totalStock) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String id=rs.getString("id");
            String name=rs.getString("name");
            int number =rs.getInt("number");
            Double currentprice =rs.getDouble("currentprice");
            Stock stock=new Stock(id,name,number,currentprice);
            totalStock.add(stock);
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public List<Manager> linkManager(){
        List<Manager> managers=new ArrayList<Manager>();
        try{
            String sql="SELECT * FROM user";
            loadmanager(sql,managers);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return managers;
    }

    private void loadmanager(String sql, List<Manager> managers) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String ManagerID=rs.getString("ManagerID");
            String passcode=rs.getString("passcode");
            String firstName=rs.getString("firstName");
            String lastName=rs.getString("lastName");
            String phoneNumber=rs.getString("phoneNumber");
            String street=rs.getString("street");
            String city=rs.getString("city");
            String state=rs.getString("state");
            String zip=rs.getString("zip");
            Address address=new Address(street,city,state,zip);
            Name name=new Name(lastName,firstName);
            Manager manager=new Manager(name,address,phoneNumber,ManagerID,passcode);
            managers.add(manager);
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    private void loadloan(String sql, List<Customer> customers) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String UserID=rs.getString("UserID");
            Double amount=rs.getDouble("amount");
            String id=rs.getString("id");
            String CollateralType=rs.getString("CollateralType");
            Date date=rs.getDate("date");
            for(Customer customer: customers){
                Loan loan=createLoan(date,amount,CollateralType,id);
                if(customer.getUserID().equals(UserID)){
                    customer.getLoans().add(loan);
                }
            }
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    private Loan createLoan(Date date, Double amount, String collateralType, String id) {
        Loan loan=null;
        if(collateralType=="ESTATE"){
            loan=new Loan(date,amount,CollateralType.ESTATE,id);
        }else if(collateralType=="VEHICLE"){
            loan=new Loan(date,amount,CollateralType.VEHICLE,id);
        }else{
            loan=new Loan(date,amount,CollateralType.DEVICE,id);
        }
        return loan;
    }

    private void loadtransaction(int s,int num, Balance balance) throws Exception{
        String sql="SELECT * FROM transaction LIMIT "+s+", "+num;
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String UserID=rs.getString("UserID");
            String accountID=rs.getString("accountID");
            Date date=rs.getDate("date");
            Double amount=rs.getDouble("amount");
            String sourceNtargetID =rs.getString("sourceNtargetID");
            String reason=rs.getString("reason");
            Transaction transaction=new Transaction(date,amount,sourceNtargetID);
            transaction.setReason(reason);
            balance.addNewTransaction(transaction);
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    private Currency createCurrency(String currency){
        if(currency.equals("EUR")){
            return Currency.getInstance(Locale.FRANCE);
        }else if(currency.equals("USD")){
            return Currency.getInstance(Locale.US);
        }else if(currency.equals("CNY")){
            return Currency.getInstance(Locale.CHINA);
        }else{
            return Currency.getInstance(Locale.JAPAN);
        }
    }
    private void loadaccount(String sql, List<Customer> customers) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.beforeFirst();
        String UserID;
        String accountID;
        String accountType;
        String currency;
        Double amount;
        int transactionNum;
        while(rs.next()) {
            UserID = rs.getString("UserID");
            accountID = rs.getString("accountID");
            accountType = rs.getString("accountType");
            currency = rs.getString("currency");
            amount = rs.getDouble("amount");
            transactionNum = rs.getInt("transactionNum");
            for (Customer customer : customers) {
                    if (customer.getUserID().equals(UserID)) {
                        boolean flag=true;
                        for(Account A:customer.getAccounts()) {
                            if (A.accountID.equals(accountID)) {
                                flag=false;
                                break;
                            }
                        }
                        if(flag){
                            Account account = createNewAccount(accountID, accountType);
                            customer.getAccounts().add(account);
                        }
                    }
            }
        }
        rs.beforeFirst();
        int s=0;
        while(rs.next()){
            UserID = rs.getString("UserID");
            accountID = rs.getString("accountID");
            accountType = rs.getString("accountType");
            currency = rs.getString("currency");
            amount = rs.getDouble("amount");
            transactionNum = rs.getInt("transactionNum");
            for(Customer customer: customers) {
                if(customer.getUserID().equals(UserID)) {
                    for (Account account : customer.getAccounts()) {
                        MoneyAccount moneyAccount = (MoneyAccount) account;
                        if (moneyAccount.accountID.equals(accountID)) {
                            Balance balance = new Balance(createCurrency(currency));
                            balance.setAmount(amount);
                            loadtransaction(s,transactionNum,balance);
                            s+=transactionNum;
                            moneyAccount.balance.add(balance);
                        }
                    }
                }
            }
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    private Account createNewAccount(String accountID, String accountType) {
        Account a=null;
        if(accountType=="checking"){
            a=new CheckingAccount();
            a.accountID=accountID;
        }else{
            a=new SavingAccount();
            a.accountID=accountID;
        }
        return a;
    }

    public void loaduser(String sql,List<Customer> customers) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String UserID=rs.getString("UserID");
            String passcode=rs.getString("passcode");
            String firstName=rs.getString("firstName");
            String lastName=rs.getString("lastName");
            String phoneNumber=rs.getString("phoneNumber");
            String street=rs.getString("street");
            String city=rs.getString("city");
            String state=rs.getString("state");
            String zip=rs.getString("zip");
            Address address=new Address(street,city,state,zip);
            Name name=new Name(lastName,firstName);
            Customer customer=new Customer(name,address,phoneNumber,UserID,passcode);
            customers.add(customer);
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    public void record(Bank bank){
        try{
            deletetable();
            String sql="insert into user values (?,?,?,?,?,?,?,?,?)";
            recorduser(sql,bank);
            sql="insert into account values (?,?,?,?,?,?)";
            recordaccount(sql,bank);
            sql="insert into loan values (?,?,?,?,?)";
            recordloan(sql,bank);
            sql="insert into transaction(UserID,accountID,date,amount,sourceNtargetID,reason) values (?,?,?,?,?,?)";
            recordtransaction(sql,bank);
            sql="insert into stock values (?,?,?,?)";
            recordstock(sql,bank);
            sql="insert into manager values (?,?,?,?,?,?,?,?,?)";
            recordmanager(sql,bank);
            sql="insert into secureaccount values (?,?,?,?)";
            recordsecureaccount(sql,bank);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(ps!=null) ps.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    private void recordsecureaccount(String sql, Bank bank) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Customer> customers=bank.getCustomers();
        for(Customer customer:customers) {
            for(SelfStock selfStock:customer.getSelfStocks()) {
                ps.setString(1, customer.getUserID());
                ps.setString(2, selfStock.getStock().getId());
                ps.setDouble(3, selfStock.getBuyPrice());
                ps.setInt(4,selfStock.getAmount() );
                ps.executeUpdate();
            }
        }
    }

    private void recordmanager(String sql, Bank bank) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Manager> managers=bank.getManagers();
        for(Manager manager:managers) {
            ps.setString(1, manager.getManagerID());
            ps.setString(2,manager.getPasscode());
            ps.setString(3,manager.name.getFirstName());
            ps.setString(4,manager.name.getLastName());
            ps.setString(5,manager.phoneNumber);
            ps.setString(6,manager.address.getStreet());
            ps.setString(7,manager.address.getCity());
            ps.setString(8,manager.address.getState());
            ps.setString(9,manager.address.getZip());
            ps.executeUpdate();
        }
    }

    private void recordstock(String sql, Bank bank) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Stock>stocks=bank.getStocks();
        for(Stock stock:stocks) {
            ps.setString(1, stock.id);
            ps.setString(2,stock.name);
            ps.setInt(3,stock.getNumber());
            ps.setDouble(4,stock.currentPrice);
            ps.executeUpdate();
        }
    }

    private void deletetable() throws Exception{
        String sql[]={"Truncate Table user","Truncate Table account","Truncate Table loan","Truncate Table transaction ","Truncate Table stock ","Truncate Table manager "};
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        for(String s:sql) {
            stmt.executeUpdate(s);
        }
        stmt.close();
        conn.close();
    }

    private void recorduser(String sql, Bank bank) throws Exception {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Customer> customers=bank.getCustomers();
        for(Customer customer:customers) {
            ps.setString(1, customer.getUserID());
            ps.setString(2,customer.getPasscode());
            ps.setString(3,customer.getName().getFirstName());
            ps.setString(4,customer.getName().getLastName());
            ps.setString(5,customer.phoneNumber);
            ps.setString(6,customer.address.getStreet());
            ps.setString(7,customer.address.getCity());
            ps.setString(8,customer.address.getState());
            ps.setString(9,customer.address.getZip());
            ps.executeUpdate();
        }
    }
    private void recordaccount(String sql, Bank bank) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Customer> customers=bank.getCustomers();
        for(Customer customer:customers) {
            for(Account account:customer.getAccounts()){
                MoneyAccount moneyAccount=(MoneyAccount)account;
                for(Balance balance:moneyAccount.balance){
                    ps.setString(1, customer.getUserID());
                    ps.setString(2,account.accountID);
                    if(account instanceof CheckingAccount){
                        ps.setString(3,"checking");
                    }else{
                        ps.setString(3,"saving");
                    }
                    if(balance.getCurrency().getCurrencyCode().equals("USD")){
                        ps.setString(4,"USD");
                    }else if(balance.getCurrency().getCurrencyCode().equals("CNY")){
                        ps.setString(4,"CNY");
                    }else if(balance.getCurrency().getCurrencyCode().equals("JPY")){
                        ps.setString(4,"JPY");
                    }else{
                        ps.setString(4,"EUR");
                    }
                    ps.setDouble(5,balance.getAmount());
                    ps.setInt(6,balance.getTransactions().size());
                    ps.executeUpdate();
                }
            }
        }
    }
    private void recordloan(String sql, Bank bank) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Customer> customers=bank.getCustomers();
        for(Customer customer:customers) {
            for(Loan loan:customer.getLoans()) {
                ps.setString(1, customer.getUserID());
                ps.setDouble(2, loan.getAmount());
                ps.setString(3,loan.getID());
                if(loan.getCollateral().equals(CollateralType.DEVICE)) {
                    ps.setString(4,"DEVICE");
                }else if(loan.getCollateral().equals(CollateralType.VEHICLE)){
                    ps.setString(4,"VEHICLE");
                }else{
                    ps.setString(4,"ESTATE");
                }
                ps.setDate(5,loan.getDate());
                ps.executeUpdate();
            }
        }
    }
    private void recordtransaction(String sql, Bank bank) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Customer> customers=bank.getCustomers();
        for(Customer customer:customers) {
            for(Account account:customer.getAccounts()){
                MoneyAccount moneyAccount=(MoneyAccount)account;
                for(Balance balance:moneyAccount.balance){
                    for(Transaction transaction:balance.getTransactions()) {
                        ps.setString(1, customer.getUserID());
                        ps.setString(2, account.accountID);
                        ps.setDate(3,transaction.getDate());
                        ps.setDouble(4,transaction.getAmount());
                        ps.setString(5,transaction.getSourceNtargetID());
                        ps.setString(6,transaction.getReason());
                        ps.executeUpdate();
                    }
                }
            }
        }
    }
}
