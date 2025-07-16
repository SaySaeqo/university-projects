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
package pl.site.lab6;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author SaySaeqo
 */
public class Main {
  
  public static long poolTesting(int limit, String src, String dst){
    
    ForkJoinPool pool = new ForkJoinPool(limit);
    long time = System.currentTimeMillis();
    
    
    try {
      pool.submit(()->{
        // Potok sciezek do inputu
        List<Path> files = null;
        Path source = Path.of(src);
        try (Stream<Path> stream = Files.list(source)) {
          files = stream.collect(Collectors.toList());
        } catch (IOException e) {
          System.err.println("Cannot get list of Paths.");
        }
        Stream<Path> fstream = files.stream().parallel();
        
        
        // Potok par ze sciezka i obrazkiem
        Stream<Pair<String,BufferedImage>> pairs = fstream
            .flatMap(path -> {
              BufferedImage image = null;
              try {
                image = ImageIO.read(path.toFile());
              } catch (IOException ex) {
                System.err.println("Cannot get image from input.");
              }
              String name = path.getFileName().toString();
              return Stream.of(Pair.of(name,image));
            })
            .parallel();
        
        
        //  Potok par ze sciezka i obrazkiem po przeksztalceniach
        Stream<Pair<String,BufferedImage>> new_pairs = pairs
            .flatMap(pair -> {
              BufferedImage original = pair.getRight();
              BufferedImage image = new BufferedImage(original.getWidth(),
                  original.getHeight(),
                  original.getType());
              for (int i = 0; i < original.getWidth(); i++) {
                for (int j = 0; j < original.getHeight(); j++) {
                  int rgb = original.getRGB(i, j);
                  
                  Color color = new Color(rgb);
                  int red = color.getRed();
                  int blue = color.getBlue();
                  int green = color.getGreen();
                  Color outColor = new Color(red, blue, green);
                  int outRgb = outColor.getRGB();
                  
                  image.setRGB(i, j, outRgb);
                }
              }
              
              return Stream.of(Pair.of(pair.getLeft(),image));
            })
            .parallel();
        
        
        // Zapisanie obrazkow na dysku
        new_pairs.forEach(pair ->{
          try {
            BufferedImage image = pair.getRight();
            String path = dst + pair.getLeft();
            String format = pair.getLeft()
                .substring(pair.getLeft().length() - 3, pair.getLeft().length());
            ImageIO.write(image, format,
                Path.of(path).toFile());
          } catch (IOException ex) {
            System.err.println("Cannot write image to output.");
          }
        });
      }).get();
      
    } catch (InterruptedException | ExecutionException ex) {
      System.err.println("Problem with ForkJoin Pool");
      System.err.println(ex.getMessage());
    }
    
    
    time = System.currentTimeMillis() - time;
    
    pool.shutdownNow();
    
    return time;
    
  }
  
  public static void deleteTestingOutput(String dst)
  {
      ForkJoinPool pool = ForkJoinPool.commonPool();
    try {
      pool.submit(() -> {
        // Potok sciezek do outputu
        List<Path> files = null;
        Path source = Path.of(dst);
        try (Stream<Path> stream = Files.list(source)) {
          files = stream.collect(Collectors.toList());
        } catch (IOException e) {
          System.err.println("Cannot get list of Paths to dest.");
        }
        Stream<Path> fstream = files.stream().parallel();
        
        // Usuwanie
        fstream.forEach(p ->{
          String path = dst + p.getFileName().toString();
          File to_delete = new File(path);
          to_delete.delete();
        });
      }).get();
    } catch (InterruptedException | ExecutionException ex) {
      System.err.println("Problem with ForkJoin commonPool");
      System.err.println(ex.getMessage());
    }
    
  }

  public static void main(String[] args){
    
    String source = args[0];
    String dest = args[1];

    int lower_limit = 1;
    int upper_limit = 8;
    
    for(int limit = upper_limit; limit>= lower_limit; limit--){
      deleteTestingOutput(dest);
      long time = poolTesting(limit, source, dest);
      System.out.println(time + "ms <-- " + limit + " watki");
      
    }
    
  }
}
