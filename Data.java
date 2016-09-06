package proj2;
import java.util.Random;

public class Data {
	private int n;
	private int bitStringLength;
	private String individual;
	
	//Constructor
	public Data(int x){
		setAlleleNumber(x);
		bitStringLength = 33; //11 bits in each individual; total 33
		setIndividual();
	}
	
	//Copy constructor
	public Data (int x, String vec){
		setAlleleNumber(x);
		this.individual = vec;
	}
	
	//setter for individual
	public void setIndividual(){
		individual = "";
		for(int i = 0; i < bitStringLength; i++){
			//get bit
			Random randInt = new Random();
			int temp = randInt.nextInt(2) ;
			individual += String.valueOf(temp);
		}
	}
	
	//getter for individual
	public String getIndividual(){
		return individual;
	}
	
	//get length of each individual
	public int getIndividualLength(){
		return individual.length();
	}
	
	//replacement for individual at index i
	public void replaceIdividualAtIndex(int index, char x){
		individual = changeCharInPosition(index, x, individual);
	}
	
	public String changeCharInPosition(int position, char ch, String str){
	    char[] charArray = str.toCharArray();
	    charArray[position] = ch;
	    return new String(charArray);
	}
	
	//get gene at index i
	public char getComponentAtIndex(int index){
		char a_char = individual.charAt(index);
		return a_char;
	}
	
	//convert bit strings to doubles
	public double[] convertVector(String data){
		//separate the super string into three individual strings
		String x1 = data.substring(0, 11);
		String x2 = data.substring(11, 22);
		String x3 = data.substring(22);
		
		//Convert x1, x2, x3 to double
		double convertedX1 = convertIndividual(x1);
		double convertedX2 = convertIndividual(x2);
		double convertedX3 = convertIndividual(x3);
	
		double[] returnVar = new double[n];
		returnVar[0] = convertedX1;
		returnVar[1] = convertedX2;
		returnVar[2] = convertedX3;
		
		return returnVar;
	}
	
	//encoding
	public double convertIndividual(String ind){
		String zero = "0";
		String one = "1";
		int signed = 0;
		char z = zero.charAt(0);
		char o = one.charAt(0);
		String temp = new String();
		temp = ind;
		//If negative
		if (temp.charAt(0) == o){
			signed = 1;
			temp = changeCharInPosition(0, z, temp);
			for(int i = 1; i < temp.length(); i++){
				if (temp.charAt(i) == o){
					temp = changeCharInPosition(i, z, temp);
				}
				else{
					temp = changeCharInPosition(i, o, temp);
				}
			}
		}
		
		int byteToInt = Integer.parseInt(temp, 2);
		double x = ((double)byteToInt/1023.0) * 10.0;
		
		if (signed == 1){
			x = (-1.0)*x;
		}

		return x;
	}
	
	
	
	//setter for allele number
	public void setAlleleNumber(int x){
		n = x;
	}

	//getter for allele number
	public int getAlleleNumber(){
		return n;
	}
	
	//tostring
	public String toString(){
		String geneString = "";
		geneString = getIndividual();
		return geneString;
	}
}