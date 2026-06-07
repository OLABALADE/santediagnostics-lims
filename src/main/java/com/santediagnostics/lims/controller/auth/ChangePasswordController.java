package com.santediagnostics.lims.controller.auth;

import com.santediagnostics.lims.service.AuthService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import com.santediagnostics.lims.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ChangePasswordController {

  @FXML
  private PasswordField newPasswordField;
  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private Label errorLabel;

  private final AuthService authService = new AuthService();

  @FXML
  private void handleChange(ActionEvent event) {
    errorLabel.setText("");
    String pass = newPasswordField.getText();
    String confirm = confirmPasswordField.getText();

    if (!ValidationUtil.isValidPassword(pass)) {
      errorLabel.setText("Password must be at least 8 characters.");
      return;
    }
    if (!pass.equals(confirm)) {
      errorLabel.setText("Passwords do not match.");
      return;
    }
    try {
      authService.changePassword(SessionManager.userId(), pass);
      SessionManager.get().setForcePasswordChange(false);
      switch (SessionManager.get().getRole()) {
        case SUPER_ADMIN -> SceneManager.navigateTo(event, "/fxml/superadmin/SuperAdminDashboard.fxml");
        case LAB_ATTENDANT -> SceneManager.navigateTo(event, "/fxml/attendant/AttendantDashboard.fxml");
        case CUSTOMER -> SceneManager.navigateTo(event, "/fxml/customer/CustomerDashboard.fxml");
      }
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }
}
