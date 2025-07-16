/*
 * The MIT License
 *
 * Copyright 2021 SaySaeqo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.site.lab5;

import java.util.Collection;
import java.util.Optional;
import java.util.ArrayList;
import lombok.NoArgsConstructor;

/**
 *
 * @author SaySaeqo
 */
@NoArgsConstructor
public class MageRepository {

  private final Collection<Mage> collection = new ArrayList<>();

  public Optional<Mage> find(String name) {
    Mage result = null;
    for (Mage mage : collection)
      if(name.equals(mage.getName())) {
        result = mage;
        break;
      }
    return Optional.ofNullable(result);
  }

  public void delete(String name) throws IllegalArgumentException{
    boolean any_removed = collection.removeIf(a -> name.equals(a.getName()));
    if (! any_removed) throw new IllegalArgumentException();
  }

  public void save(Mage mage) throws IllegalArgumentException{
    if (!find(mage.getName()).isEmpty())throw new IllegalArgumentException();
    collection.add(mage);
  }

}
