
package data;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


 /**
 * 头像类。
 */
public class Portrait extends ImageIcon{

	private int num;
	private String path;
	
	public Portrait() {
		this(1);
	}
	public Portrait(int num) {
		super();
		try {
			this.num = num;
			path = "/client/images/portrait/"+num+".jpg";
			setImage(ImageIO.read(Portrait.class.getResource(path)));
		} catch (IOException e) {
			System.out.println("加载头像时发生错误"+e.getMessage());
		}
	}
	
	public Portrait(int num,Integer state) {
		super();
		try {
			this.num = num;
			if(state==2)
				path = "/client/images/portrait/"+num+"_h.jpg";
			else
				path = "/client/images/portrait/"+num+".jpg";
			setImage(ImageIO.read(Portrait.class.getResource(path)));
		} catch (IOException e) {
			System.out.println("加载头像时发生错误"+e.getMessage());
		}
	}
	
	public Portrait(int num,Integer state,boolean isBig) {
		super();
		try {
			this.num = num;
			if(state==2){
				if(isBig)
					path = "/client/images/portrait/"+num+"_h.jpg";
				else
					path = "/client/images/portrait/"+num+"_m_h.jpg";
			}
			else{
				if(isBig)
					path = "/client/images/portrait/"+num+".jpg";
				else
					path = "/client/images/portrait/"+num+"_m.jpg";
			}
			//System.out.println(path);
			setImage(ImageIO.read(Portrait.class.getResource(path)));
		} catch (IOException e) {
			System.out.println("加载头像时发生错误"+e.getMessage());
		}
	}
	
	public Portrait(String path){
		super(path);
	}
	
	public String getPath() {
		return path;
	}
	
	public String toString() {
		return path;
	}
	public int getNum() {
		return num;
	}
}
