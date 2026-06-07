package com.santediagnostics.lims.controller.superadmin;

import com.santediagnostics.lims.model.AuditLog;
import com.santediagnostics.lims.service.AuditService;
import com.santediagnostics.lims.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AuditTrailController {

  @FXML
  private TableView<AuditLog> auditTable;
  @FXML
  private TableColumn<AuditLog, String> colUser;
  @FXML
  private TableColumn<AuditLog, String> colAction;
  @FXML
  private TableColumn<AuditLog, String> colEntity;
  @FXML
  private TableColumn<AuditLog, String> colDetail;
  @FXML
  private TableColumn<AuditLog, String> colDate;
  @FXML
  private Label errorLabel;

  private final AuditService auditService = new AuditService();

  @FXML
  public void initialize() {
    colUser.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
        d.getValue().getUserId() != null ? String.valueOf(d.getValue().getUserId()) : "system"));
    colAction.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getAction()));
    colEntity.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEntityType()));
    colDetail.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDetail()));
    colDate.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCreatedAt().toString()));
    loadLogs();
  }

  private void loadLogs() {
    try {
      auditTable.setItems(FXCollections.observableArrayList(auditService.getAll()));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void goBack(ActionEvent e) {
    try {
      SceneManager.navigateTo(e, "/fxml/superadmin/SuperAdminDashboard.fxml");
    } catch (Exception ex) {
      errorLabel.setText(ex.getMessage());
    }
  }
}
