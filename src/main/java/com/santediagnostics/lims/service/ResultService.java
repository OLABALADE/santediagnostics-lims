package com.santediagnostics.lims.service;

import com.santediagnostics.lims.dao.ResultDAO;
import com.santediagnostics.lims.dao.SampleDAO;
import com.santediagnostics.lims.dao.TestRequestDAO;
import com.santediagnostics.lims.dao.UserDAO;
import com.santediagnostics.lims.model.*;

import java.util.List;

public class ResultService {

  private final ResultDAO resultDAO = new ResultDAO();
  private final SampleDAO sampleDAO = new SampleDAO();
  private final TestRequestDAO requestDAO = new TestRequestDAO();
  private final UserDAO userDAO = new UserDAO();
  private final AuditService auditService = new AuditService();
  private final EmailService emailService = new EmailService();

  public int uploadResult(int attendantId, Result result) throws Exception {
    int id = resultDAO.insert(result);
    auditService.log(attendantId, "UPLOAD_RESULT", "results", id, "sample_id=" + result.getSampleId());
    return id;
  }

  /** Verify result and email the customer. */
  public void verifyResult(int attendantId, int resultId, int sampleId) throws Exception {
    resultDAO.verify(resultId, attendantId);
    auditService.log(attendantId, "VERIFY_RESULT", "results", resultId, null);

    Sample sample = sampleDAO.findById(sampleId);
    if (sample == null)
      return;
    TestRequest req = requestDAO.findAll().stream()
        .filter(r -> r.getId() == sample.getRequestId())
        .findFirst().orElse(null);
    if (req == null)
      return;
    User customer = userDAO.findById(req.getCustomerId());
    if (customer == null)
      return;
    emailService.sendResultReady(customer.getEmail(), customer.getName(), req.getTestTypeName());
  }

  public Result getResultBySample(int sampleId) throws Exception {
    return resultDAO.findBySampleId(sampleId);
  }

  public List<Result> getVerifiedResultsForCustomer(int customerId) throws Exception {
    return resultDAO.findVerifiedByCustomer(customerId);
  }
}
