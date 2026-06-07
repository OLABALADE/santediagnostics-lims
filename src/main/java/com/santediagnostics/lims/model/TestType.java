package com.santediagnostics.lims.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestType {
  private int id;
  private String name;
  private BigDecimal price;
  private int turnaroundHours;
  private ResultFormat resultFormat;
  private boolean active;
  private LocalDateTime createdAt;

  public TestType() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getTurnaroundHours() {
    return turnaroundHours;
  }

  public void setTurnaroundHours(int turnaroundHours) {
    this.turnaroundHours = turnaroundHours;
  }

  public ResultFormat getResultFormat() {
    return resultFormat;
  }

  public void setResultFormat(ResultFormat resultFormat) {
    this.resultFormat = resultFormat;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
