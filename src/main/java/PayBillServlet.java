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

        // Check if coming from customer or admin
        String billIdStr = request.getParameter("bill_id"); // from customer
        String billIdAdm = request.getParameter("id");      // from admin

        String redirectPage;
        String billIdToUse;

        if (billIdAdm != null && !billIdAdm.isEmpty()) {
            // Admin side
            billIdToUse = billIdAdm;
            redirectPage = request.getContextPath()+"/viewBills.html";
        } else if (billIdStr != null && !billIdStr.isEmpty()) {
            // Customer side
            billIdToUse = billIdStr;
            redirectPage = request.getContextPath()+"/customerDashboard.html";
        } else {
            out.println("<script>alert('Invalid Bill ID'); window.history.back();</script>");
            return;
        }

        try {
            int billId = Integer.parseInt(billIdToUse);

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("UPDATE bill SET status='Paid' WHERE id=?");
                ps.setInt(1, billId);

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    out.println("<script>alert('Bill Paid Successfully'); location='" + redirectPage + "';</script>");
                } else {
                    out.println("<script>alert('Bill not found or already paid'); location='" + redirectPage + "';</script>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('Error processing payment'); location='" + redirectPage + "';</script>");
        }
    }
}
