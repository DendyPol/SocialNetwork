package ru.polovinko.socialnetwork.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PasswordUtil {
  public static String encodePassword(String password) {
    return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
  }

  public static String decodePassword(String encodePassword) {
    byte[] decodedBytes = Base64.getDecoder().decode(encodePassword);
    return new String(decodedBytes, StandardCharsets.UTF_8);
  }
}
