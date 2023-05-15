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
import lombok.Setter;

/**
 *
 * @author SaySaeqo
 */
@Getter
@Setter
public class Mage implements Comparable<Mage> {

  private String name;

  private int level;

  private double power;

  private Set<Mage> apprentices;
  
  public void addToApprentices(Mage warrior)
  {
    apprentices.add(warrior);
  }

  @Override
  public int compareTo(Mage other) {
    int result = name == null
        ? (other.name == null ? 0 : -1) : name.compareTo(other.name);
    if (result == 0) {
      result = level - other.level;
    }
    if (result == 0) {
      result = power > other.power ? 1 : (power == other.power ? 0 : -1);
    }
    return result;
  }

  private Mage(String nam, int leve, double powe, int setType) {
    name = nam;
    level = leve;
    power = powe;
    switch (setType)
    {
      default:
        apprentices = new HashSet<>();
        break;
      case 1:
        apprentices = new TreeSet<>();
        break;
      case 2:
        apprentices = new TreeSet<>(new MagesByLevelComparator());
        break;
    }

  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private String name;
    private int level;
    private double power;

    public Builder name(String nam) {
      name = nam;
      return this;
    }

    public Builder level(int leve) {
      level = leve;
      return this;
    }

    public Builder power(double powe) {
      power = powe;
      return this;
    }

    public Mage build(int setType) {
      return new Mage(name, level, power, setType);
    }

  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other instanceof Mage) {
      Mage o = (Mage) other;
      if ((name == null ? o.name == null : name.equals(o.name))
          && level == o.level
          && power == o.power) {
        return true;
      }

    }
    return false;
  }

  @Override
  public int hashCode() {
    int code = name.hashCode() * 7 + (int)(power * 290) + level * 79 + apprentices.hashCode();
    return code;
  }

  @Override
  public String toString() {
    String string = "Mage{name=" + name + ", level=" + level
        + ", power=" + power + "}";
    return string;
  }
  
  public String recurencyToString()
  {
    return recurencyToString(1);
  }
  
  public String recurencyToString(int level)
  {
    String string = "";
    for (int i =0; i < level; i++)
    {
      string += "-";
    }
    string += this + "\n";
    for (Mage child : apprentices)
    {
      string += child.recurencyToString(level+1);
    }
    return string;
  }
}
