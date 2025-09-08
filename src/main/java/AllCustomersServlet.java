import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class AllCustomersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer")) {

            out.println("<table border='1' class='glass-table' style='width: 100%; border-collapse: collapse;'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Mobile</th><th>Status</th><th>Action</th></tr>");

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
                    // Approve Button
                    out.println("<form method='post' action='UpdateCustomerStatusServlet' style='display:inline;'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");
                    out.println("<input type='hidden' name='status' value='approved'>");
                    out.println("<button type='submit'>Approve✅</button>");
                    out.println("</form>");

                    // Reject Button
                    out.println("<form method='post' action='UpdateCustomerStatusServlet' style='display:inline;'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");
                    out.println("<input type='hidden' name='status' value='rejected'>");
                    out.println("<button type='submit'>Reject❌</button>");
                    out.println("</form>");

                } else if ("approved".equalsIgnoreCase(status)) {
                    out.println("<span style='color:green; font-weight:bold;'>Approved✅</span>");

                } else if ("rejected".equalsIgnoreCase(status)) {
                	out.println("<form method='post' action='UpdateCustomerStatusServlet' style='display:inline;'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");
                    out.println("<input type='hidden' name='status' value='approved'>");
                    out.println("<button type='submit'>Approve✅</button>");
                    out.println("</form>");
                    // ❌ Show delete if rejected
                    out.println("<form method='post' action='DeleteCustomerServlet' style='display:inline;' onsubmit='return confirm(\"Are you sure to delete this rejected customer?\");'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");
                    out.println("<button type='submit' style='background-color:red; color:white;'>Delete❌</button>");
                    out.println("</form>");
                }


                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
}
