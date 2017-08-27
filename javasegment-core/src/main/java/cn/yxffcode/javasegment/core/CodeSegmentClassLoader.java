package cn.yxffcode.javasegment.core;

import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 8/27/17.
 */
public class CodeSegmentClassLoader extends ClassLoader {

  private final ClassLoader parentClassLoader;
  private final ConcurrentMap<String, JavaSource> javaSources = Maps.newConcurrentMap();
  private final ConcurrentMap<String, Class<?>> loadedClasses = Maps.newConcurrentMap();

  public CodeSegmentClassLoader() {
    this(Thread.currentThread().getContextClassLoader());
  }

  public CodeSegmentClassLoader(ClassLoader parentClassLoader) {
    this.parentClassLoader = parentClassLoader;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return loadClass(name, false);
  }

  public void addToClasspath(JavaSource javaSource) {
    checkNotNull(javaSource);
    javaSources.putIfAbsent(javaSource.getFullClassName(), javaSource);
  }

  private void doLoad(String name, boolean resolve) throws ClassNotFoundException {
    final JavaSource javaSource = javaSources.get(name);
    if (javaSource == null) {
      throw new ClassNotFoundException(name);
    }
    if (!loadedClasses.containsKey(name)) {
      synchronized (this) {
        if (!loadedClasses.containsKey(name)) {
          loadedClasses.put(name, loadClass(javaSource, resolve));
        }
      }
    }
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

    if (loadedClasses.containsKey(name)) {
      return loadedClasses.get(name);
    }

    try {
      final Class<?> type = parentClassLoader.loadClass(name);
      if (type != null) {
        return type;
      }
    } catch (ClassNotFoundException e) {
      //ignore
    }

    doLoad(name, resolve);
    return loadedClasses.get(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    return loadClass(name);
  }

  private Class<?> loadClass(JavaSource javaSource, boolean resolve) throws ClassNotFoundException {
    final String fullClassName = javaSource.getFullClassName();

    final byte[] byteCode = javaSource.getByteCode();

    final Class<?> clazz = super.defineClass(fullClassName, byteCode, 0, byteCode.length);
    if (resolve) {
      resolveClass(clazz);
    }
    return clazz;
  }

}
