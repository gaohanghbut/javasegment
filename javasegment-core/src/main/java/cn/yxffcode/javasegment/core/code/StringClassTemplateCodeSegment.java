package cn.yxffcode.javasegment.core.code;

/**
 * @author gaohang on 8/26/17.
 */
public class StringClassTemplateCodeSegment extends AbstractCodeSegment {

  private String classTemplate;

  public StringClassTemplateCodeSegment(final String code, final String classTemplate) {
    super(code);
    this.classTemplate = classTemplate;
  }

  protected StringClassTemplateCodeSegment(String code) {
    super(code);
  }

  protected void setClassTemplate(String classTemplate) {
    this.classTemplate = classTemplate;
  }

  @Override
  public String classTemplate() {
    return classTemplate;
  }
}
