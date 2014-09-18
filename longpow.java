package shujujiegou;

public class longpow {
	public static long pow(long x,int n)
	{if(n==0)
		return 1;
	if(n==1)
		return x;
	if(isEven(n))
		return pow(x*x,n/2);
	else
		return pow(x*x,n/2)*x;
		

}

	private static boolean isEven(int n) {
		// TODO Auto-generated method stub
		return false;
	}
}