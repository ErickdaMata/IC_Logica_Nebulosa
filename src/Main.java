import java.util.Random;

public class Main {

	final static int TAMANHO = 50;
	final static int OBSTACULOS= 180;
	
	final static int TESTE = 200;
	static int[] passos = new int[TESTE];
	
	public static void main(String[] args) 
	{
		
		int i, j;
		char campo[][] = new char[TAMANHO][TAMANHO];
		
		limparCampo(campo);
		
		Robo robo = new Robo();
		
		//Inclusão dos Obstáculos
		colocarObstaculos(campo);
		
		campo[robo.getPosicaoI()][robo.getPosicaoJ()] = 'R';
		
		
		desenharCampo(campo);

		delay(1500);
		
		while(!robo.finalizou()) {
			//Andar com o Robo
			robo.movimentar(campo, TAMANHO);
			
			desenharCampo(campo);
			
			delay(500);
		}
		
	}
	
	private static void delay(int i) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(i);
		} catch (Exception e) {
			// TODO: handle exception
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
		campo[0][0] = '_';
		campo[0][1] = 'O';
		campo[0][2] = '_';
		campo[0][3] = '_';
		campo[0][4] = '_';
		
		campo[1][0] = '_';
		campo[1][1] = '_';
		campo[1][2] = '_';
		campo[1][3] = 'O';
		campo[1][4] = '_';
		
		campo[2][0] = 'O';
		campo[2][1] = '_';
		campo[2][2] = 'O';
		campo[2][3] = '_';
		campo[2][4] = '_';
		
		campo[3][0] = '_';
		campo[3][1] = '_';
		campo[3][2] = '_';
		campo[3][3] = '_';
		campo[3][4] = '_';
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

	
	static void limparCampo(char campo[][]) {
		
		for (int i = 0; i<TAMANHO; i++) {
			for (int j = 0; j<TAMANHO; j++) {
				campo[i][j] = '_';
			}
		}
	}
	
}
