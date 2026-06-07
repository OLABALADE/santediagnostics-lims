package com.santediagnostics.lims.service;

import com.santediagnostics.lims.dao.TestRequestDAO;
import com.santediagnostics.lims.dao.TestTypeDAO;
import com.santediagnostics.lims.model.TestRequest;
import com.santediagnostics.lims.model.TestType;

import java.util.List;

public class TestService {

  private final TestTypeDAO testTypeDAO = new TestTypeDAO();
  private final TestRequestDAO requestDAO = new TestRequestDAO();
  private final AuditService auditService = new AuditService();

  public List<TestType> getActiveTests() throws Exception {
    return testTypeDAO.findAll(true);
  }

  public List<TestType> getAllTests() throws Exception {
    return testTypeDAO.findAll(false);
  }

  public void createTestType(int adminId, TestType t) throws Exception {
    int id = testTypeDAO.insert(t);
    auditService.log(adminId, "CREATE_TEST_TYPE", "test_types", id, t.getName());
  }

  public void updateTestType(int adminId, TestType t) throws Exception {
    testTypeDAO.update(t);
    auditService.log(adminId, "UPDATE_TEST_TYPE", "test_types", t.getId(), t.getName());
  }

  public int placeOrder(int customerId, int testTypeId) throws Exception {
    int id = requestDAO.insert(customerId, testTypeId);
    auditService.log(customerId, "PLACE_ORDER", "test_requests", id, "test_type_id=" + testTypeId);
    return id;
  }

  public List<TestRequest> getAllRequests() throws Exception {
    return requestDAO.findAll();
  }

  public List<TestRequest> getCustomerRequests(int customerId) throws Exception {
    return requestDAO.findByCustomer(customerId);
  }

  public void markPaid(int staffId, int requestId) throws Exception {
    requestDAO.markPaid(requestId);
    auditService.log(staffId, "MARK_PAID", "test_requests", requestId, null);
  }
}
