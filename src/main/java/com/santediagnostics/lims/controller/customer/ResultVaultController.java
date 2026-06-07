package com.santediagnostics.lims.controller.customer;

import com.santediagnostics.lims.model.Result;
import com.santediagnostics.lims.model.TestRequest;
import com.santediagnostics.lims.service.ResultService;
import com.santediagnostics.lims.service.TestService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class ResultVaultController {

  @FXML
  private TableView<Result> resultTable;
  @FXML
  private TableColumn<Result, String> colValue;
  @FXML
  private TableColumn<Result, String> colFile;
  @FXML
  private TableColumn<Result, String> colDate;
  @FXML
  private Label countdownLabel;
  @FXML
  private Label errorLabel;

  private final ResultService resultService = new ResultService();
  private final TestService testService = new TestService();

  @FXML
  public void initialize() {
    colValue.setCellValueFactory(d -> {
      Result r = d.getValue();
      String val = r.getTextValue() != null ? r.getTextValue()
          : r.getNumericValue() != null ? r.getNumericValue().toPlainString() : "—";
      return new javafx.beans.property.SimpleStringProperty(val);
    });
    colFile.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
        d.getValue().getFilePath() != null ? "Yes" : "No"));
    colDate.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
        d.getValue().getCreatedAt().toString()));
    loadResults();
    showActiveCountdowns();
  }

  private void loadResults() {
    try {
      List<Result> results = resultService.getVerifiedResultsForCustomer(SessionManager.userId());
      resultTable.setItems(FXCollections.observableArrayList(results));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  private void showActiveCountdowns() {
    try {
      List<TestRequest> requests = testService.getCustomerRequests(SessionManager.userId());
      StringBuilder sb = new StringBuilder();
      long now = System.currentTimeMillis();
      for (TestRequest req : requests) {
        long requestedMs = req.getRequestedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        // TAT lookup would require testType — simplified display
        sb.append(req.getTestTypeName()).append(": ordered ").append(req.getRequestedAt().toLocalDate()).append("\n");
      }
      countdownLabel.setText(sb.length() > 0 ? sb.toString() : "No active requests.");
    } catch (Exception e) {
      countdownLabel.setText("");
    }
  }

  @FXML
  private void handleDownload(ActionEvent event) {
    Result selected = resultTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      errorLabel.setText("Select a result.");
      return;
    }
    if (selected.getFilePath() == null) {
      errorLabel.setText("No file attached to this result.");
      return;
    }
    try {
      Desktop.getDesktop().open(new File(selected.getFilePath()));
    } catch (Exception e) {
      errorLabel.setText("Could not open file: " + e.getMessage());
    }
  }

  @FXML
  private void goBack(ActionEvent e) {
    try {
      SceneManager.navigateTo(e, "/fxml/customer/CustomerDashboard.fxml");
    } catch (Exception ex) {
      errorLabel.setText(ex.getMessage());
    }
  }
}
