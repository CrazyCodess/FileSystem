
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * String user; 用户 int length; 文件的长度 int right; 文件的读写权限//0只读/1只写 String
 * state;文件是否打开的标志 String modifytime; 文件的修改时间 address;//对应文件块的地址---即序号
 * 
 *  String path 路径；
 * @author Zeng Xiaoyang&Yan Demi&Ma Xinhao
 * 
 */
public class INode implements Serializable, Comparable<INode> {
	private String path="";//当前的地址
	private int length=0;//当前文件长度
	private String users="";//用户名
	private int right=1;// 0只读/1可读可写
	private String state="close";// 文件是否打开的标志
	private String modifytime;// 修改时间
	private int type=0; /* 0代表目录，1代表普通文件 */
	private int address=-1;// 对应文件块的地址---即序号
	private int father=-1;//父节点
	private int  me=-1;//自己的当前节点
	
	

	public int getFather() {
		return father;
	}

	public void setFather(int father) {
		this.father = father;
	}

	public int getMe() {
		return me;
	}

	public void setMe(int me) {
		this.me = me;
	}

	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getRight() {
		if (this.right == 0)
			return "R";
		else
			return "W";
	}

	public void setRight(int right) {
		this.right = right;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModifytime() {
		return modifytime;
	}

	/**
	 * setModifytime() 设定文件的最后修改日期
	 */
	
	public void setModifytime() {
		Date date = new Date();
		SimpleDateFormat adf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		// System.out.println(adf.format(date));
		this.modifytime = adf.format(date);
	}

	public String toString() {
		return this.getUsers() + "\t" + this.getLength() + "b\t"
				+ this.getRight() + "\t" + this.getModifytime();
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	/**
	 * Comparable 的方法，为了实现INode的有序；
	 */
	@Override
	public int compareTo(INode o) {

		return (this.modifytime.hashCode()+this.getType())-(o.modifytime.hashCode()+o.getType());
	}

}
