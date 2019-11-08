import java.sql.Connection;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Currency;
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
    public List<Customer> link(){
        List<Customer> customers=new ArrayList<Customer>();
        try{
            String sql="SELECT * FROM user";
            loaduser(sql,customers);
            sql="SELECT * FROM account";
            loadaccount(sql,customers);
            sql="SELECT * FROM loan";
            loadloan(sql,customers);
            sql="SELECT * FROM transcation";
            loadtransaction(sql,customers);
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

    private void loadloan(String sql, List<Customer> customers) {
    }
    private void loadtransaction(String sql, List<Customer> customers) {
    }

    private void loadaccount(String sql, List<Customer> customers) throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String UserID=rs.getString("UserID");
            String accountID=rs.getString("accountID");
            String accountType=rs.getString("accountType");
            String currency=rs.getString("currency");
            Double amount=rs.getDouble("amount");
            for(Customer customer: customers){
                //Currency currency1=Currency.getInstance();
                //Balance balance=new Balance(currency1);
                //balance.setAmount(amount);
                Account account=new Account();
                //account.
                if(customer.getUserID().equals(UserID)){
                    customer.getAccounts().add(account);
                }
            }
        }
        rs.close();
        stmt.close();
        conn.close();
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
            String sql="insert into user";
            recorduser(sql,bank);
            sql="insert into account";
            recordaccount(sql,bank);
            sql="insert into loan";
            recordloan(sql,bank);
            sql="insert into transaction";
            recordtransaction(sql,bank);
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

    private void recorduser(String sql, Bank bank) throws Exception {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ps = conn.prepareStatement(sql);
        List<Customer> customers=bank.getCustomers();
        for(Customer customer:customers) {
            ps.setString(1, customer.getUserID());
            ps.executeUpdate();
        }
    }
    private void recordaccount(String sql, Bank bank) throws Exception{
    }
    private void recordloan(String sql, Bank bank) throws Exception{
    }
    private void recordtransaction(String sql, Bank bank) throws Exception{
    }
}
