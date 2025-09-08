
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		 response.setContentType("text/html");
         PrintWriter out = response.getWriter();
     	out.println("<script>alert('Logged out successfully'); location='"+request.getContextPath()+"/login.html';</script>");


    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	 response.setContentType("text/html");
         PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    	out.println("<script>alert('Logged out successfully'); location='"+request.getContextPath()+"/login.html';</script>");
    }
}
