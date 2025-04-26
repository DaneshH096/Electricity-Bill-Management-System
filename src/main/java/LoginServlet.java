
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // Admin login
            PreparedStatement adminStmt = conn.prepareStatement("SELECT * FROM admin WHERE (username=? or email=?) AND password=?");
            adminStmt.setString(1, username);
            adminStmt.setString(2, username);
            adminStmt.setString(3, password);
            ResultSet adminRs = adminStmt.executeQuery();

            if (adminRs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                out.println("<script>alert('Admin Login Successfull'); location='adminDashboard.html';</script>");
                return;
            }

            // Customer login (only approved)
            PreparedStatement custStmt = conn.prepareStatement(
                    "SELECT * FROM customer WHERE (email=? or mobile_no=?) AND password=? AND status='approved'");
            custStmt.setString(1, username);
            custStmt.setString(2, username);
            custStmt.setString(3, password);
            ResultSet custRs = custStmt.executeQuery();

            if (custRs.next()) {
                HttpSession session = request.getSession();

                session.setAttribute("meter_no", custRs.getString("meter_no"));
                out.println("<script>alert('Customer Login Successfull'); location='customerDashboard.html';</script>");
            } else {
            	out.println("<script>alert('Invalid username or password'); location='login.html';</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html?message=Internal server error&status=error");
        }
    }
    
}
