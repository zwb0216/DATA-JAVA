import java.math.BigInteger;
	import java.util.*;
public class fo {
		public static BigInteger fo(int n)
		{
			if(n==1 || n==2)
				return BigInteger.ONE;
			BigInteger a = BigInteger.ONE;
			BigInteger b = BigInteger.ONE;
			BigInteger sum = a.add(b);
			for(int i = 3;i < n;i++)
			{
				a = b;
				b = sum;
				sum = a.add(b);
			}
			return sum;
		}
		public static void main(String[] args)
		{
			Scanner in = new Scanner(System.in);
			int n = in.nextInt();    
		    System.out.println(fo(n));
		}
	}
