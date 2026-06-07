package com.santediagnostics.lims.dao;

import com.santediagnostics.lims.config.DatabaseConfig;
import com.santediagnostics.lims.model.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

  private AuditLog map(ResultSet rs) throws SQLException {
    AuditLog a = new AuditLog();
    a.setId(rs.getInt("id"));
    int uid = rs.getInt("user_id");
    if (!rs.wasNull())
      a.setUserId(uid);
    a.setAction(rs.getString("action"));
    a.setEntityType(rs.getString("entity_type"));
    int eid = rs.getInt("entity_id");
    if (!rs.wasNull())
      a.setEntityId(eid);
    a.setDetail(rs.getString("detail"));
    a.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    return a;
  }

  /** Insert only — no update or delete methods by design. */
  public void insert(Integer userId, String action, String entityType, Integer entityId, String detail)
      throws Exception {
    String sql = "INSERT INTO audit_logs (user_id, action, entity_type, entity_id, detail) VALUES (?, ?, ?, ?, ?)";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      if (userId != null)
        ps.setInt(1, userId);
      else
        ps.setNull(1, Types.INTEGER);
      ps.setString(2, action);
      ps.setString(3, entityType);
      if (entityId != null)
        ps.setInt(4, entityId);
      else
        ps.setNull(4, Types.INTEGER);
      ps.setString(5, detail);
      ps.executeUpdate();
    }
  }

  public List<AuditLog> findAll() throws Exception {
    List<AuditLog> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM audit_logs ORDER BY created_at DESC")) {
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }

  public List<AuditLog> findByUser(int userId) throws Exception {
    String sql = "SELECT * FROM audit_logs WHERE user_id = ? ORDER BY created_at DESC";
    List<AuditLog> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }
}
