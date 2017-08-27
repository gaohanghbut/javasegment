package cn.yxffcode.javasegment.core;

import cn.yxffcode.javasegment.core.code.CodeSegment;
import cn.yxffcode.javasegment.core.io.StrWriter;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 8/26/17.
 */
public class JavaSourceBuilder {

  private static final String DEFAULT_PACKAGE = "javasegment.gen";

  private static final AtomicInteger CLASS_NAME_NUM = new AtomicInteger();

  private static final String CLASS_NAME_PREFIX = "JavasegmentGen_";

  private static final Splitter STATEMENT_SPLITTER = Splitter.on(';').trimResults();
  private static final Joiner STATEMENT_JOINER = Joiner.on(";\n");

  private static final VelocityEngine VE = new VelocityEngine();

  public static JavaSource build(CodeSegment codeSegment, Map<String, Object> context) {
    checkNotNull(codeSegment);
    codeSegment.classTemplate();

    final String code = codeSegment.code();

    final Iterable<String> statements = STATEMENT_SPLITTER.split(code);
    final List<String> imports = Lists.newArrayList();
    final List<String> states = Lists.newArrayList();
    for (String statement : statements) {
      if (statement.startsWith("import")) {
        imports.add(statement);
      } else {
        states.add(statement);
      }
    }

    final Map<String, Object> ctx = Maps.newHashMap(context);
    if (!ctx.containsKey("className")) {
      final String className = CLASS_NAME_PREFIX + CLASS_NAME_NUM.getAndIncrement();
      ctx.put("className", className);
    }
    if (!ctx.containsKey("packageName")) {
      ctx.put("packageName", DEFAULT_PACKAGE);
    }

    ctx.put("imports", STATEMENT_JOINER.join(imports));
    ctx.put("expression", STATEMENT_JOINER.join(states));

    final VelocityContext vcontext = new VelocityContext(ctx);

    try (Writer out = new StrWriter()) {
      VE.evaluate(vcontext, out, "javasegmentGen", codeSegment.classTemplate());
      return new JavaSource(String.valueOf(ctx.get("className")),
          String.valueOf(ctx.get("packageName")), out.toString());
    } catch (IOException e) {
      throw new ReadClassTemplateException(e);
    }
  }
}
