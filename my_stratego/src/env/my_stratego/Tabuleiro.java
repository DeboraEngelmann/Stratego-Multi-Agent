// CArtAgO artifact code for project my_stratego

package my_stratego;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import cartago.*;

public class Tabuleiro extends Artifact {
	private Celula[][] tabuleiro = new Celula[7][7];
	private Celula[] timeAzul = new Celula[14];
	private Celula[] timeVermelho = new Celula[14];
	private String jogada = "timeVermelho";
	private String jogador = "livre";
	private int contAzul = 1;
	private int contVermelho = 1;
	private ArrayList<String> ativoAzul = new ArrayList();
	private ArrayList<String> ativoVermelho = new ArrayList();
	private boolean jogoFinalizado = false;
	
	void init() {
		defineObsProperty("jogada", this.jogada, this.jogador);
		for(int linha=0 ; linha < 7 ; linha++){
            for(int coluna = 0; coluna < 7 ; coluna ++){
            	defineObsProperty("posicao",coluna,linha,"livre","livre");
            	this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
            }
        }
		this.iniciaTimes();
	}
	
	void desativarJogador(String nome, String time) {
		if(time.equals("timeAzul")) {
			for(int i = 0; i < ativoAzul.size(); i++){
		        String p = ativoAzul.get(i);
		        if(p.equals(nome))
		        {
		            // Remove.
		        	ativoAzul.remove(p);
		            // Sai do loop.
		            break;
		        }
		    }
		}else {
			for(int i = 0; i < ativoVermelho.size(); i++){
		        String p = ativoVermelho.get(i);
		        if(p.equals(nome))
		        {
		            // Remove.
		        	ativoVermelho.remove(p);
		            // Sai do loop.
		            break;
		        }
		    }
		}
	}
	
	void mudaJogada() {
		if(!this.jogoFinalizado) {
			Random random = new Random();
			if (this.jogada.equals("timeAzul")) {
				this.jogador = this.ativoVermelho.get(random.nextInt(this.ativoVermelho.size()));			
				this.jogada = "timeVermelho";
				ObsProperty prop = getObsPropertyByTemplate("jogada", null, null);
	    		prop.updateValue(0, this.jogada);   
	    		prop.updateValue(1, this.jogador);
			}else {
				this.jogador = this.ativoAzul.get(random.nextInt(this.ativoAzul.size()));			
				this.jogada = "timeAzul";
				ObsProperty prop = getObsPropertyByTemplate("jogada", null, null);
	    		prop.updateValue(0, this.jogada);   
	    		prop.updateValue(1, this.jogador);
			}
		}else {
			this.jogador = "livre";			
			this.jogada = "livre";
			ObsProperty prop = getObsPropertyByTemplate("jogada", null, null);
    		prop.updateValue(0, this.jogada);   
    		prop.updateValue(1, this.jogador);
		}
	}
		
	void iniciaTimes() {
		this.timeAzul[0] = new Celula("prisioneiro","timeAzul","prisioneiro",12);
		this.timeVermelho[0] = new Celula("prisioneiro","timeVermelho","prisioneiro",12);
		
		for(int i=0 ; i < 3 ; i++){
			this.timeAzul[contAzul] = new Celula("bomba","timeAzul","bomba", 11);
			this.timeVermelho[contVermelho] = new Celula("bomba","timeVermelho","bomba", 11);
			this.contAzul++;
			this.contVermelho++;
		}
	}
	
	void entrarNoTabuleiro() {
		for(int linha=0 ; linha < 2 ; linha++){
            for(int coluna = 0; coluna < 7 ; coluna++){
        		System.out.printf("&ContAzul e  %d \n", this.contAzul);
                this.tabuleiro[coluna][linha]=this.timeAzul[this.contAzul];
                defineObsProperty("saiuDoJogo",this.timeAzul[this.contAzul].identificador,false);
                //alterar propriedade observavel
                ObsProperty prop = getObsPropertyByTemplate("posicao", coluna, linha, null, null);
        		prop.updateValue(2, this.timeAzul[this.contAzul].time);   
        		prop.updateValue(3, this.timeAzul[this.contAzul].identificador);
        		this.contAzul++;
            }
        }
		for(int linha=6 ; linha >= 5 ; linha--){
            for(int coluna = 6; coluna >= 0 ; coluna--){
                this.tabuleiro[coluna][linha]=this.timeVermelho[this.contVermelho];

                defineObsProperty("saiuDoJogo",this.timeVermelho[this.contVermelho].identificador,false);
                //alterar propriedade observavel
                ObsProperty prop = getObsPropertyByTemplate("posicao", coluna, linha, null, null);
        		prop.updateValue(2, this.timeVermelho[this.contVermelho].time);
        		prop.updateValue(3, this.timeVermelho[this.contVermelho].identificador);
        		this.contVermelho++;
            }
        }
		System.out.printf("Arr Ativo Azul  %s \n", this.ativoAzul);
		System.out.printf("Arr Ativo Vermelho  %s \n", this.ativoVermelho);
		defineObsProperty("jogoIniciado");
		this.mudaJogada();
	}
	
