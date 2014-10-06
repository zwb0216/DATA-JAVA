package list;
/*约瑟夫环问题(Josephus)
 ** 用户输入a,b值，从1至a开始顺序循环数数，每数到b输出该数值，直至全部输出。
 **************************************************
 *利用JAVA的Arraylist类，实现顺序表，即利用动态数组实现顺序表，进而实现约瑟夫环。
 * 优点：在不知道数组（对象）长度的情况下，利用迭代器和接口，节省了构造链表的时间，利用这种模式，提高了代码的可利用率的编程效率。
 * 缺点：与Array类相比，即与传统数组相比，这种泛型的数组，无疑提高了时间复杂度，因而速度上差些。
 * 
 * */
import java.util.ArrayList; 
import java.util.List; 
import java.util.Scanner; 
public class Joseph {
	 public static void main(String[] args)
	 { 
	        Scanner in = new Scanner(System.in);
	        System.out.printf("总人数是：");
	        int a = in.nextInt(); 
	        System.out.printf("被踢出的人序号：");
	        int b = in.nextInt(); 
	        List<Integer>list = new ArrayList<Integer>(); 
	        for(int i = 1;i <= a;++ i) 
	            list.add(i); 
	        int c = 0; 
	        while(list.size() > 1)
	        {
	        	c=c+b;
	        	c=c%(list.size())-1;
	        	if(c<0)
	        	{
	        		System.out.println(list.get(list.size()-1));
	        		list.remove(list.size()-1);
	        		c=0;
	        	}
	        	else
	        	{
	        		System.out.println(list.get(c));
	        		list.remove(c);
	        	}
	      }
	 }
}
