package cn.yxffcode.javasegment.core;

import com.google.common.base.MoreObjects;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author gaohang on 8/26/17.
 */
public class JavaSource extends SimpleJavaFileObject {

  private ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

  private final String className;
  private final String packageName;
  private final String classContent;
  private final String fullClassName;

  public JavaSource(String className, String packageName, String classContent) {
    super(URI.create("string:///" + packageName.replace('.', '/')
        + '/' + className + Kind.SOURCE.extension), Kind.SOURCE);

    this.className = className;
    this.packageName = packageName;
    this.classContent = classContent;
    this.fullClassName = packageName + '.' + className;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    return classContent;
  }

  public String getClassName() {
    return className;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getClassContent() {
    return classContent;
  }

  public String getFullClassName() {
    return fullClassName;
  }

  @Override
  public InputStream openInputStream() throws IOException {
    return new ByteArrayInputStream(getByteCode());
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    return byteCode;
  }

  public byte[] getByteCode() {
    return byteCode.toByteArray();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("className", className)
        .add("packageName", packageName)
        .add("classContent", classContent)
        .toString();
  }
}
