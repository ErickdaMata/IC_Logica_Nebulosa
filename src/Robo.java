import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Robo {

	private int posicaoI;
	private int posicaoJ;
	private final int ALCANCE = 5;
	private final int REGRAS = 6;
	private boolean chegou = false;
	private int sensorBaixo, sensorCima, sensorDireita, sensorEsquerda;
	
	public Robo() {
		setPosicaoI(0);
		setPosicaoj(0);
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
	
	public int[] leituraSensores(char campo[][], int TAMANHO){
		
		int posI = getPosicaoI(), posJ = getPosicaoJ(); 
		System.out.println("Robo["+posI+","+posJ+"]");
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
		
		return sensores();
	}
	
	public void movimentar(char campo[][], int TAMANHO) {
		
		int sensor[] = leituraSensores(campo, TAMANHO);
		
		campo[posicaoI][posicaoJ] = '_';
		
		if(sensor[1] > sensor[2]) {
			this.posicaoI++;
		} else {
			this.posicaoJ++;
		}
		
		campo[posicaoI][posicaoJ] = 'R';
		
		if((posicaoI+posicaoJ)==(TAMANHO-1)*2) {
			this.chegou = true;
		}
		
		//Fuzzyficação
		double fuzzyCaminhoLivre[][] = fuzzyficacaoCaminhoLivre(sensor);
		
		//Lógica Nebulosa para Andar
		//Defuzzyfucação
		
		int andar = logicaNebulosa(lerRegras("regras/regras.txt", REGRAS));
		
	}
	
	
	
	private int logicaNebulosa(String[] regras) {
		
		double somatorioPonderadoRegras = 1, somatorioParametroFuzzy = 1; 
		
		int indice_token = 0, tamanho_token = 0;
		double peso;
		
		String token;
		String parametro[] = new String[2];
		String condicaoAND[] = new String[10];
		String condicaoOR[] = new String[10];
		
		for (int nRegra = 0 ; nRegra < regras.length; nRegra++) {
			//Le uma regra
			String regra = regras[nRegra]; 
			System.out.println(regra);
			
			//Verica se contém múltiplos parâmetros
			if(regra.contains("[")) {
				
				//Obtém da regra lida, uam substring contendo toda expressão
				token = regra.substring(regra.indexOf("[")+1, regra.indexOf("]"));
				
				condicaoAND = token.split(" E ");
				
				peso = 0;
				for(int i = 0; i < condicaoAND.length; i++) {
					if(condicaoAND[i].contains("{")) {
						
						token = regra.substring(regra.indexOf("{")+1, regra.indexOf("}"));
								
						condicaoOR = token.split(" OU ");
						
						peso = minimo(peso, resultadoOR(condicaoOR));
								
					}else {
						parametro = condicaoAND[i].split("=");
						peso = minimo(peso, parametroDirecao(parametro[0], parametro[1]));	
					}
				
				}
				
				
				tamanho_token = token.length();
				
				peso = 0;
				
				somatorioParametroFuzzy += peso;
				//somatorioPonderadoRegras += defuzzyAndar(peso)*peso;
				
			
			//Caso não possua mais de um parâmetro
			} else {
				token = regra.substring(regra.indexOf("SE ")+3, regra.indexOf(" LIVRE"));
				
				parametro = token.split("=");
				peso = parametroDirecao(parametro[0], parametro[1]);
				
				somatorioParametroFuzzy += peso;
				//somatorioPonderadoRegras += defuzzyAndar(peso)*peso;
				
			}
			
			//INTERPRETA SUA CONDIÇÃO
			for(indice_token = 3; indice_token < regra.indexOf("entao"); indice_token += (tamanho_token+1)) {
				
				//Aplica a Fuzzyficação para cada parâmetro de CONDIÇÃO
				
				
				
			}
			
			System.out.println("PESO: "+peso);
			
		}
		
		//Repete até finaliar o conjunto de regras
		
		return (int)(somatorioPonderadoRegras/(somatorioParametroFuzzy == 0? 1 : somatorioParametroFuzzy ));
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
		case "POUCO":
			fuzzy = fuzzyCLPouco(leituraSensor);
			return fuzzy;
		default:
			return fuzzy;
		}
	}

	private double defuzzyficacao(String Intensidade, double fuzzyLivre) {
		
		if (Intensidade == "MUITO") {
			return defuzzyAndarMuito(fuzzyLivre);
		}
		if (Intensidade == "MEDIO") {
			return defuzzyAndarMedio(fuzzyLivre);
		}
		if (Intensidade == "POUCO") {
			return defuzzyAndarPouco(fuzzyLivre);
		}
		return 0;
	}

	private double[][] fuzzyficacaoCaminhoLivre(int sensor[]) {
		double caminhoLivre [][] = {
									{fuzzyCLPouco(sensor[0]),fuzzyCLMedio(sensor[0]),fuzzyCLMuito(sensor[0])},
									{fuzzyCLPouco(sensor[1]),fuzzyCLMedio(sensor[1]),fuzzyCLMuito(sensor[1])},
									{fuzzyCLPouco(sensor[2]),fuzzyCLMedio(sensor[2]),fuzzyCLMuito(sensor[2])},
									{fuzzyCLPouco(sensor[3]),fuzzyCLMedio(sensor[3]),fuzzyCLMuito(sensor[3])}
								   };
		

		for(int i = 0; i < 4;i++) {
			for(int j = 0; j < 3;j++) {
				System.out.print(caminhoLivre[i][j]+ ", ");
			}	
			System.out.println();
		}
		return caminhoLivre;
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


	private double resultadoOR(String[] condicaoOR) {
		
		String paramatro[] = condicaoOR[0].split("=");
		
		double pesoFinal = parametroDirecao(paramatro[0], paramatro[1]);
		
		for(int i = 1; i< condicaoOR.length; i++) {
			pesoFinal = maximo(pesoFinal, parametroDirecao(paramatro[0], condicaoOR[i]));
		}
		
		return pesoFinal;
	}

}

