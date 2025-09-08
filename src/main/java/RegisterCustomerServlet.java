import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterCustomerServlet")
public class RegisterCustomerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to the HTML page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/addCustomer.html");
        dispatcher.forward(request, response);
    }

    // Generates the next customer ID manually (starts from 1 if table is empty)
    private int generateCustomerId(Connection con) throws SQLException {
        String query = "SELECT IFNULL(MAX(id), 0) + 1 AS next_id FROM customer";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int customerId = 1;

        if (rs.next()) {
            customerId = rs.getInt("next_id");
        }

        rs.close();
        stmt.close();

        return customerId;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile_no");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = DBConnection.getConnection()) {

            // Generate next meter number
            int nextMeter = 10000;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(CAST(meter_no AS UNSIGNED)) AS max_meter FROM customer");

            if (rs.next() && rs.getString("max_meter") != null) {
                nextMeter = rs.getInt("max_meter") + 1;
            }

            rs.close();
            stmt.close();

            // Insert new customer
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO customer (id, name, email, mobile_no, address, password, meter_no, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'pending')");

            ps.setInt(1, generateCustomerId(con));
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, mobile);
            ps.setString(5, address);
            ps.setString(6, password);
            ps.setString(7, String.valueOf(nextMeter));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<script>alert('Customer Added Successfully'); location='"+request.getContextPath()+"/login.html';</script>");
            } else {
                out.println("<script>alert('Failed to add customer'); location='"+request.getContextPath()+"/addCustomer.html';</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('Error occurred: " + e.getMessage() + "'); location='"+request.getContextPath()+"/addCustomer.html';</script>");
        }
    }
}
