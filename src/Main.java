import java.util.Random;

public class Main {

	final static int TAMANHO = 30;
	final static int OBSTACULOS= 220;
	
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
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		while(!robo.finalizou()) {
			//Andar com o Robo
			robo.movimentar(campo, TAMANHO);
			desenharCampo(campo);
			try {
				Thread.sleep(1000);
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
		
		
		/**
		campo[1][0] = 'O';
		campo[1][2] = 'O';
		campo[1][3] = 'O';
		campo[1][6] = 'O';
		campo[2][3] = 'O';
		campo[3][4] = 'O';
		campo[3][6] = 'O';
		campo[4][0] = 'O';
		campo[4][6] = 'O';
		campo[4][8] = 'O';
		campo[5][2] = 'O';
		campo[6][3] = 'O';
		campo[6][5] = 'O';
		campo[6][8] = 'O';
		campo[7][5] = 'O';
		campo[8][3] = 'O';
		campo[8][5] = 'O';
		campo[8][8] = 'O';
		campo[8][9] = 'O';
		*/
	}
	
	static void desenharCampo(char campo[][]) {

		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		
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
