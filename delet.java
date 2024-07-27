
package project3;

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

@WebServlet("/update")
public class jdbc extends jakarta.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name1 = request.getParameter("name1");
        String age1 = request.getParameter("age1");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        PrintWriter out = response.getWriter();
        
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/names", "root", "12345");
            
            // Prepare SQL statement
            String sql = "UPDATE namelist SET age = ? WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, age1);
            stmt.setString(2, name1);
            
            // Execute SQL statement
            int rowsUpdated = stmt.executeUpdate();
            
            // Respond to client
            response.setContentType("text/html");
            out.println("<html><body>");
            if (rowsUpdated > 0) {
                out.println("<h2>Update successful</h2>");
            } else {
                out.println("<h2>No record found with the given name</h2>");
            }
            out.print("<a href=/project3/validate.html>");
            out.print("<h1>Click here to login</h1>");
            out.print("</a>");
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
