// Agent desarmador in project my_stratego

inJogo :- jogoIniciado & .my_name(Nome) & .term2string(Nome, NomeStr) & saiuDoJogo(NomeStr,false).

myPosition(Coluna,Linha) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,_,NomeStr).

posicaoAtaque(timeVermelho) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha-1),TimeAdv,_) & .print("coluna ", Coluna, " linha ", Linha," time ",Time," timeAdv ", TimeAdv) & (Time \== TimeAdv) & (TimeAdv \== "livre" ).
posicaoAtaque(timeAzul) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha+1),TimeAdv,_) & .print("coluna ", Coluna, " linha ", Linha," time ",Time," timeAdv ", TimeAdv) & (Time \== TimeAdv) & (TimeAdv \== "livre" ).

prisioneiroLocalizado(timeVermelho) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha-1),TimeAdv,prisioneiro) & Time \== TimeAdv.
prisioneiroLocalizado(timeAzul) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha+1),TimeAdv,prisioneiro) & Time \== TimeAdv.

podeAvancar(timeVermelho) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,_,NomeStr) & posicao(Coluna,(Linha-1),"livre","livre").
podeAvancar(timeAzul) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,_,NomeStr) & posicao(Coluna,(Linha+1),"livre","livre").

podeRecuar(timeVermelho) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,_,NomeStr) & posicao(Coluna,(Linha+1),"livre","livre").
podeRecuar(timeAzul) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,_,NomeStr) & posicao(Coluna,(Linha-1),"livre","livre").

bombaLocalizada(timeVermelho) :-  .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha-1),TimeAdv,bomba) & Time \== TimeAdv.
bombaLocalizada(timeAzul) :-  .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha+1),TimeAdv,bomba) & Time \== TimeAdv.

!registrar.

+jogada(_,NomeStr)
	: inJogo & .my_name(Nome) & .term2string(Nome, NomeStr)
<-
	.print("!!!",Nome," - Escolher Jogada!");
	!escolherJogada;	
	-jogada(_,NomeStr);
	.
	
+jogada(_,NomeStr)
	: not inJogo & group(Time,_,_) & .my_name(Nome)
<-	
	.print("!!!",Nome," - Estou fora!");
	passaJogada(Time);
	-jogada(_,NomeStr);
	.
	
+jogoIniciado
	: .my_name(Nome) & myPosition(Coluna,Linha)
<-
	 .print("#############",Nome," - My Position: ",Coluna,", ",Linha);
	 .
	 
+saiuDoJogo(NomeStr,true)
	: .my_name(Nome) & .term2string(Nome, NomeStr)
<-
	.print(Nome, " Saiu do Jogo ");
	.

+!escolherJogada
	: inJogo & group(Time,_,_) & prisioneiroLocalizado(Time) 
<-
	.print("Vou recuperar o prisioneiro");
	!recuperar_prisioneiro;
	.

+!escolherJogada
	: inJogo & group(Time,_,_) & bombaLocalizada(Time) 
<-
	.print("Vou Localizar Bomba");
	!localizar_bomba;
	.
	
+!escolherJogada
	: inJogo & group(Time,_,_) & posicaoAtaque(Time) 
<-
	.print("Vou Atacar");
	!atacar_oponente;
	.
	
+!escolherJogada
	: inJogo & group(Time,_,_) & podeAvancar(Time) 
<-
	.print("Vou Avançar");
	!avancar;
	.

+!escolherJogada
	: group(Time,_,_) & .my_name(Nome) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<-
	.print("Fora de Jogo");
	passaJogada(Time);
	.

+!escolherJogada
	: true
<-
	.print("Não é minha jogada")
	.

+!registrar
	: .my_name(Nome) & role(Tipo) & group(Time,_,_) & forca(Forca)
<- 
	.print("### Registrar Nome: ", Nome, " Tipo: ", Tipo, " Time: ", Time, " Forca: ", Forca)
	registrar(Nome, Tipo, Time, Forca);
	.
	
+!atualizarPlacar(true)
	:   oponente(Time)

<- 
	atualizaTime(Time);
	.
	
+!atualizarPlacar(false)
	:  group(Time,_,_) & .my_name(Nome) & .term2string(Nome, NomeStr) & saiuDoJogo(NomeStr,true)

<- 
	atualizaTime(Time);
	.
	
+!atualizarPlacar(_)
	:  true

<- 
	.print("Ataque mal sucedido");
	.


+!atacar_oponente
	: inJogo & .my_name(Nome) & group(Time,_,_) & myPosition(Coluna,Linha) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<- 
	.print(Nome, " Atacando");
	jogar(Nome,Time,atacar,Coluna,Linha, Success);	
	.print("Atacando oponente ", Success).

+!atacar_oponente:true <- !escolherJogada.

+!avancar
	: inJogo & .my_name(Nome) & group(Time,_,_) & myPosition(Coluna,Linha) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<- 
	jogar(Nome, Time, avancar, Coluna, Linha, Success);
	.print("Avançando ", Success);
	.
	
+!avancar:inJogo <- !escolherJogada.
	
+!recuperar_prisioneiro	
	: inJogo & .my_name(Nome) & group(Time,_,_) & myPosition(Coluna,Linha) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<- 
	jogar(Nome,Time,atacar,Coluna,Linha, Success);
	.print("Recuperando prisioneiro ", Success);
	.
	
+!recuperar_prisioneiro:inJogo <- !escolherJogada.


/* Individuais */

+!localizar_bomba
	: inJogo & bombaLocalizada & .my_name(Nome) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<- 
	.print("Bomba Localizada")
	!desarmar_bomba
	.
	
+!desarmar_bomba
	:inJogo & bombaLocalizada & .my_name(Nome) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<- 	
	jogar(Nome, Time, atacar, Coluna, Linha, Success);
	.print("Desarmando Bomba ", Success);
	.

+!desarmar_bomba
	:true 
<- 	
	!escolherJogada
	.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }


