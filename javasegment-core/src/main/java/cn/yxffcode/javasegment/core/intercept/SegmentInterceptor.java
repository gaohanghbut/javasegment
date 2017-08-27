package cn.yxffcode.javasegment.core.intercept;

import cn.yxffcode.javasegment.core.Executable;
import cn.yxffcode.javasegment.core.JavaSource;
import cn.yxffcode.javasegment.core.code.CodeSegment;

/**
 * @author gaohang on 8/26/17.
 */
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
