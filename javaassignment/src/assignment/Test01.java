package assignment;

public class Test01 {
	public static void main(String[] args) {
		
		double a;
		double b;
		
		a = Double.parseDouble(args[0]);
		b = Double.parseDouble(args[1]);
		
		if(a%b >1) {
			System.out.println("나머지가 1보다 크다!");
			
		} else {
			System.out.println("나머지가 1보다 작거나 같다!");
		}
		
		
	}
}
