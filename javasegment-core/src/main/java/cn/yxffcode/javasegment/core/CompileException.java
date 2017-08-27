package cn.yxffcode.javasegment.core;

/**
 * @author gaohang on 8/26/17.
 */
public class CompileException extends RuntimeException {
  public CompileException(Throwable cause) {
    super(cause);
  }

  public CompileException(String message) {
    super(message);
  }
}
