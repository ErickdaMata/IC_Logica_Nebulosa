
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
		double fuzzyCL[][] = fuzzyficacaoCaminhoLivre(sensor);
		
		for(int i = 0; i < 4;i++) {
			for(int j = 0; j < 3;j++) {
				System.out.print(fuzzyCL[i][j]+ ", ");
			}	
			System.out.println();
		}
		
	}
	
	private double[][] fuzzyficacaoCaminhoLivre(int sensor[]) {
		double caminhoLivre [][] = {
									{fuzzyCLPouco(sensor[0]),fuzzyCLMedio(sensor[0]),fuzzyCLMuito(sensor[0])},
									{fuzzyCLPouco(sensor[1]),fuzzyCLMedio(sensor[1]),fuzzyCLMuito(sensor[1])},
									{fuzzyCLPouco(sensor[2]),fuzzyCLMedio(sensor[2]),fuzzyCLMuito(sensor[2])},
									{fuzzyCLPouco(sensor[3]),fuzzyCLMedio(sensor[3]),fuzzyCLMuito(sensor[3])}
								   };
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
	
	
}
