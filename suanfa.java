package shujujiegou;
//�����������е����ַ���
class suanfa{
//�㷨1,ʱ�临�Ӷ�ΪO(n^3)
	public static int maxSum1( int [ ] a)
{
 int maxSum =0;
  for (int i=0;i<a.length;i++)
	  for(int j=1;j<a.length;j++)
	  {
		  int thisSum=0;
		  for(int k=i;k<=j;j++)
			  thisSum+=a[k];
		  if(thisSum > maxSum)
          maxSum=thisSum;			  
	  }
return maxSum;
}
//�㷨2,O(n^2)
	public static int maxSum2(int []a)
	{
		int maxSum =0;
		for (int i=0;i<a.length;i++)
		{
			int thisSum=0;
			for (int j=i;j<a.length;j++)
			{
				thisSum+=a[j];
				if(thisSum >maxSum)
					maxSum=thisSum;
					
			}
			return maxSum;
		}
		return maxSum;
		
}
//�㷨3,���β��ԣ�O(NLogN)
	public  static int maxSum3(int []a)
	{
		return maxSunRec(a,0,a.length-1);
		
	}
private static int maxSunRec(int[] a,int left, int right )
{
	if(left==right)
		if(a[left]>0)
			return a[left];
		else
			return 0;
	int center =(left +right)/2;
	int maxLeftSum = maxSunRec(a,left,center);
	int maxRightSum=maxSunRec(a,center+1,right);
    
	
	int maxLeftBorderSum=0,leftBorderSum=0;
	for(int i=center;i>=left;i--)
	{
		leftBorderSum+=a[i];
		if(leftBorderSum>maxLeftBorderSum)
			maxLeftBorderSum = leftBorderSum;
		
	}
	int maxRightBorderSum=0,rightBorderSum=0;
	for(int i=center+1;i<=right;i++)
	{
		rightBorderSum+=a[i];
		if(rightBorderSum>maxRightBorderSum)
			maxRightBorderSum = rightBorderSum;
		
	}
return max3(maxLeftSum,maxRightSum,
		maxLeftBorderSum+maxRightBorderSum);

}
private static int max3(int maxLeftSum, int maxRightSum, int i) {
	// TODO Auto-generated method stub
	return 0;
}
//�㷨4,�����㷨��O(n)
public static int maxSum4(int []a)
{
	int maxSum=0,thisSum=0;
	for(int j=0;j<a.length;j++)
	{
		thisSum+=a[j];
		if(thisSum>maxSum)
			maxSum=thisSum;
		else if(thisSum <0)
			thisSum=0;
		
	}
return maxSum;
}

			
}
