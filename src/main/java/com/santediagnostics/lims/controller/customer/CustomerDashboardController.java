package com.santediagnostics.lims.controller.customer;

import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomerDashboardController {

  @FXML
  private Label welcomeLabel;

  @FXML
  public void initialize() {
    welcomeLabel.setText("Welcome, " + SessionManager.get().getName());
  }

  @FXML
  private void goToBrowseTests(ActionEvent e) {
    nav(e, "/fxml/customer/BrowseTests.fxml");
  }

  @FXML
  private void goToResultVault(ActionEvent e) {
    nav(e, "/fxml/customer/ResultVault.fxml");
  }

  @FXML
  private void goToProfile(ActionEvent e) {
    nav(e, "/fxml/customer/Profile.fxml");
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
