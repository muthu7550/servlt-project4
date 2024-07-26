package project4;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/validate")
public class ValidateServlet extends jakarta.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/names", "root", "Muthu@100100");
            
            // Prepare SQL statement
            String sql = "SELECT * FROM namelist1 WHERE name = ? AND pass = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, age);
            
            // Execute SQL query
            rs = stmt.executeQuery();
            
            // Check if a matching record is found
            if (rs.next()) {
                // Record found, redirect to the success HTML page
                response.sendRedirect("json.html");
            } else {
                // No matching record found, display an error message
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body>");
                out.println("<h2>Error: Invalid name or age</h2>");
                out.println("</body></html>");
            }
            
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
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
