package cabinet.dao;

import cabinet.DatabaseConnection;
import cabinet.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// DAO = Data Access Object
// This class is responsible for all database operations related to the 'users' table.
public class UserDAO {

    public User validateLogin(String username, String password) {

        String query = "SELECT * FROM users WHERE username = ? AND password = ?"; // We use '?' (placeholders) to
                                                                                  // prevent SQL Injection (a security
                                                                                  // vulnerability).

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"));
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }
}
