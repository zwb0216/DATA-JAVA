/*TSP问题：假设有一个旅行商人要拜访N个城市，
 * 他必须选择所要走的路径，路径的限制是每个城市只能拜访一次
 * 而且最后要回到原来出发的城市。路径的选择目标是要求得的路径路程为所有路径之中的最小值
 * 粒子群算法PSO：
 * 。系统初始化为一组随机解，
 * 通过迭代搜寻最优值。但是它没有遗传算法用的交叉(crossover)以及变异(mutation)
 * ，而是粒子在解空间追随最优的粒子进行搜索。
 * 本次课程设计，以48城市的TSP问题为例，尝试讨论基于粒子群算法解决TSP问题
 * 利用随机方法，构建粒子群进行模拟
 */
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PSO {
	private int vPgd;//最优解的评价值
	private int bestT;//出现最优解的代数
    private int[] fitness;//种群适应度，每个个体的适应度
	private Random random;
	private int BN;
	private float w;//权重
	private int MAX;//迭代次数
	private int scale;//种群规模
	private int citynumber;//城市数量
	private int t;//目前的代数
	private int [][] distance;//距离矩阵
	private int [][] OP;//粒子群
	private ArrayList<ArrayList<PTO>> listV;//每个粒子的初始交换序列
	private int [][] Pd;//一个粒子在各代中出现的最优解
	private int[] VPD;//解的评价值
	private int[] Pgd;//每个粒子所经历的各代中，所记住的最优解
	private BufferedReader data;
   public PSO() {
   }
public  PSO(int n, int g, int s, float w) {
		this.citynumber= n;
		this.MAX = g;
		this.scale = s;
		this.w = w;
	}
   private void init(String filename) throws IOException {
	   //读取数据
		int[] x;
		int[] y;
		String s;
		data = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename)));
		distance = new int[citynumber][citynumber];
		x = new int[citynumber];
		y = new int[citynumber];
		for (int i = 0; i < citynumber; i++) {
			s = data.readLine();
			String[] str = s.split(" ");//分割字符串
			x[i] = Integer.valueOf(str[1]);//X坐标
			y[i] = Integer.valueOf(str[2]);//Y坐标
		}
		for (int i = 0; i < citynumber - 1; i++) {
			//计算距离矩阵
			distance[i][i] = 0; 
			for (int j = i + 1; j < citynumber; j++) {
				double rp = Math
						.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])
								* (y[i] - y[j])) / 10.0);//伪欧式公式
				int tp = (int) Math.round(rp);//四舍五入，取整数
				if (tp < rp) {
					distance[i][j] = tp + 1;
					distance[j][i] = distance[i][j];
				} 
				else {
					distance[i][j] = tp;
					distance[j][i] = distance[i][j];
				}
			}
		}
		distance[citynumber - 1][citynumber - 1] = 0;
		OP = new int[scale][citynumber];
		fitness = new int[scale];
		Pd = new int[scale][citynumber];
		VPD = new int[scale];
		Pgd = new int[citynumber];
		vPgd = Integer.MAX_VALUE;
		bestT = 0;
		t = 0;
		random = new Random(System.currentTimeMillis());
   }
   void initGroup() {
	   //初始化种群
		int i, j, k;
		for (k = 0; k < scale; k++)//种群数量
		{
			OP[k][0] = random.nextInt(65535) % citynumber;
		
			for (i = 1; i <citynumber;)//粒子个数
			{
				OP[k][i] = random.nextInt(65535) % citynumber;
				for (j = 0; j < i; j++) {
					if (OP[k][i] == OP[k][j]) {
						break;
					}
				}
				if (j == i) {
					i++;
				}
			}
		}
   }

   void initListV() {
		int ra;
		int raA;
		int raB;
		listV = new ArrayList<ArrayList<PTO>>();
        	for (int i = 0; i < scale; i++) {
			ArrayList<PTO> list = new ArrayList<PTO>();
			ra = random.nextInt(65535) % citynumber;
			for (int j = 0; j < ra; j++) {
				raA = random.nextInt(65535) % citynumber;
				raB = random.nextInt(65535) % citynumber;
				while (raA == raB) {
					raB = random.nextInt(65535) % citynumber;
				}
				PTO s = new PTO(raA, raB);
				list.add(s);
			}

			listV.add(list);
		}
	}

	public int evaluate(int[] chr) {
		int len = 0;
		//城市编码
		for (int i = 1; i < citynumber; i++) {
			len += distance[chr[i - 1]][chr[i]];
		}
		len += distance[chr[citynumber - 1]][chr[0]];
		return len;
	}
	// 求一个基本交换序列作用于编码arr后的编码
	public void add(int[] arr, ArrayList<PTO> list) {
		int temp = -1;
		PTO s;
		for (int i = 0; i < list.size(); i++) {
			s = list.get(i);
			temp = arr[s.getX()];
			arr[s.getX()] = arr[s.getY()];
			arr[s.getY()] = temp;
		}
	}
	//求两个编码的基本交换序列
	public ArrayList<PTO> minus(int[] a, int[] b) {
		int[] temp = b.clone();
		int index;//交换子
		PTO s;//交换序列
		ArrayList<PTO> list = new ArrayList<PTO>();
		for (int i = 0; i < citynumber; i++) {
			if (a[i] != temp[i]) {
				// 在temp中找出与a[i]相同数值的下标index
				index = findnumber(temp, a[i]);
				// 在temp中交换下标i与下标index的值
				changeIndex(temp, i, index);
				//记住交换子
				s = new PTO(i, index);
				//保存交换子
				list.add(s);
			}
		}
		return list;
	}
	// 在arr数组中查找number，返回number的下标
	public int findnumber(int[] arr, int num) {
		int index = -1;
		for (int i = 0; i < citynumber; i++) {
			if (arr[i] == num) {
				index = i;
				break;
			}
		}
		return index;
	}
	// 将数组arr下标index1与下标index2的值交换
	public void changeIndex(int[] arr, int index1, int index2) {
		int temp = arr[index1];
		arr[index1] = arr[index2];
		arr[index2] = temp;
	}
	//二维数组copy
	public void copyarr(int[][] from, int[][] to) {
		for (int i = 0; i < scale; i++) {
			for (int j = 0; j < citynumber; j++) {
				to[i][j] = from[i][j];
			}
		}
	}
	//一维数组copy
	public void copyarraynumber(int[] from, int[] to) {
		for (int i = 0; i < citynumber; i++) {
			to[i] = from[i];
		}
	}
	
	public void evolution() {
		int i, j, k;
		int len = 0;
		float ra = 0f;
		ArrayList<PTO> Vi;
		//迭代一次
		for (t = 0; t < MAX; t++) {
			//每个粒子
			for (i = 0; i < scale; i++) {
				if(i==BN) continue;
				ArrayList<PTO> Vii = new ArrayList<PTO>();
				// 更新速度
				// Vii=wVi+ra(Pid-Xid)+rb(Pgd-Xid)
				Vi = listV.get(i);
				// wVi+表示获取Vi中size*w取整个交换序列
				len = (int) (Vi.size() * w);
				for (j = 0; j < len; j++) {
					Vii.add(Vi.get(j));
				}
				// Pid-Xid
				ArrayList<PTO> a = minus(Pd[i], OP[i]);
				ra = random.nextFloat();
				// ra(Pid-Xid)
				len = (int) (a.size() * ra);
				for (j = 0; j < len; j++) {
					Vii.add(a.get(j));
				}
				ArrayList<PTO> b = minus(Pgd, OP[i]);
				ra = random.nextFloat();
				len = (int) (b.size() * ra);
				for (j = 0; j < len; j++) {
					PTO tt= b.get(j);
					Vii.add(tt);
				}
				// 保存新Vii
				listV.add(i, Vii);
				//Xid’=Xid+Vid
				add(OP[i], Vii);
			}
			// 计算新粒子群适应度，Fitness[max],选出最好的解
			for (k = 0; k < scale; k++) {
				fitness[k] = evaluate(OP[k]);
				if (VPD[k] > fitness[k]) {
					VPD[k] = fitness[k];
					copyarraynumber(OP[k], Pd[k]);
					BN=k;
				}
				if (vPgd > VPD[k]) {
					System.out.println("最佳长度"+vPgd+" 代数："+bestT);
					bestT = t;
					vPgd = VPD[k];
					copyarraynumber(Pd[k], Pgd);
				}
			}		
		}
	}

	public void solve() {
		int i;
		int k;
		initGroup();
		initListV();
		//每个粒子所经历的各代中，所记住的最优解
		copyarr(OP, Pd);
		// 计算新粒子群适应度，Fitness[max],选出最好的解
		for (k = 0; k < scale; k++) {
			fitness[k] = evaluate(OP[k]);
			VPD[k] = fitness[k];
			if (vPgd > VPD[k]) {
				vPgd = VPD[k];
				copyarraynumber(Pd[k], Pgd);
				BN=k;
			}
		}
		System.out.println("初始化");
		for (k = 0; k < scale; k++) {
			for (i = 0; i < citynumber; i++) {
				System.out.print(OP[k][i] + ",");
			}
			System.out.println();
			System.out.println("----------------->"+fitness[k]);
		evolution();//进化
		System.out.println("最后结果");
		for (k = 0; k < scale; k++) {
			for (i = 0; i < citynumber; i++) {
				System.out.print(OP[k][i] + ",");
			}
			System.out.println();
			System.out.println("-------------------->" + fitness[k]);
		System.out.println("最佳长度出现代数：");
		System.out.println(bestT);
		System.out.println("最佳长度");
		System.out.println(vPgd);
		System.out.println("最佳路径：");
		for (i = 0; i < citynumber; i++) {
			System.out.print(Pgd[i] + ",");
		}
	}
  }
}
	public static void main(String[] args) throws IOException {
		System.out.println("测试开始");
		PSO pso = new PSO(48, 6000, 30, 0.5f);
		pso.init("D://1.txt");//测试数据来源
		//http://elib.zib.de/pub/Packages/mp-testdata/tsp/tsplib/tsp/att48.tsp
		pso.solve();
	}

class PTO extends PSO
{
	private int x;
	private int y;

	public PTO(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void print()
	{
		System.out.println("x:"+this.x+" y:"+this.y);
	}
}
}
