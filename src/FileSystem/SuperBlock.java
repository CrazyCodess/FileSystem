import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * 
 * 
 * @author Zeng Xiaoyang&Yan Demi&Ma Xinhao
 *
 */
public class SuperBlock implements Serializable {
	private long totalcontent = 1000;
	private long alreadyuse = 0;
	private long freeuse = 1000;
	// private INode[] inodes = null;// i节点记录数据结构
	private ArrayList<Integer> inode_free = new ArrayList<Integer>();// 剩余inode节点
	private ArrayList<Integer> inode_busy = new ArrayList<Integer>();// 已用INode节点

	public long getTotalcontent() {
		return totalcontent;
	}

	public long getAlreadyuse() {
		return alreadyuse;
	}

	public void setAlreadyuse(long alreadyuse) {
		this.alreadyuse = alreadyuse;
		this.freeuse=this.getTotalcontent() - this.alreadyuse;
	}

	public long getFreeuse() {
		return this.freeuse;
	}

	public void setFreeuse(long freeuse) {
		this.freeuse = freeuse;
		this.alreadyuse=this.getTotalcontent() - this.getFreeuse();
	}
/**
 * getInode_free() 得到一个空闲的Inode的索引 -1表示获取失败
 * @return
 */
	public int getInode_free() {
		if(null!=this.inode_free&&this.inode_free.size()>0)
		{
			int tem=this.inode_free.get(0);
			if(tem>-1&&tem<100)
			{
				this.inode_free.remove(0);
				this.inode_busy.add(tem);
				return tem;
			}
		}
		return -1;
	}
/**
 * setInode_free(int inode_free) 将int inode_free的Inode索引设置为空闲
 * @param inode_free
 */
	public void setInode_free(int inode_free) {
		if (inode_free > -1&&inode_free<100) {
			if (this.inode_busy.contains(inode_free))
			{
				this.inode_busy.remove(inode_free);
			}
			this.inode_free.add(inode_free);
		} else {
			System.out.println("inode_free 操作出错！");
		}
	}
/**
 * getInode_busy() 得到一个忙的Inode的索引
 * @return -1表示获取失败
 */
	public int getInode_busy() {
		if(null!=this.inode_busy&&this.inode_busy.size()>0)
		{
			int tem=this.inode_busy.get(0);
			if(tem>-1&&tem<100)
			{
				this.inode_busy.remove(0);
				this.inode_free.add(tem);
				return tem;
			}
		}
		return -1;
	}
/**
 * setInode_busy(int inode_busy) 将inode_busy加入到忙的队列
 * @param inode_busy
 */
	public void setInode_busy(int inode_busy) {
		if (inode_busy > -1&&inode_busy<100) {
			if (this.inode_free.contains(inode_busy))
				this.inode_free.remove(inode_busy);
			this.inode_busy.add(inode_busy);
		} else {
			System.out.println("inode_busy 操作出错！");
		}
	}
}
