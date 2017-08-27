# javasegment
javasegment是一个处理java代码片断的程序库，可将java代码片断按照定义好的代码模板生成java类，并编译执行

## 使用示例
将代码模板用vm表示，例如代码模板SimpleSegTemplate.vm:
```java
package $packageName;

import cn.yxffcode.javasegment.core.Executable;
import java.util.*;

public class $className implements Executable {

    public Object execute(Map<String, Object> context) {
        Integer i = (Integer) context.get("i");
        $expression
    }
}
```
使用：
```java
public class DefaultSegmentEvaluatorTest {
  @Test
  public void eval() throws Exception {
    final SegmentEvaluator evaluator = new DefaultSegmentEvaluator();

    final CodeSegment seg =
      CodeSegment.fromClasspathTemplate("return i * i;", "/SimpleSegTemplate.vm");

      final Map<String, Object> context = Maps.newHashMap();
      context.put("i", 10);
      final Object value = evaluator.eval(seg, context);
      System.out.println("value = " + value);
  }

}
```

对于简单的模板，可以直接使用字符串：
```java
public class DefaultSegmentEvaluatorTest {
  @Test
  public void eval() throws Exception {
    final SegmentEvaluator evaluator = new DefaultSegmentEvaluator();

    final CodeSegment seg =
      CodeSegment.fromStringTemplate("return i * i;",
        "package $packageName;\n" +
        "import cn.yxffcode.javasegment.core.Executable;\n" +
        "import java.util.*;\n" +
        "public class $className implements Executable {\n" +
        "    public Object execute(Map<String, Object> context) {\n" +
        "        Integer i = (Integer) context.get(\"i\");\n" +
        "        $expression\n" +
        "    }\n" +
        "}");

      final Map<String, Object> context = Maps.newHashMap();
      context.put("i", 10);
      final Object value = evaluator.eval(seg, context);
      System.out.println("value = " + value);
  }

}
```

## 直接使用java类
```java
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
```