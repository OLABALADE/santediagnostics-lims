package com.santediagnostics.lims.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {

  private static final String UPLOAD_DIR = System.getProperty("user.home") + "/santediagnostics-lims/results/";

  static {
    new File(UPLOAD_DIR).mkdirs();
  }

  public static String saveFile(File source) throws IOException {
    String dest = UPLOAD_DIR + System.currentTimeMillis() + "_" + source.getName();
    Files.copy(source.toPath(), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
    return dest;
  }

  public static void openFile(String path) throws IOException {
    java.awt.Desktop.getDesktop().open(new File(path));
  }

  public static boolean exists(String path) {
    return path != null && new File(path).exists();
  }
}
