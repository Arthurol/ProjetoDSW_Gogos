/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import lombok.Data;
import org.joda.time.DateTime;
/**
 *
 * @author Cliente
 */
public @Data class Token {
    private int token;
    private int idUsuario;
    private int idToken;
    private DateTime data_validade;
    
}
