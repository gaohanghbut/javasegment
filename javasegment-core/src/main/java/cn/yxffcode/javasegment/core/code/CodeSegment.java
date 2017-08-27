package cn.yxffcode.javasegment.core.code;

/**
 * 表示代码片断
 *
 * @author gaohang on 8/26/17.
 */
public interface CodeSegment {

  /**
   * @return 代码片断内容
   */
  String code();

  /**
   * @return 对应的Java类模板内容
   */
  String classTemplate();
}
