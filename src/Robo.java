import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Robo {

	private int posicaoI;
	private int posicaoJ;
	
	private final int ALCANCE = 5;
	private final int REGRAS = 11;
	private final int MAX_PARAM = 10;
	
	private final boolean VERBOSO = false;
	private boolean chegou = false;
	
	private int sensorBaixo, sensorCima, sensorDireita, sensorEsquerda;
	private double direcao[] = {0,0,0,0}; 
	
	public Robo() {
		setPosicaoI(0);
		setPosicaoj(0);
	}
	public Robo(int i, int j) {
		setPosicaoI(i);
		setPosicaoj(j);
	}
	
	public void reiniciar() {
		setPosicaoI(0);
		setPosicaoj(0);
		this.chegou = false;
		this.direcao[0] = this.direcao[1] =
				this.direcao[2] = this.direcao[3] = 0;
	}
	
	
	public double[] getPesoDirecao() {
		return direcao;
	}

	private void pesoDireita(double valor) {
		this.direcao[0] += valor;
	}
	
	private void pesoBaixo(double valor) {
		this.direcao[1] += valor;
	}
	
	private void pesoEsquerda(double valor) {
		this.direcao[2] += valor;
	}
	
	private void pesoCima(double valor) {
		this.direcao[3] += valor;
	}

	
	public boolean finalizou() {
		return chegou;
	}
	
	public int getPosicaoI() {
		return posicaoI;
	}
	
	private void setPosicaoI(int i) {
		this.posicaoI = i;
	}
	
	public int getPosicaoJ() {
		return posicaoJ;
	}
	
	public void setSensorBaixo(int sensorBaixo) {
		this.sensorBaixo = sensorBaixo;
	}
	
	public int getSensorBaixo() {
		return sensorBaixo;
	}
	
	public void setSensorCima(int sensorCima) {
		this.sensorCima = sensorCima;
	}
	
	public int getSensorCima() {
		return sensorCima;
	}
	
	public void setSensorDireita(int sensorDireita) {
		this.sensorDireita = sensorDireita;
	}
	
	public int getSensorDireita() {
		return sensorDireita;
	}
	
	public void setSensorEsquerda(int sensorEsquerda) {
		this.sensorEsquerda = sensorEsquerda;
	}
	
	public int getSensorEsquerda() {
		return sensorEsquerda;
	}
	
	private void setPosicaoj(int j) {
		this.posicaoJ = j;
	}
	
	public int[] sensores() {
		int sensor[] = {getSensorCima(), getSensorBaixo(), getSensorDireita(), getSensorEsquerda()};
		System.out.println("Livre Cima: "+ getSensorCima());
		System.out.println("Livre Baixo: "+ getSensorBaixo());
		System.out.println("Livre Direita: "+ getSensorDireita());
		System.out.println("Livre Esquerda: "+ getSensorEsquerda());
		return sensor;
	}
	
	public void leituraSensores(char campo[][], int TAMANHO){
		
		direcao[0] = direcao[1] = direcao[2] = direcao[3] = 0;
		
		int posI = getPosicaoI(), posJ = getPosicaoJ(); 
		LOG("Robo["+posI+","+posJ+"]");
		int p;
		
		//Sensor +i (para baixo)
		for (p = posI+1; p < TAMANHO && p < (posI+ALCANCE+1); p++) {
			if(campo[p][posJ] == 'O')
				break;
		}
		setSensorBaixo(p-(posI+1));
		
		//Sensor +j (para direita)
		for (p = posJ+1; p < TAMANHO && p < (posJ+ALCANCE+1); p++) {
			if(campo[posI][p] == 'O')
				break;
		}
		setSensorDireita(p-(posJ+1));
		
		
		//Sensor -j (para esquerda)
		for (p = posJ-1; p >= 0 && p > (posJ-ALCANCE-1); p--) {
			if(campo[posI][p] == 'O')
				break;
		}
		setSensorEsquerda((posJ-1)-p);
		
		
		//Sensor -i (para cima)
		for (p = posI-1; p >= 0 && p > (posI-ALCANCE-1); p--) {
			if(campo[p][posJ] == 'O')
				break;
		}
		setSensorCima((posI-1)-p);;
		
	}
	
	public void movimentar(char campo[][], int TAMANHO) {
		
		//leituraSensores(campo, TAMANHO);
		leituraSensores(campo, TAMANHO);
		
		//Inferência Nebulosa para decidir a intensidade e sentido
		int passos = inferenciaNebulosa(lerRegras("regras/regras.txt", REGRAS));
		
		//Movimento
		mover(campo, passos, direcao());
		
		//AVISA QUE CHEGOU AO DESTINO
		if((getPosicaoI()+getPosicaoJ())==(TAMANHO-1)*2) {
			LOG("CHEGOU");
			this.chegou = true;
		}
		
		
	}
	
	
	private char direcao() {
		
		int maior = melhor(direcao);
		
		switch (maior) {
		case 0:
			return 'D';
		case 1:
			return 'B';
		case 2:
			return 'E';
		case 3:
			return 'C';

		}
		
		
		return 0;
	}

	private int melhor(double[] direcao) {
		
		double maiorPeso = direcao[0];
		int melhor = 0;
		
		LOG("CIMA: " + direcao[3]);
		LOG("BAIXO: " + direcao[1]);
		LOG("DIREITA: " + direcao[0]);
		LOG("ESQUERDA: " + direcao[2]);
		
		for(int i = 1; i < direcao.length ; i++) {
			if (direcao[i]>maiorPeso) {
				maiorPeso = direcao[i];
				melhor = i;
			}
		}
		return melhor;
	}

	private void mover(char[][] campo, int passos, char direcao) {
		
		campo[getPosicaoI()][getPosicaoJ()] = '_';
		
		switch (direcao) {
		case 'B':
			setPosicaoI(getPosicaoI()+passos);
			setPosicaoj(getPosicaoJ());
			break;

		case 'C':
			setPosicaoI(getPosicaoI()-passos);
			setPosicaoj(getPosicaoJ());
			break;
		
		case 'D':
			setPosicaoI(getPosicaoI());
			setPosicaoj(getPosicaoJ()+passos);
			break;
		
		case 'E':
			setPosicaoI(getPosicaoI());
			setPosicaoj(getPosicaoJ()-passos);
			break;
			
		default:
			break;
		}
		
		campo[getPosicaoI()][getPosicaoJ()] = 'R';
		
		LOG(passos + " PASSOS para " + direcao);
	}

	private int inferenciaNebulosa(String[] regras) {
		
		double peso;
		double regraPonderada;
		double somatorioPonderadoRegras = 0, somatorioParametroFuzzy = 0;
		
		String grupoAND;
		String parametro;
		String intensidadeAndar, direcaoAndar;
		String condicaoAND[] = new String[MAX_PARAM];
		
		for (int nRegra = 0 ; nRegra < regras.length; nRegra++) {
			//Le uma regra
			String regra = regras[nRegra];
			
			//Obtém a intensidade da consequencia
			intensidadeAndar = regra.substring(regra.indexOf("ANDAR=")+("ANDAR=").length(),
									regra.indexOf(" para"));
			
			//Obtém a direção da regra
			direcaoAndar = regra.substring(regra.indexOf("para ")+("para ").length());
			
			//Verica se contém múltiplos parâmetros
			if(regra.contains("[")) {
				
				//Obtém da regra lida, uam substring contendo toda expressão
				grupoAND = regra.substring(regra.indexOf("[")+1, regra.indexOf("]"));
				
				//Separa as condiçoes do grupo em parametros compostos:
				//Exemplo: SENTIDO=INTENSIDADE OU {PARAMETEROS}
				condicaoAND = grupoAND.split(" E ");
				
				//Avalio o primeira paramatro de AND
				peso = avaliarParametro(condicaoAND[0]);
				
				for(int i = 1; i < condicaoAND.length; i++) {
					
					//Avaliar peso da condição
					peso = minimo(peso, avaliarParametro(condicaoAND[i]));
					
				}	
				somatorioParametroFuzzy += peso;
			
			//Caso não possua mais de um parâmetro
			} else {
				//Busca o parametro dentro da regra
				parametro = regra.substring(regra.indexOf("SE ")+3, regra.indexOf(" LIVRE"));
				
				//Avalia e obtem o peso do parametro
				peso = avaliarParametro(parametro);
				
				somatorioParametroFuzzy += peso;
			}
			
			//Multiplica a intensidade final da CONDICAO pelo fuzzy da CONSEQUENCIA
			regraPonderada = (peso*defuzzyficacao(intensidadeAndar, peso));

			pesoDirecao(regraPonderada, direcaoAndar);
			
			somatorioPonderadoRegras += regraPonderada;
			
			LOG((nRegra+1) + " P: "+peso+ " - D: " + defuzzyficacao(intensidadeAndar, peso));
		}
		
		//Repete até finaliar o conjunto de regras
		
		return (int)(somatorioPonderadoRegras/(somatorioParametroFuzzy == 0? 1 : somatorioParametroFuzzy ));
	}

	private void pesoDirecao(double regraPonderada, String direcaoAndar) {
		
		switch (direcaoAndar) {
		case "BAIXO":
			pesoBaixo(regraPonderada);
			break;
		case "CIMA":
			pesoCima(regraPonderada);
			break;
		case "DIREITA":
			pesoDireita(regraPonderada);
			break;
		case "ESQUERDA":
			pesoEsquerda(regraPonderada);
			break;	
		default:
			break;
		}
		
	}

	private double avaliarParametro(String parametroRecebido) {
		
		//Caso o parametro avaliado tenha um precedente OR
		if(parametroRecebido.contains("{")){

			//Calcula o resultado interno do OR
			return resultadoOR(parametroRecebido);
			
		} else {

			//Se não, ele será um parametro comum
			//Separamos em sentido e intensidade
			String[] parametro = parametroRecebido.split("=");
			
			//Passamos parametro[0] = sentido e parametro[1] = intensidade
			return parametroDirecao(parametro[0], parametro[1]);	
		}
		
	}
	
	private double resultadoOR(String grupoOR) {
		
		//Obtém da regra lida, uma substring contendo toda expressão sem chaver
		grupoOR = grupoOR.substring(grupoOR.indexOf("{")+1, grupoOR.indexOf("}"));
		
		//Extrai o grupo de parametros OU, DIREITA=NAO | POUCO 
		String condicaoOR[] = grupoOR.split(" OU ");
		
		//Obtem o primeiro parametro OU, DIRETA | NAO
		String parametro[] = condicaoOR[0].split("=");
		
		//Avalia o parametro recebido
		double pesoFinal = parametroDirecao(parametro[0], parametro[1]);
		
		for(int i = 1; i< condicaoOR.length; i++) {
			//Avalia do segundo parametro em dia, mantendo a direção inicial
			//Foi avaliado DIRETA=NAO, agora será avalida DIREITA=POUCO
			pesoFinal = maximo(pesoFinal, parametroDirecao(parametro[0], condicaoOR[i]));
			
		}

		return pesoFinal;
	}

	private double parametroDirecao(String direcao, String intensidade) {
		
		double fuzzy = -1;
		
		switch (direcao) {
		case "BAIXO":
			fuzzy = parametroIntensidade(getSensorBaixo(), intensidade);
			return fuzzy;
		case "CIMA":
			fuzzy = parametroIntensidade(getSensorCima(), intensidade);
			return fuzzy;
		case "DIREITA":
			fuzzy = parametroIntensidade(getSensorDireita(), intensidade);
			return fuzzy;
		case "ESQUERDA":
			fuzzy = parametroIntensidade(getSensorEsquerda(), intensidade);
			return fuzzy;
		default:
			return fuzzy;
		}
	}
	
	private double parametroIntensidade(double leituraSensor, String intensidade) {
		double fuzzy = -1;
		
		switch (intensidade) {
		case "MEIO":
			fuzzy = fuzzyCLMedio(leituraSensor);
			return fuzzy;
		case "MUITO":
			fuzzy = fuzzyCLMuito(leituraSensor);
			return fuzzy;
		case "NAO":
			fuzzy = fuzzyCLNao(leituraSensor);
			return fuzzy;
		case "POUCO":
			fuzzy = fuzzyCLPouco(leituraSensor);
			return fuzzy;
		default:
			return fuzzy;
		}
	}

	private double fuzzyCLNao(double x) {
		return x == 0? 1 : 0;
	}
	
	private double fuzzyCLPouco(double x) {
		if (x<1 || x>3) {
			return 0;
		} else {
			return (3-x)/2;
		}
	}
	
	private double fuzzyCLMedio(double x) {
		if (x<1 || x>5) {
			return 0;
		} else {
			if (x<3) {
				return (x-1)/2;
			}else {
				return (5-x)/2;
			}
		}
	}
	
	private double fuzzyCLMuito(double x) {
		if (x<3) {
			return 0;
		} else {
			return (x-3)/2;
		}
	}
	
	private double defuzzyficacao(String intensidade, double fuzzyCL) {
		
		if (intensidade.contains("MUITO")) {
			return defuzzyAndarMuito(fuzzyCL);
		}
		if (intensidade.contains("MEDIO")) {
			return defuzzyAndarMedio(fuzzyCL);
		}
		if (intensidade.contains("POUCO")) {
			return defuzzyAndarPouco(fuzzyCL);
		}
		return -1;
	}
	
	private double defuzzyAndarPouco(double x) {
		return 3-(x*2);
	}
	
	
	private double defuzzyAndarMedio(double x) {
		return 5-(x*2);
	}
	
	
	private double defuzzyAndarMuito(double x) {
		return (2*x)+3;
	}
	
	
	private double minimo (double pesoAtual, double pesoVizinho) {
		
		return pesoAtual > pesoVizinho ? pesoVizinho : pesoAtual;
	}
	
	private double maximo (double pesoAtual, double pesoVizinho) {
		
		return pesoAtual > pesoVizinho ? pesoAtual : pesoVizinho;
	}
	
	private String[] lerRegras(String local, int quantasRegras) {
		
		String regra;
		String regras[] = new String[quantasRegras];
		int i = 0;
		
		try {
			
			//Abre o arquivo de que contém as regras do sistema 
			FileInputStream 	arquivo = new FileInputStream("regras/regras");
			InputStreamReader	input 	= new InputStreamReader(arquivo);
			BufferedReader		buffer 	= new BufferedReader(input);

			//Le uma regra contida na linha do arquivo de texto
			regra = buffer.readLine();
			
			while (regra != null) {
				regras[i] = regra;
				regra = buffer.readLine();
				i++;
			}
			

			//Finaliza o buffer e o acesso ao arquivo
			buffer.close();
			arquivo.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao abrir o arquivo: " + e.toString());
		}
		
		
		return regras;
	}
	
	void LOG(String frase) {
		if (!VERBOSO) {
			return;
		}
		System.out.println("> " +frase);
	}
	
	void LOG(String[] frase) {
		if (!VERBOSO) {
			return;
		}
		for(int i = 0;i< frase.length;i++) {
			System.out.println(">["+i+"]> " +frase[i]);	
		}
	}
}

