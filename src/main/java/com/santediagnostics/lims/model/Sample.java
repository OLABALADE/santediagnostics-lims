package com.santediagnostics.lims.model;

import java.time.LocalDateTime;

public class Sample {
  private int id;
  private int requestId;
  private SampleStatus status;
  private LocalDateTime collectedAt;
  private LocalDateTime updatedAt;

  public Sample() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public SampleStatus getStatus() {
    return status;
  }

  public void setStatus(SampleStatus status) {
    this.status = status;
  }

  public LocalDateTime getCollectedAt() {
    return collectedAt;
  }

  public void setCollectedAt(LocalDateTime collectedAt) {
    this.collectedAt = collectedAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
