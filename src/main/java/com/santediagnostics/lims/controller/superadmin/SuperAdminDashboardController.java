package com.santediagnostics.lims.controller.superadmin;

import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SuperAdminDashboardController {

  @FXML
  private Label welcomeLabel;

  @FXML
  public void initialize() {
    welcomeLabel.setText("Welcome, " + SessionManager.get().getName());
  }

  @FXML
  private void goToTestBuilder(ActionEvent e) {
    nav(e, "/fxml/superadmin/TestBuilder.fxml");
  }

  @FXML
  private void goToRequestQueue(ActionEvent e) {
    nav(e, "/fxml/superadmin/TestRequestQueue.fxml");
  }

  @FXML
  private void goToUserManagement(ActionEvent e) {
    nav(e, "/fxml/superadmin/UserManagement.fxml");
  }

  @FXML
  private void goToAuditTrail(ActionEvent e) {
    nav(e, "/fxml/superadmin/AuditTrail.fxml");
  }

  @FXML
  private void handleLogout(ActionEvent e) {
    SessionManager.clear();
    nav(e, "/fxml/auth/Login.fxml");
  }

  private void nav(ActionEvent e, String path) {
    try {
      SceneManager.navigateTo(e, path);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
