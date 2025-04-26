import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String mobileNo = request.getParameter("mobile_no");
        String password = request.getParameter("password");

        String meterNo = generateNextMeterNo();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO customer(name, email, meter_no, address, mobile_no, password, status) VALUES (?, ?, ?, ?, ?, ?, 'pending')"
            );
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, meterNo);
            stmt.setString(4, address);
            stmt.setString(5, mobileNo);
            stmt.setString(6, password);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                response.sendRedirect("login.html?message=Registered successfully. Awaiting admin approval.&status=success");
            } else {
                response.sendRedirect("register.html?message=Registration failed&status=error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.html?message=Error: email or mobile may already exist&status=error");
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    private String generateNextMeterNo() {
        int base = 10000;
        int next = base;

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT meter_no FROM customer ORDER BY id DESC LIMIT 1");

            if (rs.next()) {
                String lastMeter = rs.getString("meter_no");
                try {
                    next = Integer.parseInt(lastMeter) + 1;
                } catch (NumberFormatException e) {
                    next = base;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(next);
    }
}
