import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * MyFile  文件存储结构
 * String name 文件名字
 * substance 数据内容
 * @author Zeng Xiaoyang&Yan Demi&Ma Xinhao
 *
 */
public class MyFile /*extends MyDirectory*/ implements Serializable {
	private int inode_address=-1;//记录文件对应Inode的索引
	private String name="";//文件名字
	private String substance="";//数据内容
	
	public int getInode_address() {
		return inode_address;
	}
	public void setInode_address(int inode_address) {
		this.inode_address = inode_address;
	}
	/**
	 * getName() 得到文件名字
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * setName(String name) 设置文件
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * String getSubstance() 得到文件的内容
	 * @return
	 */
	public String getSubstance() {
		return substance;
	}
	/**
	 * setSubstance(String substance) 设置文件的内容
	 * @param substance
	 */
	public void setSubstance(String substance) {
		this.substance = substance;
	}
}