package shujujiegou;

public class »¥ÎªËØÊı {
	public static double proRelPrim(int n)
	{
		int rel=0,tot=0;
		for(int i=1;i<=n;i++)
			for(int j=i+1;j<=n;j++)
			{
				tot++;
				if(gcd(i,j)==1)
					rel++;
			}
		return (double)rel/tot;
	}
	private static long gcd(long m,long n)
	{
		while (n!=0)
		{
			long rem=m%n;
			m=n;
			n=rem;
		}

	return m;
	}

}
