package com.santediagnostics.lims.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.InputStream;
import java.util.Properties;

public class EmailService {

  private final Properties smtpProps = new Properties();
  private final String from;
  private final String appUrl;

  public EmailService() {
    try (InputStream in = getClass().getResourceAsStream("/config/smtp.properties")) {
      smtpProps.load(in);
      from = smtpProps.getProperty("smtp.from");
      appUrl = smtpProps.getProperty("app.url");
    } catch (Exception e) {
      throw new RuntimeException("Failed to load SMTP config", e);
    }
  }

  private Session buildSession() {
    Properties p = new Properties();
    p.put("mail.smtp.host", smtpProps.getProperty("smtp.host"));
    p.put("mail.smtp.port", smtpProps.getProperty("smtp.port"));
    p.put("mail.smtp.auth", "true");
    p.put("mail.smtp.starttls.enable", "true");
    String user = smtpProps.getProperty("smtp.user");
    String pass = smtpProps.getProperty("smtp.password");
    return Session.getInstance(p, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pass);
      }
    });
  }

  private void send(String to, String subject, String body) throws Exception {
    Message msg = new MimeMessage(buildSession());
    msg.setFrom(new InternetAddress(from));
    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
    msg.setSubject(subject);
    msg.setText(body);
    Transport.send(msg);
  }

  public void sendVerification(String to, String name, String token) throws Exception {
    send(to, "Verify your Sante Diagnostics account",
        "Hello " + name + ",\n\nYour verification token is:\n\n" + token +
            "\n\nEnter this token in the app under Verify Email.\n\nSante Diagnostics Ltd");
  }

  public void sendStaffWelcome(String to, String name, String token) throws Exception {
    send(to, "Your Sante Diagnostics account",
        "Hello " + name + ",\n\nYour account has been created.\nYour password reset token is:\n\n" + token +
            "\n\nEnter this in the app under Verify Email to set your password.\n\nSante Diagnostics Ltd");
  }

  public void sendResultReady(String to, String name, String testName) throws Exception {
    send(to, "Your result is ready — " + testName,
        "Hello " + name + ",\n\nYour result for " + testName
            + " is ready. Log in to view it.\n\nSante Diagnostics Ltd");
  }
}
