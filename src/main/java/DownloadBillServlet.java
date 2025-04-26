import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/DownloadBillServlet")
public class DownloadBillServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String billId = request.getParameter("bill_id");

        if (billId == null || billId.isEmpty()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Invalid Bill ID</h3>");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT b.*, c.name, c.meter_no FROM bill b JOIN customer c ON b.meter_no = c.meter_no WHERE b.id = ?");
            ps.setString(1, billId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String meterNo = rs.getString("meter_no");
                String name = rs.getString("name");
                String month = rs.getString("month");
                int units = rs.getInt("units");
                double amount = rs.getDouble("amount");
                String status = rs.getString("status");

                // Set content type and headers for PDF download
                response.setContentType("application/pdf");
                String fileName = meterNo + "-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".pdf";
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                // Create PDF
                Document document = new Document();
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
                Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

                document.add(new Paragraph("Electricity Bill", titleFont));
                document.add(new Paragraph("Generated on: " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()), bodyFont));
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Customer Name: " + name, bodyFont));
                document.add(new Paragraph("Meter Number: " + meterNo, bodyFont));
                document.add(new Paragraph("Month: " + month, bodyFont));
                document.add(new Paragraph("Units Consumed: " + units + " units", bodyFont));
                document.add(new Paragraph("Amount: ₹" + amount, bodyFont));
                document.add(new Paragraph("Status: " + status, bodyFont));

                document.close();
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<h3>No bill found for ID: " + billId + "</h3>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Error generating bill PDF: " + e.getMessage() + "</h3>");
        }
    }
}
