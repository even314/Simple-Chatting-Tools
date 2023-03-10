

package tools;

import java.util.Random;

/**
 * 数学类-随机数生成类 （来自网络资源）
 */
public class RandomUtil {

	/**
	 * 生成a-b的随机数
	 * @param a 整数a
	 * @param b 整数b
	 * @return a-b的随机数
	 */
	public static int randomInt(int a,int b){
		int t,n=0;
		if(a>b)
		{
			t=a;a=b;b=t;
		}
		t=(int)(Math.ceil(Math.log10(b)));//对b向上取整
		while(true)
		{
			n=(int)(Math.random()*Math.pow(10,t));//随机数*10的t次方
			if(n>=a && n<=b)
				break;
		}
		//System.out.println("生成的随机数如下:"+n);
		return n;
	}
	
	/**
	 * 返回0-a的随机数。
	 * @param a 整数a。
	 * @return 返回0-a的随机数。
	 */
	public static int randomInt(int a){
		return new Random().nextInt(a);
	}
}
