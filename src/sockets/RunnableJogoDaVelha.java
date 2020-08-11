package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RunnableJogoDaVelha implements Runnable {

    private Socket socket; //Socket que representa o ponto de conexão entre Jogador 1 (Servidor) e Jogador 2 (Cliente)
    private Button[][] matriz = new Button[3][3];
    private Label labelQuemEstaJogando;
    private Label labelVencedor;

    //Método contrutor da Runnable
    public RunnableJogoDaVelha(Socket socket, Button m[][], Label labelQuemEstaJogando, Label labelVencedor) {
        this.socket = socket;
        this.matriz = m;
        this.labelQuemEstaJogando = labelQuemEstaJogando;
        this.labelVencedor = labelVencedor;
    }

    @Override
    public void run() { //Método iniciado após o acionamento do método start da ThreadServidor no JFrameJogoCliente
        try {
            boolean sair = false; //Atributo para verificar se a Runnable deve continuar rodando ou não
            while (!sair) { //Repetirá até que um dos Jogadores tenha vencido (ou seja, sair = true)
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());//Obtendo do socket o objeto de entrada de dados

                //Entrada de Dados: Recebendo mensagem de dados do adversário
                Jogada jogada = (Jogada) entrada.readObject(); // Ler, via Socket, o objeto do tipo Jogada

                Button button = matriz[jogada.getJogadaLinha()][jogada.getJogadaColuna()]; // Obter o button da matriz (de acordo com linha e coluna da jogada)
                Platform.runLater(() -> button.setText(jogada.getJogador())); // Setar o text do button com o jogador responsável pela Jogada
                Platform.runLater(() -> button.setDisable(true)); // Desabilitar o button correspondente a Jogada
                if (jogada.getJogador().equals("X")) { // Setar o text do labelQuemEstaJogando com '0' ou 'X' (de acordo com o próximo jogador)
                    Platform.runLater(() -> labelQuemEstaJogando.setText("0"));
                } else {
                    Platform.runLater(() -> labelQuemEstaJogando.setText("X"));
                }

                if (jogada.getVencedorJogadorX() || jogada.getVencedorJogador0()) { //Verificar se um dos dois Jogadores venceu o jogo
                    sair = true;
                    if (jogada.getVencedorJogadorX()){ // Setar o text do labelVencedor com '0' ou 'X'
                        Platform.runLater(() -> labelVencedor.setText("X"));
                    }
                    if (jogada.getVencedorJogador0()){
                        Platform.runLater(() -> labelVencedor.setText("0"));
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RunnableJogoDaVelha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
