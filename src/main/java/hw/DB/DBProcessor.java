package hw.DB;

import hw.Shop.Product;

import java.sql.*;
import java.util.ArrayList;

public class DBProcessor {
    private Connection con;

    public DBProcessor() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Store");
            PreparedStatement st = con.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS PRODUCTS(" +
                            "ID INTEGER PRIMARY KEY," +
                            "TITLE TEXT NOT NULL," +
                            "PRICE DOUBLE NOT NULL," +
                            "AMOUNT INTEGER NOT NULL," +
                            "CATEGORY TEXT NOT NULL)");

            int result = st.executeUpdate();
            st.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }

    }

    public void Create(Product product)
    {
        String crt = "INSERT INTO PRODUCTS (TITLE, PRICE, AMOUNT, CATEGORY) VALUES (?, ?, ?, ?)";
        try(PreparedStatement st = con.prepareStatement(crt)){
            st.setString(1, product.getTitle());
            st.setDouble(2, product.getPrice());
            st.setInt(3, product.getAmount());
            st.setString(4, product.getCategory());
            int result = st.executeUpdate();
            st.close();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Product> Read() {
        ArrayList<Product> res = new ArrayList<>();

        try (Statement st = con.createStatement()) {

            ResultSet set = st.executeQuery("SELECT * FROM PRODUCTS");

            while (set.next()) {
                res.add(new Product(set.getString("TITLE"), set.getDouble("PRICE"),
                        set.getInt("AMOUNT"), set.getString("CATEGORY")));
            }
            set.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return res;
    }

    public void Update(Product product)
    {
        try(
            PreparedStatement st = con.prepareStatement(
                    "UPDATE PRODUCTS SET TITLE = ?," +
                        "PRICE = ?," +
                        "AMOUNT = ?," +
                        "CATEGORY = ?" +
                        "WHERE ID = ?"))
        {
            st.setString(1, product.getTitle());
            st.setDouble(2, product.getPrice());
            st.setInt(3, product.getAmount());
            st.setString(4, product.getCategory());
            st.setInt(5, product.getId());
            st.executeUpdate();
            int result = st.executeUpdate();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    public void Delete(int id)
    {
        try(PreparedStatement st = con.prepareStatement("DELETE FROM PRODUCTS WHERE ID = ?"))
        {
            st.setInt(1, id);
            int result = st.executeUpdate();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    // Criteria = new String("PRICE <= 10")
    public ArrayList<Product> ListByCriteria(String Criteria) {
        ArrayList<Product> res = new ArrayList<>();

        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE " + Criteria + ";")) {
            ResultSet set = st.executeQuery();
            while (set.next()) {
                res.add(new Product(set.getString("TITLE"), set.getDouble("PRICE"),
                        set.getInt("AMOUNT"), set.getString("CATEGORY")));
            }
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }

        return res;
    }

    public void ShowAllData(){
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM PRODUCTS");
            while (res.next()) {
                System.out.println (res.getShort("ID")+" "+res.getString("TITLE")
                + " " + res.getDouble("PRICE") + " " + res.getInt("AMOUNT")
                + " " + res.getString("CATEGORY"));
            }
            res.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    public void Clear()
    {
        try{
            PreparedStatement st = con.prepareStatement("DELETE FROM PRODUCTS;");
            int result = st.executeUpdate();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    private static void main(String[] args){
        DBProcessor sqlTest = new DBProcessor();
        sqlTest.Create(new Product(10, "Milk", 14.88, "Cow Milk"));
        sqlTest.Create(new Product(10, "Cook", 156.88, "Cum Milk"));
        sqlTest.ShowAllData();
        sqlTest.Clear();
    }
}
