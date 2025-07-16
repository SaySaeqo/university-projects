package pl.site.lab5;


import java.util.Optional;
import org.junit.Test;
import pl.site.lab5.MageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pl.site.lab5.Mage;

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

/**
 *
 * @author SaySaeqo
 */
public class MageRepositoryTest {

  
  @Test
  public void deleteNonExisting(){
    MageRepository repo = new MageRepository();
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> repo.delete("random stuff"));
  }
  
  @Test
  public void findNonExisting(){
    MageRepository repo = new MageRepository();
    
    Optional<Mage> result = repo.find("random stuff");
    Assertions.assertThat(result.isEmpty());
  }
  
  @Test
  public void findExisting(){
    MageRepository repo = new MageRepository();
    repo.save(new Mage("abc",3));
    
    Optional<Mage> result = repo.find("abc");
    Assertions.assertThat(! result.isEmpty());
  }
  
  @Test
  public void saveAlreadyExisting(){
    MageRepository repo = new MageRepository();
    repo.save(new Mage("abc",3));
    
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> repo.save(new Mage("abc",3)));
  }
  
  
  
}
