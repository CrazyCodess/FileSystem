import java.util.ArrayList;
import java.util.TreeMap;

/**
 * MyDirectory 目录存储结构
 * 
 * @author Zeng Xiaoyang&Yan Demi&Ma Xinhao
 * 
 */
public class MyDirectory extends MyFile{
	private int inode_address = -1;// 记录文件对应Inode的索引
	private String name = "";//目录名字

	private TreeMap<Integer, Integer> tree = new TreeMap<Integer, Integer>();//子目录的树形结构

	public int getInode_address() {
		return inode_address;
	}

	public void setInode_address(int inode_address) {
		this.inode_address = inode_address;
	}

	/**
	 * String getName() 得到目录的名字
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * setName(String name) 设置目录的名字
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * TreeMap<INode, Integer> getTree() 得到该目录下的所有内容
	 * 
	 * @return
	 */
	public TreeMap<Integer, Integer> getTree() {
		return tree;
	}

	/**
	 * setTree(TreeMap<INode, Integer> tree) 设置存放目录文件的TreeMap
	 * 
	 * @param tree
	 */
	public void setTree(TreeMap<Integer, Integer> tree) {
		this.tree = tree;
	}

	/**
	 * setTree(INode inode, int sub) 向目录文件的TreeMap 添加数据
	 * 
	 * @param inode
	 * @param sub
	 */
	public void setTree(INode inode, int sub) {
		this.tree.put(inode.getMe(), sub);
	}
}
