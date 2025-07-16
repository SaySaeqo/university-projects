/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.platformytechnologiczne.lab1;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import lombok.Getter;

/**
 *
 * @author SaySaeqo
 */
@Getter
public class InitializeTestData {
  private final Set<Mage> magesh;
  private final int setType;
  
  public InitializeTestData(int setTyp)
  {
    setType = setTyp;
    switch (setType)
    {
      default:
        magesh = new HashSet<>();
        break;
      case 1:
        magesh = new TreeSet<>();
        break;
      case 2:
        magesh = new TreeSet<>(new MagesByLevelComparator());
        break;
    }
  }
  
  public InitializeTestData init()
  {
    Mage mage1 = Mage.builder()
        .name("Odalf")
        .level(10)
        .power(20.5)
        .build(setType);
    Mage mage11 = Mage.builder()
        .name("Unzurune")
        .level(8)
        .power(10.5)
        .build(setType);
    Mage mage12 = Mage.builder()
        .name("Anaxx")
        .level(6)
        .power(5.76)
        .build(setType);
    Mage mage121 = Mage.builder()
        .name("Dhudius")
        .level(1)
        .power(0.4)
        .build(setType);
    Mage mage2 = Mage.builder()
        .name("Ustrum")
        .level(41)
        .power(120.79)
        .build(setType);
    Mage mage21 = Mage.builder()
        .name("Anufrarihm")
        .level(11)
        .power(19.78)
        .build(setType);
    Mage mage3 = Mage.builder()
        .name("Thuflyn")
        .level(40)
        .power(98.54)
        .build(setType);
    Mage mage31 = Mage.builder()
        .name("Giqium")
        .level(25)
        .power(67.7)
        .build(setType);
    Mage mage311 = Mage.builder()
        .name("Odrinorim")
        .level(8)
        .power(4.98)
        .build(setType);
    Mage mage312 = Mage.builder()
        .name("Izin")
        .level(8)
        .power(8.98)
        .build(setType);
    
    
    mage1.addToApprentices(mage11);
    mage1.addToApprentices(mage12);
    mage12.addToApprentices(mage121);
    mage2.addToApprentices(mage21);
    mage3.addToApprentices(mage31);
    mage31.addToApprentices(mage311);
    mage31.addToApprentices(mage312);
   
    magesh.add(mage1);
    magesh.add(mage2);
    magesh.add(mage3);
    
    return this;
  }
}
