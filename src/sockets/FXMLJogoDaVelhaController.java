/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author rafael
 */
public class FXMLJogoDaVelhaController implements Initializable {

    @FXML
    private Label labelJogador;
    @FXML
    private Label labelQuemEstaJogando;
    @FXML
    private Label labelVencedor;
    @FXML
    private Button buttonCasa00;
    @FXML
    private Button buttonCasa01;
    @FXML
    private Button buttonCasa02;
    @FXML
    private Button buttonCasa10;
    @FXML
    private Button buttonCasa11;
    @FXML
    private Button buttonCasa12;
    @FXML
    private Button buttonCasa20;
    @FXML
    private Button buttonCasa21;
    @FXML
    private Button buttonCasa22;

    private Button[][] matriz = new Button[3][3];

    private ServerSocket serverSocket;

    private Socket socket;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Atribuindo na matriz todos os Buttons
        matriz[0][0] = buttonCasa00;
        matriz[0][1] = buttonCasa01;
        matriz[0][2] = buttonCasa02;
        matriz[1][0] = buttonCasa10;
        matriz[1][1] = buttonCasa11;
        matriz[1][2] = buttonCasa12;
        matriz[2][0] = buttonCasa20;
        matriz[2][1] = buttonCasa21;
        matriz[2][2] = buttonCasa22;
    }

    @FXML
    public void handleButtonIniciarServidor() throws IOException {
        System.out.println("Iniciando servidor...");
        labelJogador.setText("X"); // O Jogador que inicia o Servidor sempre terá o ID 'X'
        labelQuemEstaJogando.setText("X"); // O Jogador que inicia o Servidor sempre será o primeiro a jogar
        serverSocket = new ServerSocket(54321); // Instanciar o objeto ServerSocket na porta 54321
        socket = serverSocket.accept(); // Servidor aceita conexões de clientes
        RunnableJogoDaVelha runnableServidor = new RunnableJogoDaVelha(socket, matriz, labelQuemEstaJogando, labelVencedor); // Instanciar a runnable do jogador X
        Thread threadServidor = new Thread(runnableServidor); // Instanciar a Thread do jogador x
        threadServidor.start(); // "Startar" objeto da classe Thread do jogador x
    }

    @FXML
    public void handleButtonConectarNoServidor() throws IOException {
        System.out.println("Conectando no servidor...");
        labelJogador.setText("0"); // O Jogador que se conecta ao Servidor sempre terá o ID '0'
        labelQuemEstaJogando.setText("X");// O Jogador que inicia o Servidor sempre será o primeiro a jogar
        socket = new Socket("127.0.0.1", 54321); // Instanciar o objeto Socket para se conectar na porta 54321
        RunnableJogoDaVelha runnableCliente = new RunnableJogoDaVelha(socket, matriz, labelQuemEstaJogando, labelVencedor); // Instanciar a runnable do jogador X
        Thread threadCliente = new Thread(runnableCliente); // Instanciar a Thread do jogador x
        threadCliente.start(); // "Startar" objeto da classe Thread do jogador x
    }

    //Método para enviar o objeto Jogada via socket para a Thread do Jogador adversário
    public void enviarJogada(int linha, int coluna, Button button) throws IOException {

        if (labelJogador.getText().equals(labelQuemEstaJogando.getText())) { // Verificando se o jogador está jogando em sua vez

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());//Obtendo do socket o objeto para saída de dados

            button.setText(labelJogador.getText()); //Setando no button escolhido o ID ('X' ou '0') do jogador 
            button.setDisable(true); //Desabilitando o button escolhido (para que não seja mais uma opção válida)
            atualizarLabelQuemEstaJogando(labelJogador.getText()); //Atualizar no label o ID ('X' ou '0') do jogador que irá jogar

            Jogada jogada = new Jogada(); // Instanciando um objeto do tipo 'Jogada'
            jogada.setJogadaLinha(linha); // Setando a linha no objeto do tipo Jogada
            jogada.setJogadaColuna(coluna); // Setando a coluna no objeto do tipo Jogada
            jogada.setJogador(labelJogador.getText()); // Setando o jogador no objeto do tipo Jogada
            int vencedor = verificarVencedor(); // Verificar se houve vencedor após a jogada
            if ((vencedor == 1) || (vencedor == 2)) { // Caso tenha existido um vencedor deve-se: 1) atualizar label vencedor; 2) Setar vencedor no objeto do tipo Jogada
                atualizarLabelVencedor(vencedor);
                if (vencedor == 1) {
                    jogada.setVencedorJogadorX(true);
                }
                if (vencedor == 2) {
                    jogada.setVencedorJogador0(true);
                }
            }
            saida.writeObject(jogada); // Enviar objeto Jogada via Socket
        }
    }

    public void atualizarLabelQuemEstaJogando(String jogadorAtual) {
        if (jogadorAtual.equals("X")) {
            labelQuemEstaJogando.setText("0");
        } else {
            labelQuemEstaJogando.setText("X");
        }
    }

    public void atualizarLabelVencedor(int vencedor) {
        if (vencedor == 1) {
            labelVencedor.setText("X");
        }
        if (vencedor == 2) {
            labelVencedor.setText("0");
        }
    }

    //Método para verificar se houve vencedor após a jogada
    //return 0: não teve vencedor
    //return 1: vencedor Jogador X
    //return 2: vencedor Jogador 0
    public int verificarVencedor() {
        int qtdx = 0, qtd0 = 0;
        int i, j;

        //Verificando vencedor nas linhas
        for (i = 0; i < 3; i++) {
            qtdx = 0;
            qtd0 = 0;
            for (j = 0; j < 3; j++) {
                if (matriz[i][j].getText().equals("X")) {
                    qtdx++;
                }
                if (matriz[i][j].getText().equals("0")) {
                    qtd0++;
                }
            }
            if (qtdx == 3) {
                return 1;
            }
            if (qtd0 == 3) {
                return 2;
            }
        }

        //Verificando vencedor nas colunas
        for (j = 0; j < 3; j++) {
            qtdx = 0;
            qtd0 = 0;
            for (i = 0; i < 3; i++) {
                if (matriz[i][j].getText().equals("X")) {
                    qtdx++;
                }
                if (matriz[i][j].getText().equals("0")) {
                    qtd0++;
                }
            }
            if (qtdx == 3) {
                return 1;
            }
            if (qtd0 == 3) {
                return 2;
            }
        }

        //Verificando vencedor na diagonal principal
        qtdx = 0;
        qtd0 = 0;
        for (i = 0; i < 3; i++) {
            if (matriz[i][i].getText().equals("X")) {
                qtdx++;
            }
            if (matriz[i][i].getText().equals("0")) {
                qtd0++;
            }
        }
        if (qtdx == 3) {
            return 1;
        }
        if (qtd0 == 3) {
            return 2;
        }

        //Verificando vencedor na diagonal secundária
        qtdx = 0;
        qtd0 = 0;
        for (i = 0, j = 2; i < 3; i++, j--) {
            if (matriz[i][j].getText().equals("X")) {
                qtdx++;
            }
            if (matriz[i][j].getText().equals("0")) {
                qtd0++;
            }
        }
        if (qtdx == 3) {
            return 1;
        }
        if (qtd0 == 3) {
            return 2;
        }

        return 0;
    }

    @FXML
    public void handleButtonbuttonCasa00() throws IOException {
        enviarJogada(0, 0, buttonCasa00);
    }

    @FXML
    public void handleButtonbuttonCasa01() throws IOException {
        enviarJogada(0, 1, buttonCasa01);
    }

    @FXML
    public void handleButtonbuttonCasa02() throws IOException {
        enviarJogada(0, 2, buttonCasa02);
    }

    @FXML
    public void handleButtonbuttonCasa10() throws IOException {
        enviarJogada(1, 0, buttonCasa10);
    }

    @FXML
    public void handleButtonbuttonCasa11() throws IOException {
        enviarJogada(1, 1, buttonCasa11);
    }

    @FXML
    public void handleButtonbuttonCasa12() throws IOException {
        enviarJogada(1, 2, buttonCasa12);
    }

    @FXML
    public void handleButtonbuttonCasa20() throws IOException {
        enviarJogada(2, 0, buttonCasa20);
    }

    @FXML
    public void handleButtonbuttonCasa21() throws IOException {
        enviarJogada(2, 1, buttonCasa21);
    }

    @FXML
    public void handleButtonbuttonCasa22() throws IOException {
        enviarJogada(2, 2, buttonCasa22);
    }

}
