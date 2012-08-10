/**
 * Copyright 2010 Wealthfront Inc. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.kaching.platform.converters;

import java.net.URI;

/**
 * <p>A converter is responsible for converting to and from a textual
 * representation of a value object. For instance, a {@link URI} can be
 * represented as the string {@code "http://www.kaching.com"} and a converter
 * would create a {@link URI} object from this representation and produce this
 * string from a {@link URI} instance.</p>
 *
 * <p>Converters much ensure that the textual representation produced by
 * {@link #toString(Object)} is compatible with {@link #fromString(String)} such
 * that</p>
 * <pre>
 * T value1 = ...
 * T value2 = converter.fromString(converter.toString(value1));
 * assertEquals(value1, value2);</pre>
 * <p>always succeeds.</p>
 *
 * @param <T> the type this converter converts.
 */
public interface Converter<T> {
  
  /**
   * Converts a value to a string representation.
   * @param value the value to convert.
   * @return the string representation of the value.
   */
  String toString(T value);

  /**
   * Converts a textual representation into a value.
   * Throws an {@link IllegalArgumentException} if the value cannot be converted.
   * @param the textual representation to convert.
   * @return the value represented by the textual representation.
   */
  T fromString(String representation);
  
}
