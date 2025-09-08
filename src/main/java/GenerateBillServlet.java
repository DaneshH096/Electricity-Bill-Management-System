import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/GenerateBillServlet")
public class GenerateBillServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String meterNo = request.getParameter("meter_no");
        String month = request.getParameter("month");
        int units = Integer.parseInt(request.getParameter("units"));
        double amount = calculateBill(units);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO bill (meter_no, month, units, amount, status) VALUES (?, ?, ?, ?, 'Unpaid')");
            ps.setString(1, meterNo);
            ps.setString(2, month);
            ps.setInt(3, units);
            ps.setDouble(4, amount);

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<script>alert('Bill Generated Successfully!'); location='"+request.getContextPath()+"/adminDashboard.html';</script>");
            } else {
                out.println("<script>alert('Failed to generate bill'); location='"+request.getContextPath()+"/generateBill.html';</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('Error Occurred'); location='"+request.getContextPath()+"/generateBill.html';</script>");
        }
    }

    private double calculateBill(int units) {
        double amount = 0.0;
        if (units <= 100) {
            amount = units * 3.75;
        } else if (units <= 200) {
            amount = 100 * 3.75 + (units - 100) * 5.20;
        } else {
            amount = 100 * 3.75 + 100 * 5.20 + (units - 200) * 6.75;
        }
        return amount;
    }
}
