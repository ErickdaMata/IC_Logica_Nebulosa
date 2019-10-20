import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Robo {

	private int posicaoI;
	private int posicaoJ;
	private final int ALCANCE = 5;
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
		
		int andar = logicaNebulosa(lerRegras("regras/regras.txt", 4));
		
	}
	
	
	
	private int logicaNebulosa(String[] regras) {
		
		double somatorioPonderadoRegras = 1, somatorioParametroFuzzy = 1; 
		
		int indice_token = 0, tamanho_token = 0;
		double peso;
		String token;
		
		for (int i = 0 ; i < regras.length; i++) {
			//Le uma regra
			String regra = regras[0]; 
			System.out.println(regra);
			
			//INTERPRETA SUA CONDIÇÃO
			for(indice_token = 3; indice_token < regra.indexOf("entao"); indice_token += (tamanho_token+1)) {
				
				//Obtém da regra lida, uma substring até o próximo espaço vazio. Encontrando, por exemplo> INANIMADO=não
				token = regra.substring(indice_token, regra.indexOf(" ", indice_token));

				tamanho_token = token.length();
				//Aplica a Fuzzyficação para cada parâmetro de CONDIÇÃO
				
				//Verica se é parâmetro
				if(token.contains("=")) {
					
					//Calcula o valor para o parâmetro
					String parametro[] = token.split("=");
					
					peso = parametroDirecao(parametro[0], parametro[1]);
					
					System.out.println("Para "+ parametro[0] + " = " + parametro[1] + ": " + peso);
					
				
				//Caso não seja uma parâmatro
				} else {
					//Verifica se é um operador tipo E
					if(token.contains("E")) {
						
					}
					//Verifica se é um operador tipo OU
					if(token.contains("OU")){
						
					}
				}
				
			}
			
			
			
			//SOMA o resultado da Fuzzyficação ao "somatórioFuzzy"
			
			//Lê sua CONSEQUÊNCIA
			
			//Aplica a Defuzzyficação para o parâmetro
			
			//SOMA o resultado da Defuzzyficação*Fuzzy ao "somatórioPOnderadoDefuzzy"
			
		}
		
		//Repete até finaliar o conjunto de regras
		
		return (int)(somatorioPonderadoRegras/somatorioParametroFuzzy);
	}

	private double parametroDirecao(String direcao, String intensidade) {
		
		double fuzzy = -1;
		
		System.out.println("//"+direcao);
		System.out.println("//"+intensidade);
		
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
	
	private double minimo (int valores[]) {
		
		double menor = 0;
		
		for (int i = 0; i< valores.length; i++) {
			if (valores[i] < menor) {
				menor = valores[i];
			}
		}
		
		return menor;
	}
	
	private double maior (int valores[]) {
		
		double maior = 0;
		
		for (int i = 0; i< valores.length; i++) {
			if (valores[i]>maior) {
				maior = valores[i];
			}
		}
		
		return maior;
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
}
