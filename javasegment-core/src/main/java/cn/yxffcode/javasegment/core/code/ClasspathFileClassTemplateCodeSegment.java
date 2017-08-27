package cn.yxffcode.javasegment.core.code;

import cn.yxffcode.javasegment.core.ReadClassTemplateException;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author gaohang on 8/26/17.
 */
public class ClasspathFileClassTemplateCodeSegment extends StringClassTemplateCodeSegment {
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
