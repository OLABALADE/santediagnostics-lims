package com.santediagnostics.lims.controller.customer;

import com.santediagnostics.lims.model.TestRequest;
import com.santediagnostics.lims.model.TestType;
import com.santediagnostics.lims.service.TestService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class OrderSummaryController {

  @FXML
  private TableView<TestRequest> orderTable;
  @FXML
  private TableColumn<TestRequest, String> colTest;
  @FXML
  private TableColumn<TestRequest, String> colPayment;
  @FXML
  private TableColumn<TestRequest, String> colDate;
  @FXML
  private Label totalLabel;
  @FXML
  private Label errorLabel;

  private final TestService testService = new TestService();

  @FXML
  public void initialize() {
    colTest.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTestTypeName()));
    colPayment.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPaymentStatus().name()));
    colDate.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getRequestedAt().toLocalDate().toString()));
    loadOrders();
  }

  private void loadOrders() {
    try {
      var orders = testService.getCustomerRequests(SessionManager.userId());
      orderTable.setItems(FXCollections.observableArrayList(orders));

      // Sum prices for paid orders
      double total = 0;
      for (TestRequest r : orders) {
        TestType t = testService.getAllTests().stream()
            .filter(tt -> tt.getId() == r.getTestTypeId()).findFirst().orElse(null);
        if (t != null)
          total += t.getPrice().doubleValue();
      }
      totalLabel.setText(String.format("Total spent: ₦%.2f", total));
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