	void embaralhar(Celula [] v) {			
		Random random = new Random();		
		for (int i=0; i < (v.length - 1); i++) {
			//sorteia um índice
			int j = random.nextInt(v.length); 			
			//troca o conteúdo dos índices i e j do vetor
			Celula temp = v[i];
			v[i] = v[j];
			v[j] = temp;
		}		
	}
	
	void incrementaLinha(int coluna, int linha, String time, String nome){			
			//incrementa linha da posição
			this.tabuleiro[coluna][linha+1] = this.tabuleiro[coluna][linha];
			this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
			
			//alterar propriedade observavel
            ObsProperty prop = getObsPropertyByTemplate("posicao", coluna, linha, null,null);
    		prop.updateValue(2, "livre");
    		prop.updateValue(3,"livre");
    		ObsProperty prop2 = getObsPropertyByTemplate("posicao", coluna, linha+1, null,null);
    		prop2.updateValue(2, time);
    		prop2.updateValue(3,nome);
	}
	
	void decrementaLinha(int coluna, int linha, String time, String nome){			
			//incrementa linha da posição
			this.tabuleiro[coluna][linha-1] = this.tabuleiro[coluna][linha];
			this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
			
			//alterar propriedade observavel
            ObsProperty prop = getObsPropertyByTemplate("posicao", coluna, linha, null, null);
    		prop.updateValue(2, "livre");
    		prop.updateValue(3,"livre");
    		ObsProperty prop2 = getObsPropertyByTemplate("posicao", coluna, linha-1, null, null);
    		prop2.updateValue(2, time);
    		prop2.updateValue(3,nome);
	}
	
