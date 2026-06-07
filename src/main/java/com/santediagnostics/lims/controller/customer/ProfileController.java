package com.santediagnostics.lims.controller.customer;

import com.santediagnostics.lims.service.AuthService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import com.santediagnostics.lims.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfileController {

  @FXML
  private Label nameLabel;
  @FXML
  private Label emailLabel;
  @FXML
  private PasswordField newPasswordField;
  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private Label errorLabel;

  private final AuthService authService = new AuthService();

  @FXML
  public void initialize() {
    nameLabel.setText(SessionManager.get().getName());
    emailLabel.setText(SessionManager.get().getEmail());
  }

  @FXML
  private void handleChangePassword(ActionEvent event) {
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
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Password updated successfully.");
      newPasswordField.clear();
      confirmPasswordField.clear();
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
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
