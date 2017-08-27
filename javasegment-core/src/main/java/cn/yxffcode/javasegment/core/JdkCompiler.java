package cn.yxffcode.javasegment.core;

import org.apache.commons.lang3.text.StrBuilder;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author gaohang on 8/27/17.
 */
public class JdkCompiler {

  private JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

  private final CodeSegmentClassLoader classLoader;

  private final List<String> options;

  public JdkCompiler(List<String> options) {
    this.options = options;
    this.classLoader = new CodeSegmentClassLoader();
  }

  public Class<?> compile(JavaSource javaSource) throws Throwable {

    final List<JavaSource> units = Collections.singletonList(javaSource);
    try (InMemoryJavaFileManager javaFileManager = new InMemoryJavaFileManager(
        javac.getStandardFileManager(null, null, null), units)) {

      final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
      final JavaCompiler.CompilationTask task = javac.getTask(null, javaFileManager,
          diagnosticCollector, options, null, units);
      final Boolean compileResult = task.call();
      if (compileResult == null || !compileResult) {
        final List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        final StrBuilder msg = new StrBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
          msg.append(diagnostic.getMessage(Locale.CHINESE)).append(" start at ")
              .append(diagnostic.getStartPosition()).append(" end at ")
              .append(diagnostic.getEndPosition()).append(" ")
              .append(diagnostic.getSource()).append('\n');
        }
        throw new CompileException(msg.toString());
      }
    }
    classLoader.addToClasspath(javaSource);

    return classLoader.loadClass(javaSource.getFullClassName());
  }
}
