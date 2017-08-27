package cn.yxffcode.javasegment.core;

import cn.yxffcode.javasegment.core.code.CodeSegment;
import cn.yxffcode.javasegment.core.intercept.SegmentInterceptor;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 8/26/17.
 */
public class DefaultSegmentEvaluator implements SegmentEvaluator {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSegmentEvaluator.class);

  private final List<SegmentInterceptor> segmentInterceptors;

  private final JdkCompiler compiler = new JdkCompiler(Collections.emptyList());

  private final ConcurrentMap<CodeSegment, Executable> codeCache = Maps.newConcurrentMap();

  public DefaultSegmentEvaluator() {
    this(Collections.emptyList());
  }

  public DefaultSegmentEvaluator(List<SegmentInterceptor> segmentInterceptors) {
    this.segmentInterceptors = segmentInterceptors == null ? Collections.emptyList()
        : segmentInterceptors;
  }

  @Override
  public Object eval(CodeSegment codeSegment, Map<String, Object> context) {
    checkNotNull(codeSegment);

    final Executable executable = codeCache.get(codeSegment);

    if (executable != null) {
      LOGGER.debug("invoke executable in cache\n:{}", codeSegment.code());
      return invokePostEvalResult(executable.execute(context));
    }

    synchronized (this) {
      if (!codeCache.containsKey(codeSegment)) {
        invokeBeforeSource(codeSegment);

        final JavaSource javaSource = JavaSourceBuilder.build(codeSegment, context);
        invokePostSource(javaSource);

        LOGGER.debug("to compile source\n:{}", javaSource.getClassContent());
        final Class<?> clazz = doCompile(javaSource);

        final Executable codeSegObject = newExecutable(clazz);
        codeCache.put(codeSegment, invokePostExecutable(codeSegObject));
      }
    }
    //执行代码
    LOGGER.debug("invoke executable after compiled\n:{}", codeSegment.code());
    return invokePostEvalResult(codeCache.get(codeSegment).execute(context));
  }

  private Executable newExecutable(Class<?> type) {
    Executable codeSegObject;
    try {
      codeSegObject = (Executable) type.newInstance();
    } catch (Exception e) {
      throw new EvaluateException(e);
    }
    return codeSegObject;
  }

  private Object invokePostEvalResult(Object result) {
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        result = segmentInterceptor.postEvalResult(result);
      }
    }
    return result;
  }

  private Executable invokePostExecutable(Executable codeSegObject) {
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        codeSegObject = segmentInterceptor.postExecutable(codeSegObject);
      }
    }
    return codeSegObject;
  }

  private Class<?> doCompile(JavaSource javaSource) {
    try {
      return compiler.compile(javaSource);
    } catch (CompileException e) {
      throw e;
    } catch (Throwable throwable) {
      throw new CompileException(throwable);
    }
  }

  private void invokePostSource(JavaSource javaSource) {
    JavaSource js = javaSource;
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        js = segmentInterceptor.postJavaSource(js);
      }
    }
  }

  private void invokeBeforeSource(CodeSegment codeSegment) {
    CodeSegment seg = codeSegment;
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        seg = segmentInterceptor.beforeJavaSource(seg);
      }
    }
  }
}
