package com.iluwatar.gatekeeper;

import lombok.Getter;

@Getter
public class LoginRequst extends Request {
  private String account;
  private String password;

  /**
   * Construction method.
   * @param action the action of the request, such as :"login"
   * @param account the account of the login request.
   * @param password the password of the login request.
   */
  public LoginRequst(String action, String account, String password) {
    super(action);
    this.account = account;
    this.password = password;
  }
}
