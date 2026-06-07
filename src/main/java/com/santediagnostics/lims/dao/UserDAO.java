package com.santediagnostics.lims.dao;

import com.santediagnostics.lims.config.DatabaseConfig;
import com.santediagnostics.lims.model.Role;
import com.santediagnostics.lims.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

  private User map(ResultSet rs) throws SQLException {
    User u = new User();
    u.setId(rs.getInt("id"));
    u.setName(rs.getString("name"));
    u.setEmail(rs.getString("email"));
    u.setPasswordHash(rs.getString("password_hash"));
    u.setRole(Role.valueOf(rs.getString("role")));
    u.setEmailVerified(rs.getBoolean("email_verified"));
    u.setForcePasswordChange(rs.getBoolean("force_password_change"));
    u.setVerificationToken(rs.getString("verification_token"));
    u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    return u;
  }

  public User findByEmail(String email) throws Exception {
    String sql = "SELECT * FROM users WHERE email = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? map(rs) : null;
    }
  }

  public User findByVerificationToken(String token) throws Exception {
    String sql = "SELECT * FROM users WHERE verification_token = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, token);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? map(rs) : null;
    }
  }

  public List<User> findByRole(Role role) throws Exception {
    String sql = "SELECT * FROM users WHERE role = ?::user_role ORDER BY name";
    List<User> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, role.name());
      ResultSet rs = ps.executeQuery();
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }

  public int insert(User u) throws Exception {
    String sql = "INSERT INTO users (name, email, password_hash, role, email_verified, force_password_change, verification_token) "
        +
        "VALUES (?, ?, ?, ?::user_role, ?, ?, ?) RETURNING id";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, u.getName());
      ps.setString(2, u.getEmail());
      ps.setString(3, u.getPasswordHash());
      ps.setString(4, u.getRole().name());
      ps.setBoolean(5, u.isEmailVerified());
      ps.setBoolean(6, u.isForcePasswordChange());
      ps.setString(7, u.getVerificationToken());
      ResultSet rs = ps.executeQuery();
      rs.next();
      return rs.getInt(1);
    }
  }

  public void updatePassword(int id, String passwordHash, boolean forcePasswordChange) throws Exception {
    String sql = "UPDATE users SET password_hash = ?, force_password_change = ? WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, passwordHash);
      ps.setBoolean(2, forcePasswordChange);
      ps.setInt(3, id);
      ps.executeUpdate();
    }
  }

  public void verifyEmail(int id) throws Exception {
    String sql = "UPDATE users SET email_verified = TRUE, verification_token = NULL WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }
}
