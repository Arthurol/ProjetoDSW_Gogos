/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import lombok.Data;

/**
 *
 * @author Cliente
 */
public @Data class Oferta {
    private String tipoDeOferta;
    private double valor;
    private String autor;
    private String nomeGogo;
    private String status;
}