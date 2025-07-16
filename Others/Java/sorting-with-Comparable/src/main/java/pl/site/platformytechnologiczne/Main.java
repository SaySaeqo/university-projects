/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.platformytechnologiczne;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import pl.site.platformytechnologiczne.lab1.InitializeTestData;
import pl.site.platformytechnologiczne.lab1.Mage;
import pl.site.platformytechnologiczne.lab1.MagesByLevelComparator;

/**
 *
 * @author SaySaeqo
 */
public class Main {
  
  
  /**
   * Dane wejściowe:
   * 
   * 0 -> brak sortowania
   * 1 -> sortowanie naturalne
   * 2 -> sortowanie alternatywne
   * 
   * 
   */
  
  public static Map<Mage, Integer> generateMapFrom(Set<Mage> magesh)
  {
    Map<Mage, Integer> map;
    if (magesh instanceof TreeSet)
    {
      TreeSet<Mage> w = (TreeSet<Mage>) magesh;
      if (w.comparator() == null)
        map = new TreeMap<>();
      else
        map = new TreeMap<>(new MagesByLevelComparator());
    }
    else
      map = new HashMap<>();
    
    for ( Mage mage : magesh)
    {
      Map<Mage, Integer> sub_map = generateMapFrom(mage.getApprentices());
      Integer counter = 0;
      for(Mage child : mage.getApprentices())
      {
        counter += sub_map.get(child) + 1;
      }
      map.putAll(sub_map);
      map.put(mage, counter);
    }
    
    return map;  
    
  }

  public static void main(String args[]){
    
    int sortingType = args[0].charAt(0) - '0';
    
    Set<Mage> magesh = new InitializeTestData(sortingType).init().getMagesh();
    
    for (Mage mage : magesh)
    {
      System.out.print(mage.recurencyToString());
    }
    
    Map<Mage, Integer> map = generateMapFrom(magesh);
    
    System.out.println(map);
    
  }
  
}
