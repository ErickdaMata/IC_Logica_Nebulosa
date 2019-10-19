import java.util.Random;

public class Main {

	final static int TAMANHO = 10;
	final static int OBSTACULOS = 20;
	
	public static void main(String[] args) 
	{
		
		Robo robo = new Robo();
		
		int i, j;
		char campo[][] = new char[TAMANHO][TAMANHO];
		
		for (i = 0; i<TAMANHO; i++) {
			for (j = 0; j<TAMANHO; j++) {
				campo[i][j] = '_';
			}
		}
		
		//Inclusão dos Obstáculos
		colocarObstaculos(campo);
		
		campo[robo.getPosicaoI()][robo.getPosicaoJ()] = 'R';
		
		desenharCampo(campo);
		
		
		while(!robo.finalizou()) {
			//Andar com o Robo
			robo.movimentar(campo, TAMANHO);
			desenharCampo(campo);
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	
	static void colocarObstaculos(char campo[][]) {
		Random randomico = new Random();
		
		int rndI, rndJ;
		
		for (int i = 0 ; i<OBSTACULOS;) {
			
			rndI = randomico.nextInt(TAMANHO);
			rndJ = randomico.nextInt(TAMANHO);
			
			if(campo[rndI][rndJ] != 'O' && (rndI+rndJ !=0) && (rndI+rndJ != (TAMANHO-1)*2) ) {
				campo[rndI][rndJ] = 'O';
				i++;
			}
			
		}
	}
	
	static void desenharCampo(char campo[][]) {

		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		for (int i = 0; i<TAMANHO; i++) {
			for (int j = 0; j<TAMANHO; j++) {
				System.out.print(campo[i][j] + "|");
			}
			System.out.println("");
		}
	}
	
	static void movimentar(Robo robo, char campo[][]) {

		
		
	}
	
}
