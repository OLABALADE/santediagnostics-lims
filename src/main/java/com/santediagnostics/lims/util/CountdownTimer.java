package com.santediagnostics.lims.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CountdownTimer {

  private Timeline timeline;

  /**
   * Updates label every second with remaining time until deadline. Stops when
   * expired.
   */
  public void start(Label label, LocalDateTime deadline) {
    stop();
    timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
      long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), deadline);
      if (seconds <= 0) {
        label.setText("Ready");
        stop();
      } else {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        label.setText(String.format("%02dh %02dm %02ds remaining", h, m, s));
      }
    }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  public void stop() {
    if (timeline != null) {
      timeline.stop();
      timeline = null;
    }
  }
}
