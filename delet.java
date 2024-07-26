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

@WebServlet("/delet")
public class delet extends jakarta.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/names", "root", "Muthu@100100");
            
            // Prepare SQL statement to delete the record
            String sql = "DELETE FROM namelist1 WHERE name = ? AND pass = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, age);
            
            // Execute SQL update
            int rowsAffected = stmt.executeUpdate();
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            
            // Check if a record was deleted
            if (rowsAffected > 0) {
                // Record deleted, show success message
                out.println("<h2>Record successfully deleted</h2>");
            } else {
                // No matching record found to delete
                out.println("<h2>Error: No matching record found</h2>");
            }
            
            out.println("</body></html>");
            
        } catch (ClassNotFoundException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Error: MySQL Driver not found</h2>");
            out.println("</body></html>");
            e.printStackTrace();
        } catch (SQLException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>SQL Error: " + e.getMessage() + "</h2>");
            out.println("</body></html>");
            e.printStackTrace();
        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
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
