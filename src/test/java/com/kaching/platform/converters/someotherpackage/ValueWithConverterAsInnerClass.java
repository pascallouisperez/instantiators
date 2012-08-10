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
package com.kaching.platform.converters.someotherpackage;

import com.kaching.platform.converters.ConvertedBy;
import com.kaching.platform.converters.Converter;

@ConvertedBy(ValueWithConverterAsInnerClass.Cvter.class)
public class ValueWithConverterAsInnerClass {

  private final int val;

  ValueWithConverterAsInnerClass(int val) {
    this.val = val;
  }

  public int getVal() {
    return val;
  }
  
  static class Cvter implements Converter<ValueWithConverterAsInnerClass> {

    @Override
    public String toString(ValueWithConverterAsInnerClass value) {
      return Integer.toString(value.getVal());
    }

    @Override
    public ValueWithConverterAsInnerClass fromString(String representation) {
      return new ValueWithConverterAsInnerClass(Integer.valueOf(representation));
    }

  }

}