	boolean atacaOponente(int coluna, int linha, int forca) {	
		boolean retorno = false;
			if(this.tabuleiro[coluna][linha].time.equals("timeAzul")) {// Se for time Azul
				if(this.tabuleiro[coluna][linha].forca == this.tabuleiro[coluna][linha+1].forca) {
					ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null);
		    		prop.updateValue(1, true);
		    		this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);
		    		ObsProperty prop2 = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha+1].identificador, null);
		    		prop2.updateValue(1, true);
		    		this.desativarJogador(this.tabuleiro[coluna][linha+1].identificador,this.tabuleiro[coluna][linha+1].time);
					this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
					this.tabuleiro[coluna][linha+1] = new Celula("livre","livre","livre",0);
					retorno = false;
				}else if(this.tabuleiro[coluna][linha+1].forca < 11) { // o atacado não bomba nem prisioneiro
					if(this.tabuleiro[coluna][linha].tipo == "espiao") { // o atacante for espião
						if(this.tabuleiro[coluna][linha+1].tipo == "general") { // e o atacado for general
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha+1].identificador, null); //espião ganha
				    		prop.updateValue(1, true);
				    		this.desativarJogador(this.tabuleiro[coluna][linha+1].identificador,this.tabuleiro[coluna][linha+1].time);
							this.incrementaLinha(coluna, linha, this.tabuleiro[coluna][linha].time,this.tabuleiro[coluna][linha].identificador);
							retorno = true;
						}else {// se atacado não for general
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null); // espião perde
							prop.updateValue(1, true);
							this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);				    		
							this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
							retorno = false;
						}
					}else if(forca>this.tabuleiro[coluna][linha+1].forca) { //se o atacante não for espião e a força do atacante for maior que a do atacado
						ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha+1].identificador, null); //atacante ganha
						this.desativarJogador(this.tabuleiro[coluna][linha+1].identificador,this.tabuleiro[coluna][linha+1].time);
			    		prop.updateValue(1, true);
			    		this.desativarJogador(this.tabuleiro[coluna][linha+1].identificador,this.tabuleiro[coluna][linha+1].time);
						this.incrementaLinha(coluna, linha, this.tabuleiro[coluna][linha].time,this.tabuleiro[coluna][linha].identificador);
						retorno = true;
					}else { // se a força do atacante não for maior que a do atacado
						ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null); //atacante perde
			    		prop.updateValue(1, true);
			    		this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);
						this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
						retorno = false;
					}
				}else { // se o atacado for bomba ou prisioneiro
					if(this.tabuleiro[coluna][linha+1].forca == 11) { //se for bomba
						if(this.tabuleiro[coluna][linha].tipo == "desarmador") {// e o atacante for desarmador
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha+1].identificador, null); //desarmador vence
				    		prop.updateValue(1, true);
				    		this.desativarJogador(this.tabuleiro[coluna][linha+1].identificador,this.tabuleiro[coluna][linha+1].time);
							this.incrementaLinha(coluna, linha, this.tabuleiro[coluna][linha].time,this.tabuleiro[coluna][linha].identificador);
							retorno = true;
						}else {// se o atacante não for desarmador, bomba explode e os dois saem do jogo
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null);
				    		prop.updateValue(1, true);
				    		this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);
				    		ObsProperty prop2 = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha+1].identificador, null);
				    		prop2.updateValue(1, true);
							this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
							this.tabuleiro[coluna][linha+1] = new Celula("livre","livre","livre",0);
							retorno = false;
						}
					}else if(this.tabuleiro[coluna][linha+1].forca == 12) {//se for prisioneiro
						if(this.tabuleiro[coluna][linha+1].time != this.tabuleiro[coluna][linha].time) {//e o time do prisioneiro for diferente do time do atacante
							defineObsProperty("jogoFinalizado"); // fim de jogo, time do atacante vence
							System.out.printf("**** FIM DE JOGO **** \n Vencedor %s", this.tabuleiro[coluna][linha].time);
							this.jogoFinalizado = true;
							retorno = true;
						}
					}
				}
			}else {//se for time vermelho
				if(this.tabuleiro[coluna][linha].forca == this.tabuleiro[coluna][linha-1].forca) {
					ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null);
		    		prop.updateValue(1, true);
		    		this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);
		    		ObsProperty prop2 = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha-1].identificador, null);
		    		prop2.updateValue(1, true);
		    		this.desativarJogador(this.tabuleiro[coluna][linha+1].identificador,this.tabuleiro[coluna][linha-1].time);
					this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
					this.tabuleiro[coluna][linha-1] = new Celula("livre","livre","livre",0);
					retorno = false;
				}else if(this.tabuleiro[coluna][linha-1].forca < 11) { // o atacado não bomba nem prisioneiro
					if(this.tabuleiro[coluna][linha].tipo == "espiao") { // o atacante for espião
						if(this.tabuleiro[coluna][linha-1].tipo == "general") { // e o atacado for general
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha-1].identificador, null); //espião ganha
				    		prop.updateValue(1, true);
				    		this.desativarJogador(this.tabuleiro[coluna][linha-1].identificador,this.tabuleiro[coluna][linha-1].time);
							this.incrementaLinha(coluna, linha, this.tabuleiro[coluna][linha].time,this.tabuleiro[coluna][linha].identificador);
							retorno = true;
						}else {// se atacado não for general
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null); // espião perde
				    		prop.updateValue(1, true);
				    		this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);
							this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
							retorno = false;
						}
					}else if(forca>this.tabuleiro[coluna][linha-1].forca) { //se o atacante não for espião e a força do atacante for maior que a do atacado
						ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha-1].identificador, null); //atacante ganha
			    		prop.updateValue(1, true);
			    		this.desativarJogador(this.tabuleiro[coluna][linha-1].identificador,this.tabuleiro[coluna][linha-1].time);
						this.incrementaLinha(coluna, linha, this.tabuleiro[coluna][linha].time,this.tabuleiro[coluna][linha].identificador);
						retorno = true;
					}else { // se a força do atacante não for maior que a do atacado
						ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null); //atacante perde
			    		prop.updateValue(1, true);
			    		this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);
						this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
						retorno = false;
					}
				}else { // se o atacado for bomba ou prisioneiro
					if(this.tabuleiro[coluna][linha-1].forca == 11) { //se for bomba
						if(this.tabuleiro[coluna][linha].tipo == "desarmador") {// e o atacante for desarmador
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha-1].identificador, null); //desarmador vence
				    		prop.updateValue(1, true);
				    		this.desativarJogador(this.tabuleiro[coluna][linha-1].identificador,this.tabuleiro[coluna][linha-1].time);
							this.incrementaLinha(coluna, linha, this.tabuleiro[coluna][linha].time,this.tabuleiro[coluna][linha].identificador);
							retorno = true;
						}else {// se o atacante não for desarmador, bomba explode e os dois saem do jogo
							ObsProperty prop = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha].identificador, null);
				    		prop.updateValue(1, true);
				    		this.desativarJogador(this.tabuleiro[coluna][linha].identificador,this.tabuleiro[coluna][linha].time);
				    		ObsProperty prop2 = getObsPropertyByTemplate("saiuDoJogo", this.tabuleiro[coluna][linha-1].identificador, null);
				    		prop2.updateValue(1, true);
							this.tabuleiro[coluna][linha] = new Celula("livre","livre","livre",0);
							this.tabuleiro[coluna][linha-1] = new Celula("livre","livre","livre",0);
							retorno = false;
						}
					}else if(this.tabuleiro[coluna][linha-1].forca == 12) {//se for prisioneiro
						if(this.tabuleiro[coluna][linha-1].time != this.tabuleiro[coluna][linha].time) {//e o time do prisioneiro for diferente do time do atacante
							defineObsProperty("jogoFinalizado"); // fim de jogo, time do atacante vence
							System.out.printf("**** FIM DE JOGO **** \n Vencedor %s", this.tabuleiro[coluna][linha].time);
							this.jogoFinalizado = true;
							retorno = true;
						}
					}
				}
			}
			return retorno;
	}
	
	boolean atacar(String nome, int forca, String time, int coluna, int linha) {
		boolean retorno = false;
		if(time.equals(this.jogada)) {
			if(time.equals("timeAzul")) {
				if(this.tabuleiro[coluna][linha+1].identificador.equals("livre")) {
					System.out.printf("Ação não permitida! Celula [%d][%d] está livre \n", coluna, linha+1);
					retorno = false;
				}else {
					if(this.tabuleiro[coluna][linha+1].time.equals("timeVermelho")) {
						retorno = this.atacaOponente(coluna, linha, forca);
					}					
				}
			}else if(time.equals("timeVermelho")) {
				if(this.tabuleiro[coluna][linha-1].identificador.equals("livre")) {
					System.out.printf("Ação não permitida! Celula [%d][%d] está livre \n", coluna, linha-1);
					retorno = false;
				}else {
					if(this.tabuleiro[coluna][linha+1].time.equals("timeVermelho")) {
						retorno = this.atacaOponente(coluna, linha, forca);
					}	
				}
			}
		}else{
			System.out.printf("Ação não permitida! A jogada atual é do time %s \n", this.jogada);
			retorno = false;
		}
		return retorno;
	}
	
	@OPERATION
	void passaJogada(String time) {
		Random random = new Random();
		if (this.jogada.equals("timeAzul")) {
			this.jogador = this.ativoAzul.get(random.nextInt(this.ativoAzul.size()));
			this.jogada = "timeAzul";
			ObsProperty prop = getObsPropertyByTemplate("jogada", null, null);
    		prop.updateValue(0, this.jogada);   
    		prop.updateValue(1, this.jogador);
		}else {
			this.jogador = this.ativoVermelho.get(random.nextInt(this.ativoVermelho.size()));			
			this.jogada = "timeVermelho";
			ObsProperty prop = getObsPropertyByTemplate("jogada", null, null);
    		prop.updateValue(0, this.jogada);   
    		prop.updateValue(1, this.jogador);			
		}
	}
	
	@OPERATION
	void registrar(String nome, String tipo, String time, int forca) {
		System.out.printf("****** agente "+ nome+" Tipo: "+ tipo+ " Time: "+ time +" ****** \n");
		if(time.equals("timeAzul")) {
			Celula celula = new Celula(nome,time,tipo,forca);
			//adicionar celula no array timeAzul
			this.timeAzul[contAzul] = celula;
            this.ativoAzul.add(nome);
			this.contAzul++;
		}
		if(time.equals("timeVermelho")) {
			Celula celula = new Celula(nome,time,tipo, forca);
			//adicionar celula no array timeVermelho
			this.timeVermelho[contVermelho] = celula;
            this.ativoVermelho.add(nome);
			this.contVermelho++;
		}
		if(this.contAzul == 14 && this.contVermelho == 14) {
			this.embaralhar(this.timeAzul);
			this.embaralhar(this.timeVermelho);
			this.contAzul = 0;
			this.contVermelho = 0;
			this.entrarNoTabuleiro();			
		}
	}
	
	@OPERATION
	void jogar(String nome, String time, String acao, int coluna, int linha, OpFeedbackParam success) {
		System.out.printf("Jogar! Nome: %s, Time: %s, Acao %s, Coluna: [%d], Linha: [%d]\n",nome, time, acao, coluna, linha);
		boolean retorno = false;
		if(time.equals(this.jogada)) {
			if(time.equals("timeAzul")) {				
				if(acao.equals("avancar")) {
					if(this.tabuleiro[coluna][linha+1] != null) {
						if(this.tabuleiro[coluna][linha+1].identificador.equals("livre")) {
							System.out.printf("Ação executada! Agente %s foi para Celula [%d][%d] \n", nome, coluna, linha+1);
							this.incrementaLinha(coluna, linha, time, nome);
							success.set(true);
						}else {
							System.out.printf("Ação não permitida! Celula [%d][%d] não está livre \n", coluna, linha+1);
							success.set(false);
						}
					}else {
						success.set(false);
						System.out.printf("Ação falhou! getIdentificador null Coluna [%d] linha [%d] identificador %s\n", coluna, linha+1,this.tabuleiro[coluna][linha+1].time);
					}
				}else if(acao.equals("recuar")) {
					if(this.tabuleiro[coluna][linha-1].identificador.equals("livre")) {
						this.decrementaLinha(coluna, linha, time, nome);
						System.out.printf("Ação executada! Agente %s foi para Celula [%d][%d] \n", nome, coluna, linha-1);
						success.set(true);
					}else {
						System.out.printf("Ação não permitida! Celula [%d][%d] não está livre \n", coluna, linha-1);
						success.set(false);
					}
				}else if(acao.equals("atacar")) {
					retorno = this.atacar(nome, this.tabuleiro[coluna][linha].forca, time, coluna, linha);
					success.set(retorno);
				}
			}else if(time.equals("timeVermelho")) {
				if(acao.equals("avancar")) {
					if(this.tabuleiro[coluna][linha-1] != null) {
						if(this.tabuleiro[coluna][linha-1].identificador.equals("livre")) {
							System.out.printf("Ação executada! Agente %s foi para Celula [%d][%d] \n", nome, coluna, linha-1);
							this.decrementaLinha(coluna, linha, time, nome);
							success.set(true);
						}else {
							System.out.printf("Ação não permitida! Celula [%d][%d] não está livre \n", coluna, linha-1);
							success.set(false);
						}
					}else {
						System.out.printf("Ação falhou! getIdentificador null Coluna [%d] linha [%d] identificador %s\n", coluna, linha+1,this.tabuleiro[coluna][linha-1].identificador);
						success.set(false);
					}
				}else if(acao.equals("recuar")) {
					if(this.tabuleiro[coluna][linha+1].identificador.equals("livre")) {
						this.incrementaLinha(coluna, linha, time, nome);
						System.out.printf("Ação executada! Agente %s foi para Celula [%d][%d] \n", nome, coluna, linha+1);
						success.set(true);
					}else {
						System.out.printf("Ação não permitida! Celula [%d][%d] não está livre \n", coluna, linha+1);
						success.set(false);
					}
				}else if(acao.equals("atacar")) {
					retorno = this.atacar(nome, this.tabuleiro[coluna][linha].forca, time, coluna, linha);
					success.set(retorno);
				}
			}
			System.out.printf("\n \n");
			this.mudaJogada();
		}else{
			System.out.printf("Ação não permitida! A jogada atual é do time %s \n", this.jogada);
			success.set(false);
		}
	}	
}

