package com.quizapp.dao;

import com.quizapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for user CRUD operations. Methods return model objects and
 * consistently use try-with-resources to close DB resources.
 */
public class UserDAO {
    /**
     * Find a user by email. Includes password_hash so callers that
     * perform authentication can verify credentials.
     *
     * @param email email to search
     * @return User or null if not found
     * @throws SQLException on DB errors
     */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, email, role, password_hash FROM users WHERE email=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setRole(rs.getString("role"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Return all users (without password hash).
     *
     * @return list of users
     * @throws SQLException on DB errors
     */
    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, name, email, role FROM users";
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                list.add(u);
            }
        }
        return list;
    }

    /**
     * Create a new user. Sets the generated id on the User object.
     *
     * @param u user to create (should have passwordHash set)
     * @throws SQLException on DB errors
     */
    public void create(User u) throws SQLException {
        String sql = "INSERT INTO users (name,email,password_hash,role) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, u.getName());
            p.setString(2, u.getEmail());
            p.setString(3, u.getPasswordHash());
            p.setString(4, u.getRole());
            int rows = p.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Failed to insert user, updateCount=" + rows);
            }
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Update user fields. If passwordHash is null, the password is left unchanged.
     *
     * @param u user with updated data
     * @throws SQLException on DB errors
     */
    public void update(User u) throws SQLException {
        if (u.getPasswordHash() == null) {
            String sql = "UPDATE users SET name=?, email=?, role=? WHERE id=?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement p = conn.prepareStatement(sql)) {
                p.setString(1, u.getName());
                p.setString(2, u.getEmail());
                p.setString(3, u.getRole());
                p.setInt(4, u.getId());
                p.executeUpdate();
            }
        } else {
            String sql = "UPDATE users SET name=?, email=?, password_hash=?, role=? WHERE id=?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement p = conn.prepareStatement(sql)) {
                p.setString(1, u.getName());
                p.setString(2, u.getEmail());
                p.setString(3, u.getPasswordHash());
                p.setString(4, u.getRole());
                p.setInt(5, u.getId());
                p.executeUpdate();
            }
        }
    }

    /**
     * Delete a user by id.
     *
     * @param id user id
     * @throws SQLException on DB errors
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, id);
            p.executeUpdate();
        }
    }
}
