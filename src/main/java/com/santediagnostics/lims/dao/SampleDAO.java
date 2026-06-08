package com.santediagnostics.lims.dao;

import com.santediagnostics.lims.config.DatabaseConfig;
import com.santediagnostics.lims.model.Sample;
import com.santediagnostics.lims.model.SampleStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SampleDAO {

  private Sample map(ResultSet rs) throws SQLException {
    Sample s = new Sample();
    s.setId(rs.getInt("id"));
    s.setRequestId(rs.getInt("request_id"));
    s.setStatus(SampleStatus.valueOf(rs.getString("status")));
    s.setCollectedAt(rs.getTimestamp("collected_at").toLocalDateTime());
    s.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
    return s;
  }

  public Sample findById(int id) throws Exception {
    String sql = "SELECT * FROM samples WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? map(rs) : null;
    }
  }

  public Sample findByRequestId(int requestId) throws Exception {
    String sql = "SELECT * FROM samples WHERE request_id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, requestId);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? map(rs) : null;
    }
  }

  public List<Sample> findAll() throws Exception {
    List<Sample> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM samples ORDER BY collected_at DESC")) {
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }

  public int insert(int requestId) throws Exception {
    String sql = "INSERT INTO samples (request_id) VALUES (?) RETURNING id";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, requestId);
      ResultSet rs = ps.executeQuery();
      rs.next();
      return rs.getInt(1);
    }
  }

  public void updateStatus(int id, SampleStatus status) throws Exception {
    String sql = "UPDATE samples SET status = ?::sample_status, updated_at = NOW() WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, status.name());
      ps.setInt(2, id);
      ps.executeUpdate();
    }
  }
}
