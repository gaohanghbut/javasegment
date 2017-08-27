package cn.yxffcode.javasegment.core;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author gaohang on 8/27/17.
 */
public class InMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
  private final List<JavaSource> units;

  public InMemoryJavaFileManager(JavaFileManager fileManager, List<JavaSource> units) {
    super(fileManager);
    this.units = units;
  }

  @Override
  public JavaSource getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
    return findByName(className);
  }

  private JavaSource findByName(String className) {
    for (JavaSource unit : units) {
      if (Objects.equals(className, unit.getFullClassName())) {
        return unit;
      }
    }
    return null;
  }

  @Override
  public JavaSource getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
    return findByName(className);
  }

  @Override
  public JavaSource getFileForInput(Location location, String packageName, String relativeName) throws IOException {
    return findByName(packageName + '.' + relativeName);
  }

  @Override
  public JavaSource getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
    return findByName(packageName + '.' + relativeName);
  }
}
