package com.santediagnostics.lims.model;

import java.time.LocalDateTime;

public class TestRequest {
  private int id;
  private int customerId;
  private int testTypeId;
  private PaymentStatus paymentStatus;
  private LocalDateTime requestedAt;
  private LocalDateTime paidAt;

  // Joined fields
  private String customerName;
  private String testTypeName;

  public TestRequest() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getTestTypeId() {
    return testTypeId;
  }

  public void setTestTypeId(int testTypeId) {
    this.testTypeId = testTypeId;
  }

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public LocalDateTime getRequestedAt() {
    return requestedAt;
  }

  public void setRequestedAt(LocalDateTime requestedAt) {
    this.requestedAt = requestedAt;
  }

  public LocalDateTime getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(LocalDateTime paidAt) {
    this.paidAt = paidAt;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getTestTypeName() {
    return testTypeName;
  }

  public void setTestTypeName(String testTypeName) {
    this.testTypeName = testTypeName;
  }
}
