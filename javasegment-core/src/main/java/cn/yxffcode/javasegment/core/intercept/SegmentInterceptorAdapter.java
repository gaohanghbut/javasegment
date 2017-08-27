package cn.yxffcode.javasegment.core.intercept;

import cn.yxffcode.javasegment.core.Executable;
import cn.yxffcode.javasegment.core.JavaSource;
import cn.yxffcode.javasegment.core.code.CodeSegment;

/**
 * @author gaohang on 8/26/17.
 */
public class SegmentInterceptorAdapter implements SegmentInterceptor {
  @Override
  public CodeSegment beforeJavaSource(CodeSegment segment) {
    return segment;
  }

  @Override
  public void postJavaSource(JavaSource javaSource) {
  }

  @Override
  public Executable postExecutable(Executable obj) {
    return obj;
  }

  @Override
  public Object postEvalResult(Object result) {
    return result;
  }
}
