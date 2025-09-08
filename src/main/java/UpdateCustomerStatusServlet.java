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
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE customer SET status=? WHERE id=?")) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

         	out.println("<script>alert('Customer "+status+"'); location='"+request.getContextPath()+"/viewCustomers.html';</script>");

        } catch (Exception e) {
        	out.println("<script>alert('Error updating status'); location='"+request.getContextPath()+"/viewCustomers.html';</script>");
        }
    }
}
