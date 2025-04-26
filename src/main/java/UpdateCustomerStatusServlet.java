import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/UpdateCustomerStatusServlet")
public class UpdateCustomerStatusServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE customer SET status=? WHERE id=?")) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

            response.sendRedirect("viewCustomers.html?message=Customer " + status + "&status=success");

        } catch (Exception e) {
            response.sendRedirect("viewCustomers.html?message=Error updating status&status=error");
        }
    }
}
