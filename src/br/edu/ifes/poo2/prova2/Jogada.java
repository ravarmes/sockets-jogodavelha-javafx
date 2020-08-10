package br.edu.ifes.poo2.prova2;

import java.io.Serializable;

//Objetos dessa classe serão enviados, via socket, de Cliente para Servidor, e vice-versa
//A finalidade principal é que um dado Jogador mantenha seu oponente informado quanto:
//1) a posição y de seu JButton
//2) ao fato de ter vencido, ou seja, ter clicado no JButton de seu oponente (vencedorJogadorX ou vencedorJogador0)
public class Jogada implements Serializable {

    private int jogadaLinha; //Armazena a informação da casa do Jogo da Velha (linha)
    private int jogadaColuna; //Armazena a informação da casa do Jogo da Velha (coluna)
    private String jogador; //Armazena 'X' ou '0' especificando o Jogador responsável pela Jogada
    private boolean vencedorJogadorX = false; //Armazena a informação se Jogador 1 venceu o jogo
    private boolean vencedorJogador0 = false; //Armazena a informação se Jogador 2 venceu o jogo

    public int getJogadaLinha() {
        return jogadaLinha;
    }

    public void setJogadaLinha(int jogadaLinha) {
        this.jogadaLinha = jogadaLinha;
    }

    public int getJogadaColuna() {
        return jogadaColuna;
    }

    public void setJogadaColuna(int jogadaColuna) {
        this.jogadaColuna = jogadaColuna;
    }

    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public boolean getVencedorJogadorX() {
        return vencedorJogadorX;
    }

    public void setVencedorJogadorX(boolean vencedorJogadorX) {
        this.vencedorJogadorX = vencedorJogadorX;
    }

    public boolean getVencedorJogador0() {
        return vencedorJogador0;
    }

    public void setVencedorJogador0(boolean vencedorJogador0) {
        this.vencedorJogador0 = vencedorJogador0;
    }

    

}
