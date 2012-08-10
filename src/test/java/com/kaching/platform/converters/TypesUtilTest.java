package com.kaching.platform.converters;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

public class TypesUtilTest {

  @Test
  public void testIsInstance() {
    // class
    assertTrue(TypesUtil.isInstance(String.class, String.class));
    assertTrue(TypesUtil.isInstance(boolean[].class, boolean[].class));
    assertFalse(TypesUtil.isInstance(List.class, String.class));
    
    // generic array type
    assertTrue(TypesUtil.isInstance(
        new TypeLiteral<List<?>[]>() {}.getType(),
        new TypeLiteral<List<Integer>[]>() {}.getType()));
    
    // parameterized type
    assertTrue(TypesUtil.isInstance(
        Types.listOf(Types.subtypeOf(Object.class)), Types.listOf(String.class)));
    
    // type variable
    assertTrue(TypesUtil.isInstance(new TypeVariableImpl(), String.class));
    assertTrue(TypesUtil.isInstance(new TypeVariableImpl(Serializable.class), String.class));
    assertFalse(TypesUtil.isInstance(new TypeVariableImpl(Collection.class), String.class));
    
    // wildcard type
    assertTrue(TypesUtil.isInstance(
        Types.subtypeOf(Object.class), Types.listOf(Boolean.class)));
    assertTrue(TypesUtil.isInstance(
        Types.subtypeOf(Collection.class), Types.listOf(Boolean.class)));
    
    assertFalse(TypesUtil.isInstance(
        Types.subtypeOf(Collection.class), Serializable.class));
    assertFalse(TypesUtil.isInstance(
        Types.supertypeOf(Set.class), TreeSet.class));
  }

  @Test
  public void testIsAssignableFrom() {
    // class
    assertTrue(TypesUtil.isAssignableFrom(String.class, String.class));
    assertTrue(TypesUtil.isAssignableFrom(Serializable.class, String.class));
    assertTrue(TypesUtil.isAssignableFrom(Set.class, HashSet.class));
    assertTrue(TypesUtil.isAssignableFrom(Object.class, Types.listOf(String.class)));
    assertTrue(TypesUtil.isAssignableFrom(
        List[].class, new TypeLiteral<List<?>[]>() {}.getType()));
    assertTrue(TypesUtil.isAssignableFrom(
        ArrayList[].class, new TypeLiteral<ArrayList<?>[]>() {}.getType()));

    assertFalse(TypesUtil.isAssignableFrom(Set.class, List.class));
    assertFalse(TypesUtil.isAssignableFrom(
        ArrayList[].class, new TypeLiteral<List<?>[]>() {}.getType()));

    // generic array type
    assertTrue(TypesUtil.isAssignableFrom(
        new TypeLiteral<List<?>[]>() {}.getType(),
        new TypeLiteral<List<Integer>[]>() {}.getType()));
    assertTrue(TypesUtil.isAssignableFrom(
        new TypeLiteral<List<Integer>[]>() {}.getType(),
        new TypeLiteral<ArrayList<Integer>[]>() {}.getType()));
    assertTrue(TypesUtil.isAssignableFrom(
        new TypeLiteral<List<? extends Set<?>>[]>() {}.getType(),
        new TypeLiteral<ArrayList<TreeSet<Double>>[]>() {}.getType()));

    assertFalse(TypesUtil.isAssignableFrom(
        new TypeLiteral<List<?>[]>() {}.getType(), boolean[].class));
    assertFalse(TypesUtil.isAssignableFrom(
        new TypeLiteral<List<?>[]>() {}.getType(), Collection[].class));
    assertFalse(TypesUtil.isAssignableFrom(
        new TypeLiteral<List<?>[]>() {}.getType(), List[].class));

    // parameterized type
    assertTrue(TypesUtil.isAssignableFrom(Types.listOf(String.class),
        List.class));
    assertTrue(TypesUtil.isAssignableFrom(Types.listOf(String.class), Types
        .listOf(String.class)));
    assertTrue(TypesUtil.isAssignableFrom(Types.listOf(String.class), Types
        .newParameterizedType(ArrayList.class, String.class)));
    assertTrue(TypesUtil.isAssignableFrom(Types.listOf(Types
        .subtypeOf(Serializable.class)), Types.newParameterizedType(
        ArrayList.class, String.class)));

    assertFalse(TypesUtil.isAssignableFrom(Types.listOf(String.class), Types
        .setOf(String.class)));
    assertFalse(TypesUtil.isAssignableFrom(Types.listOf(Serializable.class),
        Types.listOf(String.class)));
    assertFalse(TypesUtil.isAssignableFrom(Types.listOf(String.class), Types
        .listOf(Integer.class)));

    // type variable
    assertTrue(TypesUtil.isAssignableFrom(new TypeVariableImpl(), String.class));
    assertTrue(TypesUtil.isAssignableFrom(new TypeVariableImpl(String.class),
        String.class));
    assertTrue(TypesUtil.isAssignableFrom(new TypeVariableImpl(String.class,
        Serializable.class, Comparable.class), String.class));

    assertFalse(TypesUtil.isAssignableFrom(new TypeVariableImpl(List.class,
        Set.class), TreeSet.class));
    
    // wildcard type
    assertTrue(TypesUtil.isAssignableFrom(
        Types.supertypeOf(Collection.class), Types.subtypeOf(List.class)));
    assertTrue(TypesUtil.isAssignableFrom(
        Types.supertypeOf(Collection.class), Types.listOf(Integer.class)));
    assertTrue(TypesUtil.isAssignableFrom(
        Types.supertypeOf(new TypeLiteral<Collection<? extends Number>>() {}.getType()),
        Types.listOf(Integer.class)));
    
    assertFalse(TypesUtil.isAssignableFrom(
        Types.subtypeOf(Object.class), Types.subtypeOf(Object.class)));
    assertFalse(TypesUtil.isAssignableFrom(
        Types.supertypeOf(new TypeLiteral<Collection<? extends Number>>() {}.getType()),
        Types.listOf(String.class)));
  }

  static class TypeVariableImpl implements TypeVariable<GenericDeclaration> {
    private final Type[] bounds;

    TypeVariableImpl() {
      this(new Type[0]);
    }

    TypeVariableImpl(Type... bounds) {
      this.bounds = bounds;
    }

    @Override
    public Type[] getBounds() {
      return bounds;
    }

    @Override
    public GenericDeclaration getGenericDeclaration() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
      throw new UnsupportedOperationException();
    }
  }

}
