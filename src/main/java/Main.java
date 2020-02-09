import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main {
    public static void main(String[] args) {
        String url =  "jdbc:mysql://localhost:3306/skillbox?serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
        String user = "root";
        String pass = "TestTest";
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            String query1 = "CREATE TEMPORARY TABLE Courses_MonthPurchase (" +
                    "name VARCHAR (100) NOT NULL," +
                    "month INT NOT NULL," +
                    "year INT NOT NULL," +
                    "monthPurchase INT NOT NULL); ";
            String query2 = "INSERT Courses_MonthPurchase (name, month, year, monthPurchase)" +
                    "(" +
                    "SELECT Courses.name, MONTH(Purchaselist.subscription_date), YEAR(Purchaselist.subscription_date), " +
                    "COUNT(Purchaselist.subscription_date)" +
                    " FROM Courses " +
                    "JOIN Purchaselist ON Purchaselist.course_name = Courses.name " +
                    "GROUP BY MONTH(Purchaselist.subscription_date), YEAR(Purchaselist.subscription_date), Courses.name" +
                    ");";
            String query3 = "SELECT name, AVG(monthPurchase) AS monthPurchase" +
                    " FROM Courses_MonthPurchase " +
                    "GROUP BY name;";
            statement.executeUpdate(query1);
            statement.executeUpdate(query2);
            ResultSet resultSet = statement.executeQuery(query3);

            while (resultSet.next()) {
                String courseName = resultSet.getString("name");
                Float monthPurchase = resultSet.getFloat("monthPurchase");
                System.out.printf("%-35s|%-5.2f%n", courseName, monthPurchase);
            }
            statement.executeUpdate("DROP TABLE Courses_MonthPurchase;");
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
