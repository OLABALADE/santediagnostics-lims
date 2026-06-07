package com.santediagnostics.lims.service;

import com.santediagnostics.lims.dao.AuditLogDAO;
import com.santediagnostics.lims.model.AuditLog;

import java.util.List;

public class AuditService {

  private final AuditLogDAO auditLogDAO = new AuditLogDAO();

  public void log(Integer userId, String action, String entityType, Integer entityId, String detail) {
    try {
      auditLogDAO.insert(userId, action, entityType, entityId, detail);
    } catch (Exception e) {
      // Audit failure must never crash the main flow
      System.err.println("Audit log failed: " + e.getMessage());
    }
  }

  public List<AuditLog> getAll() throws Exception {
    return auditLogDAO.findAll();
  }

  public List<AuditLog> getByUser(int userId) throws Exception {
    return auditLogDAO.findByUser(userId);
  }
}
