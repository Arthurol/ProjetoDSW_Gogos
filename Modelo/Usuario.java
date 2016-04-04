/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Cliente
 */
public @Data class Usuario {
    private String nome;
    private int ID;
    private int CPF;
    private int telefone;
    private String senha;
    private String email;
    private List<Gogo> gogos;
}
