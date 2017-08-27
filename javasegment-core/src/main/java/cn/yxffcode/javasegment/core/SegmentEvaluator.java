package cn.yxffcode.javasegment.core;

import cn.yxffcode.javasegment.core.code.CodeSegment;

import java.util.Map;

/**
 * 代码片断求值
 *
 * @author gaohang on 8/26/17.
 */
public interface SegmentEvaluator {

  /**
   * 执行CodeSegment
   *
   * @param context 代码片断中需要的外界变量
   */
  Object eval(CodeSegment codeSegment, Map<String, Object> context);
}
