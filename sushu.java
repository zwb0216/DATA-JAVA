package shujujiegou;
import java.util.Scanner;
//定义法求素数
public class sushu 
{
	public static void main(String[] args)
	{//输入的x<y
		Scanner in=new Scanner(System.in);
		System.out.print("上限:");
		int x=in.nextInt();
		System.out.print("下限:");
		int y=in.nextInt();
		for(;x>=y;x--)
		{
			if(PR(x))
				System.out.println(x);
		}
	}
public static boolean PR(int n)
{
		   
			boolean isPrime = true;
			for(int i=n-1;i>1;i--)
                {if(n%i==0)
				 isPrime = false; }
			
			  
			return isPrime;  
}
}