import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * FileTools 文件操作的工具类
 * 
 * @author Zengxiaoyang&Yandemei&Ma Xinhao
 * 
 */
public class FileTools {
	/**
	 * Object read( String name) 读取name文件返回object类型的数据
	 * 
	 * @param name
	 * @return
	 */
	public static Object read(String name) {
		File file = new File(name);
		ObjectInputStream oin = null;
		Object obj = null;
		try {
			oin = new ObjectInputStream(new FileInputStream(name));
			obj = oin.readObject();
			// System.out.println("read"+obj);EOFException
		} catch (EOFException e1) {
		} catch (FileNotFoundException e) {

			write(name, null);
			read(name);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		try {
			if (null != oin)
				oin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ObjectInputStream关闭失败！");
		}
		return obj;

	}

	/**
	 * write(String name,Object o) 向name目录文件写入所给的对象
	 * 
	 * @param name
	 * @param o
	 */
	public static void write(String name, Object o) {
		File file = new File(name);
		ObjectOutputStream oout = null;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("创建原始数据文件失败！");
			}
		}

		try {

			oout = new ObjectOutputStream(new FileOutputStream(file));
			if (null != o) {
				oout.writeObject(o);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("读取内容文件失败！");
		} finally {
			try {
				if (null != oout)
					oout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("ObjectOutputStream关闭失败！");
			}
		}
	}
	
	
	public static void format(){
		
	}
}
