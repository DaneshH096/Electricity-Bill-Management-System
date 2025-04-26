import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ApproveCustomerServlet")
public class ApproveCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE customer SET status='approved' WHERE id=?");
            ps.setInt(1, id);
            int updated = ps.executeUpdate();

            if (updated > 0) {
                response.sendRedirect("viewCustomers.html?message=Customer approved&status=success");
            } else {
                response.sendRedirect("viewCustomers.html?message=Approval failed&status=error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("viewCustomers.html?message=Error occurred&status=error");
        }
    }
}
