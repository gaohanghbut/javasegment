package cn.yxffcode.javasegment.core.intercept;

import cn.yxffcode.javasegment.core.Executable;
import cn.yxffcode.javasegment.core.JavaSource;
import cn.yxffcode.javasegment.core.code.CodeSegment;

/**
 * @author gaohang on 8/26/17.
 */
public interface SegmentInterceptor {
  CodeSegment beforeJavaSource(final CodeSegment segment);

  void postJavaSource(final JavaSource javaSource);

  /**
   * 处理由CodeSegment创建的对象
   */
  Executable postExecutable(final Executable obj);

  /**
   * 处理代码片断的执行结果
   */
  Object postEvalResult(final Object result);
}
