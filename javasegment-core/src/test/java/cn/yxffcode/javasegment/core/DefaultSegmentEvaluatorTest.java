package cn.yxffcode.javasegment.core;

import cn.yxffcode.javasegment.core.code.CodeSegment;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author gaohang on 8/26/17.
 */
public class DefaultSegmentEvaluatorTest {
  @Test
  public void eval() throws Exception {
    final SegmentEvaluator evaluator = new DefaultSegmentEvaluator();

    final CodeSegment seg =
        CodeSegment.fromClassContent(
            "package $packageName;\n" +
                "import cn.yxffcode.javasegment.core.Executable;\n" +
                "import java.util.*;\n" +
                "public class $className implements Executable {\n" +
                "    public Object execute(Map<String, Object> context) {\n" +
                "        Integer i = (Integer) context.get(\"i\");\n" +
                "        return i * i;\n" +
                "    }\n" +
                "}");

    final Map<String, Object> context = Maps.newHashMap();
    context.put("i", 10);
    final Object value = evaluator.eval(seg, context);
    System.out.println("value = " + value);
  }

}