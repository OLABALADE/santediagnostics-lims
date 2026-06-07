package com.santediagnostics.lims.util;

import com.santediagnostics.lims.model.User;

public class SessionManager {
  private static User currentUser;

  public static void set(User user) {
    currentUser = user;
  }

  public static User get() {
    return currentUser;
  }

  public static void clear() {
    currentUser = null;
  }

  public static int userId() {
    return currentUser.getId();
  }
}
