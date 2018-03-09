package com.eteks.sweethome3d.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class sss {
  private static Object deepClone(Object a) {
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    ObjectOutputStream oo;
    try {
      oo = new ObjectOutputStream(bo);

      oo.writeObject(a);
      ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
      ObjectInputStream oi = new ObjectInputStream(bi);
      return (oi.readObject());
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  

  public static void main(String [] args) {
    A a = new A();
    a.a=1;
    a.b=2;
    
    A b = (A)deepClone(a);
    System.out.println(b.a);
  }
}
class A implements Serializable{
  int a;
  int b;
}