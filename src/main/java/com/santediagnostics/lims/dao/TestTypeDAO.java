package com.santediagnostics.lims.dao;

import com.santediagnostics.lims.config.DatabaseConfig;
import com.santediagnostics.lims.model.ResultFormat;
import com.santediagnostics.lims.model.TestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestTypeDAO {

  private TestType map(ResultSet rs) throws SQLException {
    TestType t = new TestType();
    t.setId(rs.getInt("id"));
    t.setName(rs.getString("name"));
    t.setPrice(rs.getBigDecimal("price"));
    t.setTurnaroundHours(rs.getInt("turnaround_hours"));
    t.setResultFormat(ResultFormat.valueOf(rs.getString("result_format")));
    t.setActive(rs.getBoolean("active"));
    t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    return t;
  }

  public List<TestType> findAll(boolean activeOnly) throws Exception {
    String sql = activeOnly
        ? "SELECT * FROM test_types WHERE active = TRUE ORDER BY name"
        : "SELECT * FROM test_types ORDER BY name";
    List<TestType> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(sql)) {
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }

  public TestType findById(int id) throws Exception {
    String sql = "SELECT * FROM test_types WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? map(rs) : null;
    }
  }

  public int insert(TestType t) throws Exception {
    String sql = "INSERT INTO test_types (name, price, turnaround_hours, result_format) " +
        "VALUES (?, ?, ?, ?::result_format) RETURNING id";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, t.getName());
      ps.setBigDecimal(2, t.getPrice());
      ps.setInt(3, t.getTurnaroundHours());
      ps.setString(4, t.getResultFormat().name());
      ResultSet rs = ps.executeQuery();
      rs.next();
      return rs.getInt(1);
    }
  }

  public void update(TestType t) throws Exception {
    String sql = "UPDATE test_types SET name = ?, price = ?, turnaround_hours = ?, result_format = ?::result_format, active = ? WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, t.getName());
      ps.setBigDecimal(2, t.getPrice());
      ps.setInt(3, t.getTurnaroundHours());
      ps.setString(4, t.getResultFormat().name());
      ps.setBoolean(5, t.isActive());
      ps.setInt(6, t.getId());
      ps.executeUpdate();
    }
  }
}
