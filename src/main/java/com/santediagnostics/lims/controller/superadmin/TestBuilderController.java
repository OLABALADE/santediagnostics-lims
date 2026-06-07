package com.santediagnostics.lims.controller.superadmin;

import com.santediagnostics.lims.model.ResultFormat;
import com.santediagnostics.lims.model.TestType;
import com.santediagnostics.lims.service.TestService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;

public class TestBuilderController {

  @FXML
  private TableView<TestType> testTable;
  @FXML
  private TableColumn<TestType, String> colName;
  @FXML
  private TableColumn<TestType, BigDecimal> colPrice;
  @FXML
  private TableColumn<TestType, Integer> colTat;
  @FXML
  private TableColumn<TestType, String> colFormat;
  @FXML
  private TableColumn<TestType, Boolean> colActive;

  @FXML
  private TextField nameField;
  @FXML
  private TextField priceField;
  @FXML
  private TextField tatField;
  @FXML
  private ComboBox<ResultFormat> formatCombo;
  @FXML
  private Label errorLabel;

  private final TestService testService = new TestService();

  @FXML
  public void initialize() {
    formatCombo.setItems(FXCollections.observableArrayList(ResultFormat.values()));
    colName.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
    colPrice.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getPrice()));
    colTat
        .setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getTurnaroundHours()));
    colFormat.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getResultFormat().name()));
    colActive.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().isActive()));
    loadTests();
  }

  private void loadTests() {
    try {
      testTable.setItems(FXCollections.observableArrayList(testService.getAllTests()));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleSave(ActionEvent event) {
    errorLabel.setText("");
    try {
      TestType t = new TestType();
      t.setName(nameField.getText().trim());
      t.setPrice(new BigDecimal(priceField.getText().trim()));
      t.setTurnaroundHours(Integer.parseInt(tatField.getText().trim()));
      t.setResultFormat(formatCombo.getValue());
      t.setActive(true);

      TestType selected = testTable.getSelectionModel().getSelectedItem();
      if (selected != null) {
        t.setId(selected.getId());
        testService.updateTestType(SessionManager.userId(), t);
      } else {
        testService.createTestType(SessionManager.userId(), t);
      }
      loadTests();
      clearForm();
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleSelect() {
    TestType t = testTable.getSelectionModel().getSelectedItem();
    if (t == null)
      return;
    nameField.setText(t.getName());
    priceField.setText(t.getPrice().toPlainString());
    tatField.setText(String.valueOf(t.getTurnaroundHours()));
    formatCombo.setValue(t.getResultFormat());
  }

  private void clearForm() {
    nameField.clear();
    priceField.clear();
    tatField.clear();
    formatCombo.setValue(null);
    testTable.getSelectionModel().clearSelection();
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
