import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DeleteBillServlet")
public class DeleteBillServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String billId = request.getParameter("id");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM bill WHERE id = ?");
            ps.setString(1, billId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
            	out.println("<script>alert('Deleted Succesfully'); location='"+request.getContextPath()+"/viewBills.html';</script>");
            } else {
            	out.println("<script>alert('Error Occured'); location='"+request.getContextPath()+"/viewBills.html';</script>");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());;
            out.println("<script>alert('Exception Occured'); location='"+request.getContextPath()+"/viewBills.html';</script>");
        }
    }
}
