package com.santediagnostics.lims.controller.attendant;

import com.santediagnostics.lims.model.TestRequest;
import com.santediagnostics.lims.service.SampleService;
import com.santediagnostics.lims.service.TestService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AttendantQueueController {

  @FXML
  private TableView<TestRequest> requestTable;
  @FXML
  private TableColumn<TestRequest, String> colCustomer;
  @FXML
  private TableColumn<TestRequest, String> colTest;
  @FXML
  private TableColumn<TestRequest, String> colStatus;
  @FXML
  private TableColumn<TestRequest, String> colDate;
  @FXML
  private Label errorLabel;

  private final TestService testService = new TestService();
  private final SampleService sampleService = new SampleService();

  @FXML
  public void initialize() {
    colCustomer
        .setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCustomerName()));
    colTest.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTestTypeName()));
    colStatus.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPaymentStatus().name()));
    colDate.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getRequestedAt().toString()));
    loadRequests();
  }

  private void loadRequests() {
    try {
      requestTable.setItems(FXCollections.observableArrayList(testService.getAllRequests()));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleMarkPaid(ActionEvent event) {
    TestRequest selected = requestTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      errorLabel.setText("Select a request first.");
      return;
    }
    try {
      testService.markPaid(SessionManager.userId(), selected.getId());
      loadRequests();
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleCollectSample(ActionEvent event) {
    TestRequest selected = requestTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      errorLabel.setText("Select a request first.");
      return;
    }
    try {
      sampleService.collectSample(SessionManager.userId(), selected.getId());
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Sample collected.");
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void goBack(ActionEvent e) {
    try {
      SceneManager.navigateTo(e, "/fxml/attendant/AttendantDashboard.fxml");
    } catch (Exception ex) {
      errorLabel.setText(ex.getMessage());
    }
  }
}
