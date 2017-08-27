package cn.yxffcode.javasegment.core;

import java.util.Map;

/**
 * @author gaohang on 8/26/17.
 */
public interface Executable {
  Object execute(Map<String, Object> context);
}
