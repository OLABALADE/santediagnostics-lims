package com.santediagnostics.lims.controller.attendant;

import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AttendantDashboardController {

    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, " + SessionManager.get().getName());
    }

    @FXML private void goToQueue(ActionEvent e)          { nav(e, "/fxml/attendant/AttendantQueue.fxml"); }
    @FXML private void goToSampleTracking(ActionEvent e) { nav(e, "/fxml/attendant/SampleTracking.fxml"); }
    @FXML private void goToResultUpload(ActionEvent e)   { nav(e, "/fxml/attendant/ResultUpload.fxml"); }

    @FXML
    private void handleLogout(ActionEvent e) {
        SessionManager.clear();
        nav(e, "/fxml/auth/Login.fxml");
    }

    private void nav(ActionEvent e, String path) {
        try { SceneManager.navigateTo(e, path); } catch (Exception ex) { ex.printStackTrace(); }
    }
}
