package com.santediagnostics.lims.controller.auth;

import com.santediagnostics.lims.model.User;
import com.santediagnostics.lims.service.AuthService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Label errorLabel;

  private final AuthService authService = new AuthService();

  @FXML
  private void handleLogin(ActionEvent event) {
    errorLabel.setText("");
    try {
      User user = authService.login(emailField.getText().trim(), passwordField.getText());
      SessionManager.set(user);

      if (user.isForcePasswordChange()) {
        SceneManager.navigateTo(event, "/fxml/auth/ChangePassword.fxml");
        return;
      }

      switch (user.getRole()) {
        case SUPER_ADMIN -> SceneManager.navigateTo(event, "/fxml/superadmin/SuperAdminDashboard.fxml");
        case LAB_ATTENDANT -> SceneManager.navigateTo(event, "/fxml/attendant/AttendantDashboard.fxml");
        case CUSTOMER -> SceneManager.navigateTo(event, "/fxml/customer/CustomerDashboard.fxml");
      }
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void goToRegister(ActionEvent event) {
    try {
      SceneManager.navigateTo(event, "/fxml/auth/Register.fxml");
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }
}
