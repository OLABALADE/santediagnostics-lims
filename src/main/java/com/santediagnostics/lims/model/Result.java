package com.santediagnostics.lims.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Result {
  private int id;
  private int sampleId;
  private String textValue;
  private BigDecimal numericValue;
  private String filePath;
  private boolean verified;
  private Integer verifiedBy;
  private LocalDateTime verifiedAt;
  private LocalDateTime createdAt;

  public Result() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getSampleId() {
    return sampleId;
  }

  public void setSampleId(int sampleId) {
    this.sampleId = sampleId;
  }

  public String getTextValue() {
    return textValue;
  }

  public void setTextValue(String textValue) {
    this.textValue = textValue;
  }

  public BigDecimal getNumericValue() {
    return numericValue;
  }

  public void setNumericValue(BigDecimal numericValue) {
    this.numericValue = numericValue;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public Integer getVerifiedBy() {
    return verifiedBy;
  }

  public void setVerifiedBy(Integer verifiedBy) {
    this.verifiedBy = verifiedBy;
  }

  public LocalDateTime getVerifiedAt() {
    return verifiedAt;
  }

  public void setVerifiedAt(LocalDateTime verifiedAt) {
    this.verifiedAt = verifiedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
