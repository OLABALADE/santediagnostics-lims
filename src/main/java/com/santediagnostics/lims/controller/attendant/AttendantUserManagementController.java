package com.santediagnostics.lims.controller.attendant;

import com.santediagnostics.lims.model.Role;
import com.santediagnostics.lims.service.AuthService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import com.santediagnostics.lims.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AttendantUserManagementController {

  @FXML
  private TextField nameField;
  @FXML
  private TextField emailField;
  @FXML
  private Label errorLabel;

  private final AuthService authService = new AuthService();

  @FXML
  private void handleCreate(ActionEvent event) {
    errorLabel.setText("");
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();

    if (name.isEmpty() || email.isEmpty()) {
      errorLabel.setText("All fields required.");
      return;
    }
    if (!ValidationUtil.isValidEmail(email)) {
      errorLabel.setText("Invalid email.");
      return;
    }
    try {
      authService.createStaffAccount(SessionManager.userId(), name, email, Role.CUSTOMER);
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Customer account created. Welcome email sent.");
      nameField.clear();
      emailField.clear();
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
