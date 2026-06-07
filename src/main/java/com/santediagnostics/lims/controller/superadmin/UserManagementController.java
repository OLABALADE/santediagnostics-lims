package com.santediagnostics.lims.controller.superadmin;

import com.santediagnostics.lims.model.Role;
import com.santediagnostics.lims.service.AuthService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import com.santediagnostics.lims.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserManagementController {

  @FXML
  private TextField nameField;
  @FXML
  private TextField emailField;
  @FXML
  private ComboBox<Role> roleCombo;
  @FXML
  private Label errorLabel;

  private final AuthService authService = new AuthService();

  @FXML
  public void initialize() {
    roleCombo.getItems().addAll(Role.LAB_ATTENDANT, Role.CUSTOMER);
  }

  @FXML
  private void handleCreate(ActionEvent event) {
    errorLabel.setText("");
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    Role role = roleCombo.getValue();

    if (name.isEmpty() || email.isEmpty() || role == null) {
      errorLabel.setText("All fields required.");
      return;
    }
    if (!ValidationUtil.isValidEmail(email)) {
      errorLabel.setText("Invalid email.");
      return;
    }
    try {
      authService.createStaffAccount(SessionManager.userId(), name, email, role);
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Account created. Welcome email sent.");
      nameField.clear();
      emailField.clear();
      roleCombo.setValue(null);
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
