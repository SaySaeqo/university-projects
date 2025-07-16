/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.platformytechnologiczne.lab1;

import java.util.Comparator;

/**
 *
 * @author SaySaeqo
 */
public class MagesByLevelComparator implements Comparator<Mage>{
  
  @Override
  public int compare(Mage o1, Mage o2) {
    int result;
    result = o2.getLevel() - o1.getLevel();
    if (result == 0) {
      result = o1.getName() == null
        ? (o2.getName() == null ? 0 : -1) : o1.getName().compareTo(o2.getName());
    }
    if (result == 0) {
      result = o1.getPower() > o2.getPower() ? 1 : 
                                        (o1.getPower() == o2.getPower() ? 0 : -1);
    }
    return result;
  }
}
