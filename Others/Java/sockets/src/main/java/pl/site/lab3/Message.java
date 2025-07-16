/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab3;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author SaySaeqo
 */
@Setter
@Getter
@AllArgsConstructor
public class Message implements Serializable {
  
    private int number;
    private String content;
}
