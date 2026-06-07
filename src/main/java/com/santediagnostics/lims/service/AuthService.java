package com.santediagnostics.lims.service;

import com.santediagnostics.lims.dao.UserDAO;
import com.santediagnostics.lims.model.Role;
import com.santediagnostics.lims.model.User;
import com.santediagnostics.lims.util.PasswordUtil;

import java.util.UUID;

public class AuthService {

  private final UserDAO userDAO = new UserDAO();
  private final AuditService auditService = new AuditService();
  private final EmailService emailService = new EmailService();

  public User login(String email, String password) throws Exception {
    User user = userDAO.findByEmail(email);
    if (user == null)
      throw new Exception("Invalid email or password.");
    if (!user.isEmailVerified())
      throw new Exception("Please verify your email before logging in.");
    if (!PasswordUtil.verify(password, user.getPasswordHash()))
      throw new Exception("Invalid email or password.");
    auditService.log(user.getId(), "LOGIN", "users", user.getId(), null);
    return user;
  }

  public void registerCustomer(String name, String email, String password) throws Exception {
    if (userDAO.findByEmail(email) != null)
      throw new Exception("Email already registered.");
    User u = new User();
    u.setName(name);
    u.setEmail(email);
    u.setPasswordHash(PasswordUtil.hash(password));
    u.setRole(Role.CUSTOMER);
    u.setEmailVerified(false);
    u.setForcePasswordChange(false);
    u.setVerificationToken(UUID.randomUUID().toString());
    int id = userDAO.insert(u);
    emailService.sendVerification(email, name, u.getVerificationToken());
    auditService.log(null, "REGISTER", "users", id, email);
  }

  public void verifyEmail(String token) throws Exception {
    User u = userDAO.findByVerificationToken(token);
    if (u == null)
      throw new Exception("Invalid or expired verification token.");
    userDAO.verifyEmail(u.getId());
    auditService.log(u.getId(), "EMAIL_VERIFIED", "users", u.getId(), null);
  }

  public User createStaffAccount(int creatorId, String name, String email, Role role) throws Exception {
    if (userDAO.findByEmail(email) != null)
      throw new Exception("Email already registered.");
    User u = new User();
    u.setName(name);
    u.setEmail(email);
    u.setPasswordHash(PasswordUtil.hash(UUID.randomUUID().toString())); // temp password
    u.setRole(role);
    u.setEmailVerified(true);
    u.setForcePasswordChange(true);
    u.setVerificationToken(UUID.randomUUID().toString());
    int id = userDAO.insert(u);
    emailService.sendStaffWelcome(email, name, u.getVerificationToken());
    auditService.log(creatorId, "CREATE_USER", "users", id, role.name());
    return u;
  }

  public void changePassword(int userId, String newPassword) throws Exception {
    String hash = PasswordUtil.hash(newPassword);
    userDAO.updatePassword(userId, hash, false);
    auditService.log(userId, "CHANGE_PASSWORD", "users", userId, null);
  }
}
