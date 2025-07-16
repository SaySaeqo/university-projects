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

import java.util.Optional;
import org.junit.Test;
import pl.site.lab5.MageRepository;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import pl.site.lab5.Mage;
/**
 *
 * @author SaySaeqo
 */
public class MageControllerTest {
  
  
  @Test
  public void deleteNonExisting(){
    MageRepository repo = Mockito.mock(MageRepository.class);
    Mockito.doThrow(new IllegalArgumentException()).when(repo).delete("random stuff");
    
    MageController ctrl = new MageController(repo);
    
    Assertions.assertThat(ctrl.delete("random stuff")).
        isEqualTo("not found");
  }
  
  @Test
  public void deleteExisting(){
    MageRepository repo = Mockito.mock(MageRepository.class);
    MageController ctrl = new MageController(repo);
    
    
    Assertions.assertThat(ctrl.delete("abc"))
        .isEqualTo("done");
  }
  
  @Test
  public void findNonExisting(){
    MageRepository repo = Mockito.mock(MageRepository.class);
    Mockito.when(repo.find("random stuff")).thenReturn(Optional.empty());
    
    MageController ctrl = new MageController(repo);
    Assertions.assertThat(ctrl.find("random stuff")).
        isEqualTo("not found");
  }
  
  @Test
  public void findExisting(){
    MageRepository repo = Mockito.mock(MageRepository.class);
    Mage mage = new Mage("abc", 3);
    Mockito.when(repo.find("abc")).thenReturn(Optional.of(mage));
    
    MageController ctrl = new MageController(repo);
    Assertions.assertThat(ctrl.find("abc")).
        isEqualTo(mage.toString());
  }
  
  @Test
  public void SaveNonExisting(){
    MageRepository repo = Mockito.mock(MageRepository.class);
    
    MageController ctrl = new MageController(repo);
    Assertions.assertThat(ctrl.save("random stuff", "3")).
        isEqualTo("done");
  }
  
  @Test
  public void SaveExisting(){
    MageRepository repo = Mockito.mock(MageRepository.class);
    Mockito.doThrow(new IllegalArgumentException())
        .when(repo).save(Mockito.any(Mage.class));
    
    MageController ctrl = new MageController(repo);
    Assertions.assertThat(ctrl.save("abc", "3")).
        isEqualTo("bad request");
  }
  
  
}
