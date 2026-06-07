package com.santediagnostics.lims.util;

import java.util.regex.Pattern;

public class ValidationUtil {
  private static final Pattern EMAIL = Pattern.compile("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$");

  public static boolean isValidEmail(String email) {
    return email != null && EMAIL.matcher(email).matches();
  }

  public static boolean isValidPassword(String password) {
    return password != null && password.length() >= 8;
  }
}
