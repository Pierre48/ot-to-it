package com.cgi.connect.converter;

import java.util.HashSet;
import java.util.Set;

public class TreeElement {

  private TreeElement parent;
  private final String id;
  private String type;
  private int depth = 0;
  private final Set<TreeElement> children = new HashSet<>();

  public void setParent(TreeElement parent) {
    this.parent = parent;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public static TreeElement of(String id) {
    return new TreeElement(id);
  }

  private TreeElement(String id) {
    this.id = id;
  }

  public void addChild(TreeElement child) {
    this.children.add(child);
  }

  public boolean isRoot() {
    return parent == null;
  }

  public TreeElement getParent() {
    return parent;
  }

  public Set<TreeElement> getChildren() {
    return this.children;
  }

  public static TreeElement getAncestor(TreeElement child, int depth) {
    if (depth > child.getDepth() || depth < 0) {
      throw new IllegalStateException(
          child.getId()
              + " field isn't deep ("
              + child.getDepth()
              + ") enought for this request ("
              + depth
              + ")");
    }

    if (depth == child.getDepth()) {
      return child;
    }

    if (child.parent == null) {
      throw new IllegalStateException(child.getId() + " field is misconfigured");
    }

    return getAncestor(child.getParent(), depth);
  }
}
