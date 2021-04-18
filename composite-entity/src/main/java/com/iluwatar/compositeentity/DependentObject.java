package com.iluwatar.compositeentity;

/**
 * It is an object, which can contain other dependent objects (there may be a tree of objects within
 * the composite entity), that depends on the coarse-grained object and has its life cycle managed
 * by the coarse-grained object.
 */

public abstract class DependentObject<T> {

  private T data;

  public void setData(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }
}
