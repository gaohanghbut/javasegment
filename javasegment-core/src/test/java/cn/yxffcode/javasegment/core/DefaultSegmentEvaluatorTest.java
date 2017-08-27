package cn.yxffcode.javasegment.core;

import cn.yxffcode.javasegment.core.code.CodeSegment;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gaohang on 8/26/17.
 */
public class DefaultSegmentEvaluatorTest {
  @Test
  public void eval() throws Exception {
    final SegmentEvaluator evaluator = new DefaultSegmentEvaluator();

    final CodeSegment seg =
        CodeSegment.fromClasspathTemplate("return i * i;", "/SimpleSegTemplate.vm");

    for (int i = 0; i < 100; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          final Map<String, Object> context = Maps.newHashMap();
          context.put("i", 10);
          final Object value = evaluator.eval(seg, context);
          System.out.println("value = " + value);
        }
      }).start();
    }
    TimeUnit.SECONDS.sleep(10);
  }

}