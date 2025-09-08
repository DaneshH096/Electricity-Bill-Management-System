import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CustomerDashboardServlet")
public class CustomerDashboardServlet extends HttpServlet {
	

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Step 1: Get the session and check if meter_no is present
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("meter_no") == null) {
         	out.println("<script>alert('Logged out successfully'); location='"+request.getContextPath()+"/login.html';</script>");
             // End the request-response cycle if session is invalid
        }

        // Step 2: Get meter_no from the session
        String meterNo = (String) session.getAttribute("meter_no");
        
        // Also get customer name if available
        out.println("<div class='customer-info'>");
        String customerName = (String) session.getAttribute("name");
        if (customerName != null) {
            out.println("<p><strong>Customer Name:</strong> " + customerName + "</p>");
        }
     

        // Display customer info
        

        // Step 3: Connect to the database and query bills
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) {
                out.println("<div class='error-message'>Database connection failed. Please try again later.</div>");
                return;
            }
            
            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT b.*, c.name, c.meter_no FROM bill b JOIN customer c ON b.meter_no = c.meter_no WHERE c.meter_no = ?");
            ps1.setString(1, meterNo);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                customerName= rs1.getString("name");
            }
            if (customerName != null) {
                out.println("<p><strong>Customer Name:</strong> " + customerName + "</p>");
            }
            
            out.println("<p><strong>Meter Number:</strong> " + meterNo + "</p>");
            out.println("</div>");
            // Prepare SQL query to fetch bills for the customer based on meter_no
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bill WHERE meter_no = ? ORDER BY id DESC");
            ps.setString(1, meterNo);
            ResultSet rs = ps.executeQuery();
            

            // Step 4: Check if no bills were found for this customer
            if (!rs.isBeforeFirst()) { // This checks if the ResultSet is empty
                out.println("<div class='no-data-message'>No bills found for meter number " + meterNo + ".</div>");
            } else {
                // Step 5: Output the bills in an HTML table
                out.println("<table border='1' class='glass-table'>");
                out.println("<tr><th>Month</th><th>Units</th><th>Amount</th><th>Status</th><th>Download</th></tr>");
                
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getString("month") + "</td>");
                    out.println("<td>" + rs.getInt("units") + "</td>");
                    out.println("<td>₹" + rs.getDouble("amount") + "</td>");
                    
                    // Add status with color coding
                    String status = rs.getString("status");
                    String statusColor = "black";
                    if ("Paid".equalsIgnoreCase(status)) {
                        statusColor = "green";
                    } else if ("Unpaid".equalsIgnoreCase(status)) {
                        statusColor = "red";
                    } else if ("Pending".equalsIgnoreCase(status)) {
                        statusColor = "orange";
                    }
                    out.println("<td style='color:" + statusColor + ";'>" + status + "</td>");
                    
                    out.println("<td>");
                    
                    out.println("<form method='post' action='DownloadBillServlet' target='_blank'>");
                    out.println("<input type='hidden' name='bill_id' value='" + rs.getInt("id") + "'>");
                    out.println("<button type='submit' class='download-btn'>Download</button>");
                    out.println("</form>");
                    out.println("</td>");
                    out.println("</td>");
                    if ("Unpaid".equalsIgnoreCase(status)) {
                    	out.println("<td>");
                        out.println("<form method='post' action='PayBillServlet'>");
                        out.println("<input type='hidden' name='bill_id' value='" + rs.getInt("id") + "'>");
                        out.println("<button type='submit' style='background-color: #28a745; color: white;'>Pay Now</button>");
                        out.println("</form>");
                    }
                    out.println("</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
        } catch (SQLException e) {
            // Step 6: Handle database errors
            out.println("<div class='error-message'>Error fetching bill details: " + e.getMessage() + "</div>");
            log("SQL Error in CustomerDashboardServlet: " + e.getMessage(), e);
        } catch (Exception e) {
            // Step 7: Handle other errors
            out.println("<div class='error-message'>Unexpected error: " + e.getMessage() + "</div>");
            log("Unexpected error in CustomerDashboardServlet: " + e.getMessage(), e);
        } finally {
            // Step 8: Close the database connection
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log("Error closing connection: " + e.getMessage(), e);
                }
            }
        }
    }
}