package com.santediagnostics.lims.dao;

import com.santediagnostics.lims.config.DatabaseConfig;
import com.santediagnostics.lims.model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

  private Result map(ResultSet rs) throws SQLException {
    Result r = new Result();
    r.setId(rs.getInt("id"));
    r.setSampleId(rs.getInt("sample_id"));
    r.setTextValue(rs.getString("text_value"));
    r.setNumericValue(rs.getBigDecimal("numeric_value"));
    r.setFilePath(rs.getString("file_path"));
    r.setVerified(rs.getBoolean("verified"));
    int vb = rs.getInt("verified_by");
    if (!rs.wasNull())
      r.setVerifiedBy(vb);
    Timestamp va = rs.getTimestamp("verified_at");
    if (va != null)
      r.setVerifiedAt(va.toLocalDateTime());
    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    return r;
  }

  public Result findBySampleId(int sampleId) throws Exception {
    String sql = "SELECT * FROM results WHERE sample_id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, sampleId);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? map(rs) : null;
    }
  }

  public List<Result> findVerifiedByCustomer(int customerId) throws Exception {
    String sql = "SELECT r.* FROM results r " +
        "JOIN samples s ON s.id = r.sample_id " +
        "JOIN test_requests tr ON tr.id = s.request_id " +
        "WHERE tr.customer_id = ? AND r.verified = TRUE " +
        "ORDER BY r.created_at DESC";
    List<Result> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, customerId);
      ResultSet rs = ps.executeQuery();
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }

  public int insert(Result r) throws Exception {
    String sql = "INSERT INTO results (sample_id, text_value, numeric_value, file_path) " +
        "VALUES (?, ?, ?, ?) RETURNING id";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, r.getSampleId());
      ps.setString(2, r.getTextValue());
      ps.setBigDecimal(3, r.getNumericValue());
      ps.setString(4, r.getFilePath());
      ResultSet rs = ps.executeQuery();
      rs.next();
      return rs.getInt(1);
    }
  }

  public void verify(int id, int verifiedBy) throws Exception {
    String sql = "UPDATE results SET verified = TRUE, verified_by = ?, verified_at = NOW() WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, verifiedBy);
      ps.setInt(2, id);
      ps.executeUpdate();
    }
  }
}
