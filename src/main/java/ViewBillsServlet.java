import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/ViewBillsServlet")
public class ViewBillsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String meterNo = request.getParameter("meter_no");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bill WHERE meter_no = ?");
            ps.setString(1, meterNo);
            ResultSet rs = ps.executeQuery();
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>View Bills</title>");

            // ✅ CSS Link Tag
            out.println("<link rel='stylesheet' href='css/styles.css'>");

            out.println("</head>");
            out.println("<body>");

            out.println("<div class='glass-container'>");
            out.println("<h2>Bills for Meter No: " + meterNo + "</h2>");
            out.println("<table border='1' class='glass-table'>");
            out.println("<tr><th>Month</th><th>Units</th><th>Amount</th><th>Status</th><th>Action</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String month = rs.getString("month");
                int units = rs.getInt("units");
                double amount = rs.getDouble("amount");
                String status = rs.getString("status");

                out.println("<tr>");
                out.println("<td>" + month + "</td>");
                out.println("<td>" + units + "</td>");
                out.println("<td>₹" + amount + "</td>");
                out.println("<td>" + status + "</td>");
                if (!"Paid".equalsIgnoreCase(status)) {
                    out.println("<td>");
                    out.println("<form method='post' action='PayBillServlet'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");
                    out.println("<button type='submit'>Mark as Paid</button>");
                    out.println("</form>");
                    out.println("</td>");
                } else {
                    out.println("<td>✔️</td>");
                }
                
                out.println("<td>");
                out.println("<form method='post' action='DeleteBillServlet' style='display:inline;margin-left:10px;' onsubmit='return confirm(\"Are you sure you want to delete this bill?\");'>");
                out.println("<input type='hidden' name='id' value='" + id + "'>");
                out.println("<button type='submit' style='background-color:red;color:white;'>Delete</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("<br>");            
            out.println("<button onclick=\"window.history.back()\">Back</button>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red;'>Error retrieving bills</p>");
        }
    }
}
