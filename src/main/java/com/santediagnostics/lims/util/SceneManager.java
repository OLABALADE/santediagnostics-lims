package com.santediagnostics.lims.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

  public static void navigateTo(Stage stage, String fxmlPath) throws Exception {
    Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
    stage.setScene(new Scene(root));
    stage.show();
  }

  public static void navigateTo(javafx.event.ActionEvent event, String fxmlPath) throws Exception {
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    navigateTo(stage, fxmlPath);
  }
}
