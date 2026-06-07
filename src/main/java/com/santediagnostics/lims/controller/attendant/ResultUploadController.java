package com.santediagnostics.lims.controller.attendant;

import com.santediagnostics.lims.model.Result;
import com.santediagnostics.lims.model.Sample;
import com.santediagnostics.lims.service.ResultService;
import com.santediagnostics.lims.service.SampleService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;

public class ResultUploadController {

  @FXML
  private ComboBox<Sample> sampleCombo;
  @FXML
  private TextField textValueField;
  @FXML
  private TextField numericValueField;
  @FXML
  private Label fileLabel;
  @FXML
  private Label errorLabel;

  private final SampleService sampleService = new SampleService();
  private final ResultService resultService = new ResultService();
  private String selectedFilePath;

  @FXML
  public void initialize() {
    try {
      sampleCombo.setItems(FXCollections.observableArrayList(sampleService.getAllSamples()));
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleBrowse(ActionEvent event) {
    FileChooser chooser = new FileChooser();
    chooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("PDF/Images", "*.pdf", "*.png", "*.jpg", "*.jpeg"));
    File file = chooser.showOpenDialog(null);
    if (file != null) {
      selectedFilePath = file.getAbsolutePath();
      fileLabel.setText(file.getName());
    }
  }

  @FXML
  private void handleUpload(ActionEvent event) {
    errorLabel.setText("");
    Sample sample = sampleCombo.getValue();
    if (sample == null) {
      errorLabel.setText("Select a sample.");
      return;
    }

    Result r = new Result();
    r.setSampleId(sample.getId());
    r.setTextValue(textValueField.getText().trim());
    r.setFilePath(selectedFilePath);
    String num = numericValueField.getText().trim();
    if (!num.isEmpty()) {
      try {
        r.setNumericValue(new BigDecimal(num));
      } catch (NumberFormatException ex) {
        errorLabel.setText("Invalid numeric value.");
        return;
      }
    }
    try {
      resultService.uploadResult(SessionManager.userId(), r);
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Result uploaded. Pending verification.");
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void handleVerify(ActionEvent event) {
    errorLabel.setText("");
    Sample sample = sampleCombo.getValue();
    if (sample == null) {
      errorLabel.setText("Select a sample.");
      return;
    }
    try {
      Result r = resultService.getResultBySample(sample.getId());
      if (r == null) {
        errorLabel.setText("No result uploaded for this sample.");
        return;
      }
      resultService.verifyResult(SessionManager.userId(), r.getId(), sample.getId());
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Result verified. Customer notified.");
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
