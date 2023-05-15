/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab4;

import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author SaySaeqo
 */
public class Main {
  
  public static void main(String args[])
  {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pllab4");
    JpaService<Mage> mages = new JpaService<>(emf, Mage.class);
    JpaService<Tower> towers = new JpaService<>(emf, Tower.class);
    Scanner scan = new Scanner(System.in);
    
    boolean running = true;
    while (running)
    {
      String command = scan.next();
      switch (command){
        case "quit":
          running = false;
          break;
        case "add":
          command = scan.next();
          switch(command){
            case "tower":
            {
              System.out.print("Nazwa: ");
              String name = scan.next();
              System.out.print("Wysokosc: ");
              int height = scan.nextInt();

              Tower new_tuple = Tower.builder()
                  .name(name)
                  .height(height)
                  .build();
              
              towers.add(new_tuple);
              break;
            }
            case "mage":
            {
              System.out.print("Nazwa: ");
              String name = scan.next();
              System.out.print("Level: ");
              int level = scan.nextInt();
              System.out.print("Rezyduje na wieży?: ");
              String tower = scan.next();
              Tower tuple = towers.find(tower);

              Mage new_tuple = Mage.builder()
                  .name(name)
                  .level(level)
                  .tower(tuple)
                  .build();
              
              mages.add(new_tuple);
              break;
            }
          }
          break;
        case "delete":
          command = scan.next();
          switch(command){
            case "tower":
            {
              System.out.print("Nazwa: ");
              String name = scan.next();
              Tower tuple = towers.find(name);
              if(tuple != null)
              {
                EntityManager em = emf.createEntityManager();
                 List<Mage> list = em.createQuery("select e from Mage e WHERE "
                     + "e.tower = :tower", Mage.class)
                    .setParameter("tower", tuple)
                    .getResultList();
                 list.forEach(a -> mages.delete(a));
                em.close();
                towers.delete(tuple);
              }
              break;
            }
            case "mage":
            {
              System.out.print("Nazwa: ");
              String name = scan.next();
              Mage tuple = mages.find(name);
              if(tuple != null)
              {
                mages.delete(tuple);
              }
              break;
            }
          }
          break;
        case "show":
          mages.findAll().forEach((mage) -> {
            System.out.println(mage);
        });
          towers.findAll().forEach((tower) -> {
            System.out.println(tower);
        });
          break;
        case "level":
          System.out.print("Minimalny wiekszy niz: ");
          int level = scan.nextInt();
          EntityManager em = emf.createEntityManager();
          List<Mage> list = em.createQuery("select e from Mage e WHERE e.level "
              + "> :level", Mage.class)
              .setParameter("level", level)
              .getResultList();
          em.close();
          list.forEach(a -> System.out.println(a));
      }
    }
    emf.close();
  }
}