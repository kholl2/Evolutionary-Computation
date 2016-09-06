package proj2;
import java.util.*;

//import proj1.Data;

public class proj2 {

	public static void main(String[] args) {
		int alleleNumber = 3; //Number of individual components
		
		int N = 50;  //pop size
		int t_max = 100; //max generations
		double bestOfRun = 300.0; //best of run
		Data bestOfRunVector = new Data(alleleNumber);
		double prob = 0.0;
		double min = 0.5, max = 1.5;
		int tournSize = 0;
		double[] fitnessDistr = new double[N]; //fitness holder
		
		
		
		ArrayList<Data> data = new ArrayList<Data>(N);
		int t = 0;
		
		//Initialize Pop(0)
		for(int i = 0; i < N; i++){
			data.add(new Data(alleleNumber));
		}
		
		//Get fitness
		fitnessDistr = getFitnessDistr(data, N);
		
		double bestOfGeneration = fitnessDistr[0];
		int index = 0;
		double avgOfGen = fitnessDistr[0];
		//System.out.println("Fitness at gen 0 "+ fitnessDistr[0]+" corresponding to vector: "+data.get(index).getIndividual());
		
		for (int i = 1; i < N; i++){
			//System.out.println("Fitness at gen 0 "+ fitnessDistr[i]+" corresponding to vector: "+data.get(i).getIndividual());
			
			avgOfGen += fitnessDistr[i];
			if (fitnessDistr[i] < bestOfGeneration){
				bestOfGeneration = fitnessDistr[i];
				index = i;
			}
		}
		double[] bestOfGen = new double[(t_max/10) + 1];
		double[] AvOfGen = new double[(t_max/10) + 1];
		int j =0;
		
		Scanner in = new Scanner(System.in);
		int choice;
		System.out.println("Please enter the selection method that you want to use");
		System.out.print("Enter 1 for probabability, 2 for tournament, and 3 for ranking: ");
		choice = in.nextInt();
		
		if(choice == 2){
			System.out.print("What tournament size would you like to play (Typical values 2-5): ");
			tournSize = in.nextInt();
			if(tournSize == 2){
				System.out.print("Enter probability of selecting winner: ");
				prob = in.nextDouble();
			}	
		}
		
		if(choice == 3){
			System.out.println("Please enter the values for min and max");
			System.out.print("min: ");
			min = in.nextDouble();
			System.out.print("max: ");
			max = in.nextDouble();
		}
		
		
		System.out.println("Generation 0");
		System.out.println("Best of generation: "+ bestOfGeneration +" corresponding to vector: "+data.get(index));
		bestOfGen[j] = bestOfGeneration;
		
		System.out.println("Average of generation: "+ avgOfGen/N);
		AvOfGen[j] = avgOfGen/N;
		System.out.println("--------------------------------------------------------");
		
		
		
		while(t < t_max){
			
			ArrayList<Data> afterCrossover = new ArrayList<Data>(1);
			ArrayList<Data> afterMutation = new ArrayList<Data>(1);
			
			for (int i = 0; i < N; i++){
				
				//select pop(t+1)
				if(choice == 1){
					int x = selection(fitnessDistr);
					int y = selection(fitnessDistr);
					
					//crossover
					Data crossoverVector = crossover(data.get(x), data.get(y));
					afterCrossover.add(crossoverVector);
					double fit = fitness(crossoverVector);
					//best of Run
					if (fit < bestOfRun){
						bestOfRun = fit;
						bestOfRunVector = crossoverVector;
					}
					
					//mutation
					Data mutationVector = mutationApplied(crossoverVector);
					afterMutation.add(mutationVector);
					fit = fitness(mutationVector);
					
					//best of Run
					if (fit < bestOfRun){
						bestOfRun = fit;
						bestOfRunVector = mutationVector;
					}
				}
				
				
				if(choice == 2){
					
					Data x = tournamentSelection(fitnessDistr, tournSize, prob, data);
					Data y = tournamentSelection(fitnessDistr, tournSize, prob, data);
					
					//crossover
					Data crossoverVector = crossover(x, y);
					afterCrossover.add(crossoverVector);
					double fit = fitness(crossoverVector);
					//best of Run
					if (fit < bestOfRun){
						bestOfRun = fit;
						bestOfRunVector = crossoverVector;
					}
					
					//mutation
					Data mutationVector = mutationApplied(crossoverVector);
					afterMutation.add(mutationVector);
					fit = fitness(mutationVector);
					
					//best of Run
					if (fit < bestOfRun){
						bestOfRun = fit;
						bestOfRunVector = mutationVector;
					}
				}

				
				//select pop(t+1)
				if(choice == 3){
					int x = rankingSelection(min, max, fitnessDistr, data);
					int y = rankingSelection(min, max, fitnessDistr, data);
					
					//crossover
					Data crossoverVector = crossover(data.get(x), data.get(y));
					afterCrossover.add(crossoverVector);
					double fit = fitness(crossoverVector);
					//best of Run
					if (fit < bestOfRun){
						bestOfRun = fit;
						bestOfRunVector = crossoverVector;
					}
					
					//mutation
					Data mutationVector = mutationApplied(crossoverVector);
					afterMutation.add(mutationVector);
					fit = fitness(mutationVector);
					
					//best of Run
					if (fit < bestOfRun){
						bestOfRun = fit;
						bestOfRunVector = mutationVector;
					}
				}
			}

			data = afterMutation; //update data vector
			fitnessDistr = getFitnessDistr(data, N); //update fitness distribution
			
			//Get best and average at every 10th generation
			if ((t+1) % 10 == 0){
			    bestOfGeneration = fitnessDistr[0];
				avgOfGen = fitnessDistr[0];
				index = 0;
				
				for (int i = 1; i < N; i++){
					//System.out.println("Fitness at gen " +(t+1) +"::: "+ fitnessDistr[i]+" corresponding to vector: "+data.get(index));
					avgOfGen += fitnessDistr[i];
					if (fitnessDistr[i] < bestOfGeneration){
						bestOfGeneration = fitnessDistr[i];
						index = i;
					}
				}
				j++;
				avgOfGen = avgOfGen / N;
				System.out.println("Generation "+ (t+1));
				System.out.println("Best of generation is: "+ bestOfGeneration+" corresponding to vector: "+data.get(index) );
				System.out.println("Average of generation is: "+ avgOfGen);
				bestOfGen[j] = bestOfGeneration;
				AvOfGen[j] = avgOfGen;
				System.out.println("Best so far: "+ bestOfRun + " corresponding to vector: "+bestOfRunVector);
				System.out.println("--------------------------------------------------------");
			}
			t++;
		}
		
		System.out.println("Best of run is: "+ bestOfRun + " corresponding to vector: "+bestOfRunVector);
		System.out.println("The Best of generations in an array:");
		System.out.println(Arrays.toString(bestOfGen));
		System.out.println("The Average of generations in an array:");
		System.out.println(Arrays.toString(AvOfGen));
		
		in.close();
	}
	
