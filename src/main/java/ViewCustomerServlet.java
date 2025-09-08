import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/ViewCustomersServlet")
public class ViewCustomerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // HTML Header
        out.println("<html><head><title>View Customers</title>");
        out.println("<link rel='stylesheet' href='css/styles.css'>");
        out.println("<script src='js/popup.js'></script>");
        out.println("</head><body>");
        out.println("<div class='glass-container'>");
        out.println("<h2>Customer List</h2>");
        out.println("<table border='1' class='glass-table' cellpadding='10'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Mobile</th><th>Status</th><th>Action</th></tr>");

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile_no");
                String status = rs.getString("status");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("<td>" + mobile + "</td>");
                out.println("<td>" + status + "</td>");
                out.println("<td>");

                if ("pending".equalsIgnoreCase(status)) {
                    out.println("<form method='post' action='UpdateCustomerStatusServlet' style='display:inline;'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");
                    out.println("<input type='hidden' name='status' value='approved'>");
                    out.println("<button type='submit'>Approve</button>");
                    out.println("</form>");

                    out.println("<form method='post' action='DeleteCustomerServlet' style='display:inline;'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");
                    out.println("<button type='submit' >Reject❌</button>");
                    out.println("</form>");
                } else {
                    out.println(status);
                }

                out.println("</td>");
                out.println("</tr>");
            }

        } catch (Exception e) {
            out.println("<tr><td colspan='6'>Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("</table>");
        out.println("<br><button onclick=\"location.href='adminDashboard.html'\">Back</button>");
        out.println("</div>");
        out.println("</body></html>");
    }
}
