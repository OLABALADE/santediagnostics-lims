package com.santediagnostics.lims.controller.attendant;

import com.santediagnostics.lims.model.Sample;
import com.santediagnostics.lims.model.SampleStatus;
import com.santediagnostics.lims.service.SampleService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SampleTrackingController {

  @FXML
  private TableView<Sample> sampleTable;
  @FXML
  private TableColumn<Sample, Integer> colId;
  @FXML
  private TableColumn<Sample, Integer> colRequest;
  @FXML
  private TableColumn<Sample, String> colStatus;
  @FXML
  private TableColumn<Sample, String> colUpdated;
  @FXML
  private ComboBox<SampleStatus> statusCombo;
  @FXML
  private Label errorLabel;

  private final SampleService sampleService = new SampleService();

  @FXML
  public void initialize() {
    statusCombo.setItems(FXCollections.observableArrayList(SampleStatus.values()));
    colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
    colRequest.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getRequestId()));
    colStatus.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getStatus().name()));
    colUpdated.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getUpdatedAt().toString()));
    loadSamples();
  }

  private void loadSamples() {
    try {
      sampleTable.setItems(FXCollections.observableArrayList(sampleService.getAllSamples()));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleUpdateStatus(ActionEvent event) {
    Sample selected = sampleTable.getSelectionModel().getSelectedItem();
    SampleStatus status = statusCombo.getValue();
    if (selected == null || status == null) {
      errorLabel.setText("Select a sample and status.");
      return;
    }
    try {
      sampleService.updateStatus(SessionManager.userId(), selected.getId(), status);
      loadSamples();
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