	//function to get fitness distribution
	public static double[] getFitnessDistr(ArrayList<Data> data, int popSize){
		double[] fitnessDistr = new double[popSize];
		for (int i = 0; i< popSize; i++){
			double fit = fitness(data.get(i));
			fitnessDistr[i] = fit;
			//System.out.println(fitnessDistr[i]);
		}
		
		return fitnessDistr;
	}
	
	//calculate fitness
	public static double fitness(Data data){
		double f = 0.0;
		int n = data.getAlleleNumber();
		String currentbitString = data.getIndividual() ;
		//System.out.println(currentbitString);
		double[] currentVector = data.convertVector(currentbitString);
		for (int i = 0; i< n; i++){
			double x = currentVector[i]*currentVector[i];
			f += x;
		}
		
		return f;
	}
	
	//fitness proportionate selection
	public static int selection(double[] array){
		double sum = 0.0;
		double[] probability = new double[array.length];
		
		//get probability of selection for each vector
		for(int i = 0; i < array.length; i++){
			double s = array[i] + 1;
			double x = 1/s;
			sum += x;
		}
		for(int i = 0; i < array.length; i++){
			double s = array[i] + 1;
			double x = 1/s;
			probability[i] = x /sum;
		}
		
		//selection
		sum = 0.0;
		Random rand = new Random();
		double randNum = rand.nextDouble();
		int i = 0;
		sum = probability[i];
		while(sum < randNum){
			i++;
			sum += probability[i];
		}
		return i;
	}
	
