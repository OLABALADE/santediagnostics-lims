package com.santediagnostics.lims.dao;

import com.santediagnostics.lims.config.DatabaseConfig;
import com.santediagnostics.lims.model.PaymentStatus;
import com.santediagnostics.lims.model.TestRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestRequestDAO {

  private TestRequest map(ResultSet rs) throws SQLException {
    TestRequest r = new TestRequest();
    r.setId(rs.getInt("id"));
    r.setCustomerId(rs.getInt("customer_id"));
    r.setTestTypeId(rs.getInt("test_type_id"));
    r.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
    r.setRequestedAt(rs.getTimestamp("requested_at").toLocalDateTime());
    Timestamp paid = rs.getTimestamp("paid_at");
    if (paid != null)
      r.setPaidAt(paid.toLocalDateTime());
    // joined fields (may be null if not joined)
    try {
      r.setCustomerName(rs.getString("customer_name"));
    } catch (SQLException ignored) {
    }
    try {
      r.setTestTypeName(rs.getString("test_type_name"));
    } catch (SQLException ignored) {
    }
    return r;
  }

  private static final String JOIN_SQL = "SELECT tr.*, u.name AS customer_name, tt.name AS test_type_name " +
      "FROM test_requests tr " +
      "JOIN users u ON u.id = tr.customer_id " +
      "JOIN test_types tt ON tt.id = tr.test_type_id ";

  public List<TestRequest> findAll() throws Exception {
    List<TestRequest> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(JOIN_SQL + "ORDER BY tr.requested_at DESC")) {
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }

  public List<TestRequest> findByCustomer(int customerId) throws Exception {
    String sql = JOIN_SQL + "WHERE tr.customer_id = ? ORDER BY tr.requested_at DESC";
    List<TestRequest> list = new ArrayList<>();
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, customerId);
      ResultSet rs = ps.executeQuery();
      while (rs.next())
        list.add(map(rs));
    }
    return list;
  }

  public int insert(int customerId, int testTypeId) throws Exception {
    String sql = "INSERT INTO test_requests (customer_id, test_type_id) VALUES (?, ?) RETURNING id";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, customerId);
      ps.setInt(2, testTypeId);
      ResultSet rs = ps.executeQuery();
      rs.next();
      return rs.getInt(1);
    }
  }

  public void markPaid(int id) throws Exception {
    String sql = "UPDATE test_requests SET payment_status = 'PAID', paid_at = NOW() WHERE id = ?";
    try (Connection c = DatabaseConfig.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }
}
