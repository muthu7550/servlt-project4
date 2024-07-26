package project4;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/submit")
public class jdbc extends jakarta.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        PrintWriter out = response.getWriter();
        
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/names", "root", "Muthu@100100");
            
            // Prepare SQL statement
            String sql = "INSERT INTO namelist1 (name, pass) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, age);
            
            // Execute SQL statement
            stmt.executeUpdate();
            
            // Respond to client
            response.setContentType("text/html");
            out.println("<html><body>");
            out.println("<h2>Data successfully inserted</h2>");
            out.println("</body></html>");
            
        } catch (ClassNotFoundException e) {
            out.println("<html><body>");
            out.println("<h2>Error: MySQL Driver not found</h2>");
            out.println("</body></html>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<html><body>");
            out.println("<h2>SQL Error: " + e.getMessage() + "</h2>");
            out.println("</body></html>");
            e.printStackTrace();
        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h2>Unexpected Error: " + e.getMessage() + "</h2>");
            out.println("</body></html>");
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
