// Agent espiao in project my_stratego

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

generalLocalizado(timeVermelho) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha-1),TimeAdv,NomeAdv) & generalAdv(General) & (NomeAdv == General).
generalLocalizado(timeVermelho) :- .my_name(Nome) & .term2string(Nome, NomeStr) & posicao(Coluna,Linha,Time,NomeStr) & posicao(Coluna,(Linha+1),TimeAdv,NomeAdv) & generalAdv(General) & (NomeAdv == General).

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
	: inJogo & group(Time,_,_) & generalLocalizado(Time) 
<-
	.print("Vou Atacar o General");
	!recuperar_prisioneiro;
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
	: inJogo & group(Time,_,_) & podeRecuar(Time) 
<-
	.print("Vou Recuar");
	!recuar;
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
	: oponente(Time)

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
	
+!avancar:true <- !escolherJogada.
	
+!recuperar_prisioneiro	
	: inJogo & .my_name(Nome) & group(Time,_,_) & myPosition(Coluna,Linha) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<- 
	jogar(Nome,Time,atacar,Coluna,Linha, Success);
	.print("Recuperando prisioneiro ", Success);
	.
	
+!recuperar_prisioneiro <- !escolherJogada.

/* Individuais */

+!atacar_general
	: generalLocalizado & .my_name(Nome) & group(Time,_,_) & myPosition(Coluna,Linha) & .term2string(Nome, NomeStr) & jogada(_,NomeStr)
<- 
	jogar(Nome, Time, atacar, Coluna, Linha, Success);
	.print("Atacando general ", Success)
	.

+!atacar_general:true <- !escolherJogada.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }

