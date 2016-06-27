package bolsaGogos.model;
import lombok.Getter;
import lombok.Setter;


/**
* Classe responsável por instanciar objetos do tipo Personagem
*/
public @Getter @Setter class Personagem {
    private String nome;
    private int id;

    public Personagem(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    public Personagem() {
        nome = "";
        id = -1;
    }
}

