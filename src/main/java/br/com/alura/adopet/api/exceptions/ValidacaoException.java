package br.com.alura.adopet.api.exceptions;

public class ValidacaoException extends RuntimeException {

  private String message;

  public ValidacaoException(String message) {
    super(message);
  }
}
