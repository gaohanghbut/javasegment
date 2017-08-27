package cn.yxffcode.javasegment.core.code;

/**
 * @author gaohang on 8/26/17.
 */
public abstract class AbstractCodeSegment implements CodeSegment {

  private final String code;

  /**
   * 通过代码版本构造对象
   */
  protected AbstractCodeSegment(String code) {
    this.code = code;
  }

  @Override
  public String code() {
    return code;
  }
}
