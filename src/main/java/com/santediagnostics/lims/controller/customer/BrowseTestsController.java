package com.santediagnostics.lims.controller.customer;

import com.santediagnostics.lims.model.TestType;
import com.santediagnostics.lims.service.TestService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BrowseTestsController {

  @FXML
  private TableView<TestType> testTable;
  @FXML
  private TableColumn<TestType, String> colName;
  @FXML
  private TableColumn<TestType, String> colPrice;
  @FXML
  private TableColumn<TestType, Integer> colTat;
  @FXML
  private TableColumn<TestType, String> colFormat;
  @FXML
  private Label bankDetailsLabel;
  @FXML
  private Label errorLabel;

  private final TestService testService = new TestService();

  @FXML
  public void initialize() {
    colName.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
    colPrice.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty("₦" + d.getValue().getPrice().toPlainString()));
    colTat
        .setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getTurnaroundHours()));
    colFormat.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getResultFormat().name()));
    bankDetailsLabel.setVisible(false);
    try {
      testTable.setItems(FXCollections.observableArrayList(testService.getActiveTests()));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleOrder(ActionEvent event) {
    TestType selected = testTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      errorLabel.setText("Select a test to order.");
      return;
    }
    try {
      testService.placeOrder(SessionManager.userId(), selected.getId());
      bankDetailsLabel.setVisible(true);
      bankDetailsLabel.setText(
          "Order placed!\nBank: Sante Diagnostics Ltd\nAccount: 1234567890\nBank: First Bank Nigeria\nAmount: ₦"
              + selected.getPrice().toPlainString());
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Order placed. Please make payment and await confirmation.");
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
