import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PayBillServlet")
public class PayBillServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String billIdStr = request.getParameter("bill_id");

        if (billIdStr == null || billIdStr.isEmpty()) {
            out.println("<script>alert('Invalid Bill ID'); location='customerDashboard.html';</script>");
            return;
        }

        int billId = Integer.parseInt(billIdStr);

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE bill SET status='Paid' WHERE id=?");
            ps.setInt(1, billId);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                out.println("<script>alert('Bill Paid Successfully'); location='customerDashboard.html';</script>");
            } else {
                out.println("<script>alert('Bill not found or already paid'); location='customerDashboard.html';</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('Error processing payment'); location='customerDashboard.html';</script>");
        }
    }
}
