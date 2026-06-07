package com.santediagnostics.lims.service;

import com.santediagnostics.lims.dao.SampleDAO;
import com.santediagnostics.lims.model.Sample;
import com.santediagnostics.lims.model.SampleStatus;

import java.util.List;

public class SampleService {

  private final SampleDAO sampleDAO = new SampleDAO();
  private final AuditService auditService = new AuditService();

  public int collectSample(int attendantId, int requestId) throws Exception {
    int id = sampleDAO.insert(requestId);
    auditService.log(attendantId, "COLLECT_SAMPLE", "samples", id, "request_id=" + requestId);
    return id;
  }

  public void updateStatus(int attendantId, int sampleId, SampleStatus status) throws Exception {
    sampleDAO.updateStatus(sampleId, status);
    auditService.log(attendantId, "UPDATE_SAMPLE_STATUS", "samples", sampleId, status.name());
  }

  public List<Sample> getAllSamples() throws Exception {
    return sampleDAO.findAll();
  }

  public Sample getSampleByRequest(int requestId) throws Exception {
    return sampleDAO.findByRequestId(requestId);
  }
}
