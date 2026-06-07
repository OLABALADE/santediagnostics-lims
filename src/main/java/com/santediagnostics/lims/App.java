package com.santediagnostics.lims;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/auth/Login.fxml"));
    stage.setTitle("Sante Diagnostics LIMS");
    stage.setScene(new Scene(root, 480, 420));
    stage.setResizable(false);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