	//tournament selection
	public static Data tournamentSelection(double[] array, int times, double prob, ArrayList<Data> data){
		int size = array.length-1;
		double[] tournamentPlayers = new double[times];
		 ArrayList<Data> temp  =  new ArrayList<Data>();
		 
		//generate random numbers to choose the tournament players
		for(int i = 0; i < times; i++){
			Random rand = new Random();
			int randomNum = rand.nextInt(((size-1) - 0) + 1) + 1;
			tournamentPlayers[i] = array[randomNum];
			temp.add(data.get(randomNum));
		}
		
		//get winner of tournament
		for(int i = 0; i < times-1; i++){
			int index = i;
			for(int j = i + 1; j < times; j++){
				if (tournamentPlayers[j] < tournamentPlayers[index])
                    index = j;
				double smallerNumber = tournamentPlayers[index]; 
				Data tempSave = temp.get(index);
				tournamentPlayers[index] = tournamentPlayers[i];
				temp.set(index, temp.get(i));
				tournamentPlayers[i] = smallerNumber;
				temp.set(i, tempSave);
			}
		}
		
		if(times == 2){
			Random rand = new Random();
			double randNum = rand.nextDouble();
			if(randNum < prob){
				return temp.get(0);
			}
			else{
				return temp.get(1);
			}
		}
		else{
			return temp.get(0);
		}
		
	}
	
	
	//Ranking selection
	public static int rankingSelection(double min, double max, double[] array, ArrayList<Data> data){
		ArrayList<Data> temp  =  new ArrayList<Data>();
		double[] sortFitness = new double[array.length];
		
		//sort individuals based on fitness
		for(int i = 0; i < array.length-1; i++){
			int index = i;
			for(int j = i + 1; j < array.length; j++){
				if (sortFitness[j] < sortFitness[index])
		               index = j;
				double smallerNumber = sortFitness[index]; 
				Data tempSave = data.get(index);
				sortFitness[index] = sortFitness[i];
				sortFitness[i] = smallerNumber;
				temp.add(tempSave);
			}
		}
		
		double expectedR = 0.0;
		double totalExpectedCopies = 0.0;
		
		double[] expectedCopies = new double[array.length];
		//Calculate E(r)
		for(int i = 0; i < array.length; i++){
			expectedR  = min + ((max - min)/(array.length-1))*(i); //i is r-1
			expectedCopies[i] = expectedR;
			totalExpectedCopies += expectedR;
		}
		
		double[] probailitiesOfRanks = new double[array.length];
		//Set the probabilities with which the individuals get selected
		for(int i =0; i < array.length; i++){
			probailitiesOfRanks[i] = expectedCopies[i]/totalExpectedCopies;
		}
		
		//selection
		double sum = 0.0;
		Random rand = new Random();
		double randNum = rand.nextDouble();
		int i = 0;
		sum = probailitiesOfRanks[i];
		while(sum < randNum){
			i++;
			sum += probailitiesOfRanks[i];
		}
		return i;
	}
	
	//crossover
	public static Data crossover(Data parentData1, Data parentData2){
		double p_c = 0.8; //Crossover probability
		Random rand = new Random();
		double randNum = rand.nextDouble();
		
		Data childData1 = new Data(parentData1.getAlleleNumber(),parentData1.getIndividual()) ;
		Data childData2 = new Data(parentData2.getAlleleNumber(),parentData2.getIndividual()) ;
		
		//get cutpoint
		Random randInt = new Random();
		int cutPoint = randInt.nextInt((32-1)+1) + 1;
		
		//apply crossover
		if (randNum < p_c){ 
			childData1 = crossoverHelper(parentData1, parentData2, cutPoint);
		}
		//return the best
		else{ 
			double fitness1 = fitness(childData1);
			double fitness2 = fitness(childData2);
			if (fitness1 < fitness2){
				return childData1;
			}
			else{
				return childData2;
			}
		}
		return childData1;
	}
	
	//Crossover applier function
	public static Data crossoverHelper (Data data1, Data data2, int cutPoint){
		
		Data childData1 = new Data(data1.getAlleleNumber(), data1.getIndividual());
		Data childData2 = new Data(data2.getAlleleNumber(), data2.getIndividual());

		//get children
		for (int i = 0; i < childData1.getIndividualLength(); i++){
			if(i >= cutPoint){
				childData1.replaceIdividualAtIndex(i,data2.getComponentAtIndex(i));
				childData2.replaceIdividualAtIndex(i, data1.getComponentAtIndex(i));
			}
		}
		
		double fitness1 = fitness(childData1);
		double fitness2 = fitness(childData2);
		
		//return child with best fitness
		if (fitness1 < fitness2){
			return childData1;
		}
		else{
			return childData2;
		}
	}
		
	//Mutation
	public static Data mutationApplied(Data data){
		double p_m = 0.01; //mutation rate
		Data mutData = new Data(data.getAlleleNumber(), data.getIndividual());
		String zero = "0";
		String one = "1";
		char z = zero.charAt(0);
		char o = one.charAt(0);
		
		//for each allele
		for (int i = 0; i < data.getIndividualLength(); i++){
			Random rand = new Random();
			double randNum = rand.nextDouble();
			char x = data.getComponentAtIndex(i); //get the allele
			if(randNum < p_m){
				if (x == z)
					mutData.replaceIdividualAtIndex(i, o);
				else
					mutData.replaceIdividualAtIndex(i, o);
			}
		}
		return mutData;
	}
}

