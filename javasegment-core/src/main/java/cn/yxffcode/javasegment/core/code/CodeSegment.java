package cn.yxffcode.javasegment.core.code;

import cn.yxffcode.javasegment.core.ReadClassTemplateException;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 表示代码片断
 *
 * @author gaohang on 8/26/17.
 */
public abstract class CodeSegment {

  public static CodeSegment fromStringTemplate(String code, String template) {
    return new StringClassTemplateCodeSegment(code, template);
  }

  public static CodeSegment fromClasspathTemplate(String code, String templatePath) {
    return new ClasspathFileClassTemplateCodeSegment(code, templatePath);
  }

  public static CodeSegment fromClassContent(String classContent) {
    return new ClassContent(classContent);
  }

  private final String code;

  private final boolean needRend;

  /**
   * 通过代码版本构造对象
   */
  protected CodeSegment(String code, boolean needRend) {
    this.code = code;
    this.needRend = needRend;
  }

  public boolean isNeedRend() {
    return needRend;
  }

  /**
   * @return 代码片断内容
   */
  public String code() {
    return code;
  }

  /**
   * @return 对应的Java类模板内容
   */
  public abstract String classTemplate();

  private static final class ClasspathFileClassTemplateCodeSegment extends StringClassTemplateCodeSegment {
    public ClasspathFileClassTemplateCodeSegment(String code, String classpath) {
      super(code);

      try (final Reader reader = new InputStreamReader(
          ClasspathFileClassTemplateCodeSegment.class.getResourceAsStream(classpath))) {
        setClassTemplate(CharStreams.toString(reader));
      } catch (IOException e) {
        throw new ReadClassTemplateException(e);
      }
    }
  }

  private static class StringClassTemplateCodeSegment extends CodeSegment {

    private String classTemplate;

    public StringClassTemplateCodeSegment(final String code, final String classTemplate) {
      super(code, true);
      this.classTemplate = classTemplate;
    }

    protected StringClassTemplateCodeSegment(String code) {
      super(code, true);
    }

    protected void setClassTemplate(String classTemplate) {
      this.classTemplate = classTemplate;
    }

    @Override
    public String classTemplate() {
      return classTemplate;
    }
  }

  private static class ClassContent extends CodeSegment {

    protected ClassContent(String code) {
      super(code, false);
    }

    @Override
    public String classTemplate() {
      return "";
    }
  }

}
