package com.santediagnostics.lims.controller.customer;

import com.santediagnostics.lims.dao.TestTypeDAO;
import com.santediagnostics.lims.model.Result;
import com.santediagnostics.lims.model.TestRequest;
import com.santediagnostics.lims.model.TestType;
import com.santediagnostics.lims.service.ResultService;
import com.santediagnostics.lims.service.TestService;
import com.santediagnostics.lims.util.CountdownTimer;
import com.santediagnostics.lims.util.FileUtil;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
  private VBox countdownBox;
  @FXML
  private Label errorLabel;

  private final ResultService resultService = new ResultService();
  private final TestService testService = new TestService();
  private final TestTypeDAO testTypeDAO = new TestTypeDAO();
  private final List<CountdownTimer> timers = new ArrayList<>();

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
    loadCountdowns();
  }

  private void loadResults() {
    try {
      resultTable.setItems(FXCollections.observableArrayList(
          resultService.getVerifiedResultsForCustomer(SessionManager.userId())));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  private void loadCountdowns() {
    stopAllTimers();
    if (countdownBox == null)
      return;
    countdownBox.getChildren().clear();
    try {
      List<TestRequest> requests = testService.getCustomerRequests(SessionManager.userId());
      if (requests.isEmpty()) {
        countdownBox.getChildren().add(new Label("No active requests."));
        return;
      }
      for (TestRequest req : requests) {
        Label nameLabel = new Label(req.getTestTypeName() + " — " + req.getPaymentStatus());
        nameLabel.setStyle("-fx-font-weight: bold;");
        countdownBox.getChildren().add(nameLabel);

        TestType type = testTypeDAO.findById(req.getTestTypeId());
        if (type == null) {
          countdownBox.getChildren().add(new Label("TAT unavailable"));
          continue;
        }

        LocalDateTime deadline = req.getRequestedAt().plusHours(type.getTurnaroundHours());
        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-text-fill: #1a73e8;");

        if (deadline.isBefore(LocalDateTime.now())) {
          timerLabel.setText("Processing complete — awaiting result.");
        } else {
          CountdownTimer timer = new CountdownTimer();
          timer.start(timerLabel, deadline);
          timers.add(timer);
        }

        countdownBox.getChildren().add(timerLabel);
      }
    } catch (Exception e) {
      countdownBox.getChildren().add(new Label("Error loading orders: " + e.getMessage()));
    }
  }

  private void stopAllTimers() {
    timers.forEach(CountdownTimer::stop);
    timers.clear();
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
    new Thread(() -> {
      try {
        FileUtil.openFile(selected.getFilePath());
      } catch (Exception e) {
        javafx.application.Platform.runLater(() -> errorLabel.setText("Could not open file: " + e.getMessage()));
      }
    }).start();
  }

  @FXML
  private void goBack(ActionEvent e) {
    stopAllTimers();
    try {
      SceneManager.navigateTo(e, "/fxml/customer/CustomerDashboard.fxml");
    } catch (Exception ex) {
      errorLabel.setText(ex.getMessage());
    }
  }
}
