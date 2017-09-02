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

## 使用拦截器
javasegment提供了SegmentInterceptor接口和SegmentInterceptorAdapter类，可对代码
片断处理的各种阶段做拦截。例如在代码片断中加入特殊符号，可使用SegmentInterceptor对特征
符号做处理，将特征符号相关的代码转换成java代码:
```java
public class PhraseParser extends SegmentInterceptorAdapter {
  @Override
  public CodeSegment beforeJavaSource(CodeSegment segment) {
    final String code = segment.code();

    //在代码片断中找特殊符号
    final Iterable<String> tokens = token(code);
    for (String token : tokens) {
      //处理特征符号的转换
      ...
    }

    return CodeSegment.fromStringTemplate(finalCode, segment.classTemplate());
  }
}

```
SegmentInterceptor接口的定义如下，每个方法用于拦截代码片断处理的一个步骤：
```java
public interface SegmentInterceptor {
  /**
   * 转换成JavaSource前调用
   */
  CodeSegment beforeJavaSource(final CodeSegment segment);

  /**
   * 转换成JavaSource后调用
   */
  JavaSource postJavaSource(final JavaSource javaSource);

  /**
   * 处理由CodeSegment创建的对象
   */
  Executable postExecutable(final Executable obj);

  /**
   * 处理代码片断的执行结果
   */
  Object postEvalResult(final Object result);
}
```