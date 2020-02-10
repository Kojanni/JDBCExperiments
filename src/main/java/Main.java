import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main {
    public static void main(String[] args) {
        String url =  "jdbc:mysql://localhost:3306/skillbox?serverTimezone=UTC&useSSL=true";
        String user = "root";
        String pass = "TestTest";
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            String query = "SELECT name, " +
                    "(COUNT(Purchaselist.subscription_date)/COUNT(DISTINCT YEAR(Purchaselist.subscription_date) * 100 + MONTH(Purchaselist.subscription_date))) AS monthPurchase " +
                    "FROM Courses " +
                    "JOIN Purchaselist ON Purchaselist.course_name = Courses.name " +
                    "GROUP BY Courses.name;";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String courseName = resultSet.getString("name");
                Float monthPurchase = resultSet.getFloat("monthPurchase");
                System.out.printf("%-35s|%-5.2f%n", courseName, monthPurchase);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
