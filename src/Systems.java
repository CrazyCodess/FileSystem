import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Systems {
	Scanner sc = new Scanner(System.in);// 从控制台读取数据

	public static SuperBlock sb = null;// 超级块 记录虚拟磁盘的总信息
	public static ArrayList<Users> users;// 用户名数组;
	public static ArrayList<INode> inodes = new ArrayList<INode>(100);// inode记录数据结构
	public static INode[] inodes2;
	public static ArrayList<Object> blocks = new ArrayList<>(100);// 文件块的结构；
	public static String name = null;// 当前登录用户名
	public static String password= null;// 当前登录密码
	public static int cnt;
	public static INode now_inode = null;// 当前节点
	public static Object now_file = null;

	// public static INode father;//父节点
	// public static INode me;//自己的当前节点

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Systems sts = new Systems();
		//int cnt;
		sts.init();// 初始化数据；
		sts.login();
		//System.out.println("yun");
	}

	public void init() {
	
		System.out.println("***************欢迎使用该文件管理系统*************");
		/*
		 * if(null!=FileTools.read("f:\\users.dat")) { //inodes=(INode[])
		 * FileTools.read("f:\\users.dat"); }
		 */
		sb = (SuperBlock) FileTools.read("f:\\super.dat");
		if(sb != null){
			inodes = (ArrayList<INode>) FileTools.read("f:\\inodes.dat");			
		}
//		if(inodes2!=null){
//			for(int i=0;i<inodes2.length;i++){
//				inodes.add(inodes2[i]);
//			}
//		}
		
		
		
		
		
		
		
		
		blocks=(ArrayList<Object>)FileTools.read("f:\\data.dat");
		if(null==blocks){
			blocks=new ArrayList<Object>(100);
			for(int i=0;i<100;i++){
				blocks.add(new MyFile());
			}
			FileTools.write("f:\\data.dat",blocks);
		}
		
		if (null == sb || sb.getAlreadyuse() == 0) {
			sb = new SuperBlock();
			
			for (int i= 0; i < 100; i++) {
					inodes.add(new INode());
				}				
			
			for (int i = 0; i < 100; i++) {
				sb.setInode_free(i);
			}
			//Users u=new Users("admin","admin");
			//regeist(u);
			
			FileTools.write("f:\\super.dat", sb);
		}
				
		users = (ArrayList<Users>) FileTools.read("f:\\users.dat");
		if (null == users) {
			users = new ArrayList<Users>();// 存放整个文件系统
			Users u=new Users("admin","admin");
//			u.setName("admin");
//			u.setPassword("admin");
			users.add(u);
			regeist(u);
			FileTools.write("f:\\users.dat", users);
		}
		
	}

	public void login() {

		
		System.out.println("请先登录->");
		System.out.print("user name:");
		name = sc.next();
		System.out.print("password:");
		password= sc.next();
		Users user=isInNames(name);
		if (user==null) {
			System.out.println("该用户名不存在！是否注册该用户？y/n");
			if ("y".equals(sc.next())) {
				Users u=new Users(name,password);
				
				if (regeist(u)) {
					System.out.println("regeist success！");
					login();
				} else {
					System.out.println("注册失败！");
					System.exit(0);
				}

			} else {
				login();
			}

		} else {
			
			
			if(name.equals(user.getName())&&password.equals(user.getPassword())){
				System.out.println("login success!!");
				now_inode = getInode(name+ "->");// 得到当前的inode
				now_file = blocks.get(now_inode.getAddress());// 得到当前的目录
				help();
				execute();
			}else {
				
				System.out.println("login fail!!");
				login();
			}
	
			
		}
	}

	/**
	 * 命令执行的主方法
	 */
	public void execute() {
		
		String commond = null;
		String cmd[] = null;// 操作命令数组 cmd[0] 操作的命令 cmd[1]操作的文件
		/*
		 * INode id = new INode();// 文件的节点 int fileNumber = 0;// 拥有文件的总数 int
		 * getNumber = 0;// 存储打开文件的节点索引号，或者说是虚拟的内存地址 int emptyNumber = 0;//
		 * 空的文件目录的索引
		 */// System.out.println(now_inode.getPath());

		while (true) {
			System.out.print(now_inode.getPath());
			commond = sc.nextLine();
			if (commond.equals(""))
				commond = sc.nextLine();
			cmd = commond.trim().split(" ");
			// 列举同一个人用户名的文件目录
			if (cmd[0].trim().equals("dir")) {
				int m = 0;
				
				if (now_file instanceof MyDirectory) {
					MyDirectory now__real_file = (MyDirectory) now_file;
					m = now__real_file.getTree().size();
					if (m == 0) {
						System.out.println("没有目录项");
					} else {
						System.out.println("文件名\t\t地址 \t只读0/可写1\t文件长度\t ");//修改
						Set<Integer> dir_inodes = now__real_file.getTree()
								.keySet();
						Iterator<Integer> iteratore = dir_inodes.iterator();
						while (iteratore.hasNext()) {

							Object file = blocks.get(now__real_file.getTree().get(
									iteratore.next()));
							if (file instanceof MyDirectory) {
								MyDirectory real_file = (MyDirectory) file;
								INode real_inode = inodes.get(real_file.getInode_address());
								// "文件名\t用户名\t地址\t文件长度\t只读1/可写2\t打开控制\t创建时间"
								System.out.println(real_file.getName() + "\t"
										
										+ "inode "+real_inode.getAddress() + "\t\t"
										+ real_inode.getRight() + "\t"
										+ real_inode.getLength() + "B\t"
										);

							} else {
								MyFile real_file = (MyFile) file;
								INode real_inode = inodes.get(real_file.getInode_address());
								System.out.println(real_file.getName() + "\t"
			
										+"inode "+ real_inode.getAddress() + "\t\t"
											+ real_inode.getRight() +  "\t"
										+ real_inode.getLength() + "B\t"
									);

							}

						}
						System.out.println("文件个数---" + m);
					}

				} else {
					MyFile now__real_file = (MyFile) now_file;
				}

			}
			// 创建文件
			else if (cmd[0].equals("create")) {

				int index = getFreeInode();
				if (index != -1) {
					MyFile my_file = new MyFile();
					my_file.setName(cmd[1]);
					INode inode = new INode();
					inode.setFather(now_inode.getMe());
					inode.setUsers(name);
					inode.setMe(index);
					inode.setModifytime();
					if (inode.getFather() == -1) {
						inode.setPath(name + "->");
					} else {
						inode.setPath(inodes.get(inode.getFather()).getPath()
								+ cmd[1] + "->");
					}
					inode.setRight(1);// 可写
					inode.setState("open");
					inode.setType(1);// 文件
					inode.setAddress(index);
					inodes.set(index, inode);
					my_file.setInode_address(index);
					MyDirectory real_file = (MyDirectory) now_file;
					blocks.set(index,my_file);
					real_file.getTree().put(index, index);
					System.out.println(cmd[1] + "文件已经打开！请输入内容,以###结束输入");
					StringBuffer content = new StringBuffer();
					while (true) {//修改
						String tem = sc.nextLine();
						if (tem.equals("###")) {
							System.out.println("文件输入结束");
							break;// 文件输入结束
						}else{
							content.append(tem + "\r\n");
						}
						
					}
					my_file.setSubstance(content.toString());
					inodes.get(index).setLength(content.length());
					inodes.get(index).setState("close");
					System.out.println(cmd[1] + "文件已关闭！");
					sb.setAlreadyuse(content.length());
					sb.setInode_busy(index);
				} else {
					System.out.println("inode申请失败！");
				}

			}
			// 创建文件目录
			else if (cmd[0].trim().equals("cdir")) {
				if(cmd.length==1){
					System.out.println("没有输入目录名！");//修改
					continue;
				}
				int index = getFreeInode();//取当前空闲的第一个inode下标
				if (index != -1) {
					MyDirectory my_file = new MyDirectory();
					my_file.setName(cmd[1]);
					INode inode = new INode();
					inode.setFather(now_inode.getMe());
					inode.setUsers(name);
					inode.setMe(index);
					inode.setModifytime();
					inode.setPath(now_inode.getPath() + cmd[1] + "->");
					System.out.println("新建的地址"+now_inode.getPath() + cmd[1] + "->");
					inode.setRight(1);// 可写
					inode.setType(0);//0表示目录
					inode.setAddress(index);
					inodes.set(index, inode);
					my_file.setInode_address(index);
					MyDirectory real_file = (MyDirectory) now_file;
					blocks.set(index,my_file);
					real_file.getTree().put(index, index);
					inodes.get(index).setLength(0);
					sb.setInode_busy(index);

				} else {
					System.out.println("inode申请失败！");
				}

			}
			// 删除文件的操作
			else if (cmd[0].trim().equals("delete")) {

				Object o = this.getFileByName(cmd[1]);
				if (null != o) {
					if (o instanceof MyDirectory) {
						MyDirectory o1 = (MyDirectory) o;

						if (o1.getTree().size() == 0) {
							int index = o1.getInode_address();
							sb.setInode_free(index);
							// 重置节点
							inodes.set(index, new INode());
							// 重置数据块
							blocks.set(o1.getInode_address(), new MyFile());
							// 在目录的tree中删除数据
							MyDirectory file = (MyDirectory) now_file;
							file.getTree().remove(index);

							System.out.println(o1.getName() + "目录已删除！");
						} else {
							System.out.println(o1.getName() + "目录不为空！不可以删除");
						}
					} else if (o instanceof MyFile) {
						MyFile o1 = (MyFile) o;

						int index = o1.getInode_address();
						// 设置超级快
						sb.setInode_free(index);
						sb.setFreeuse(inodes.get(index).getLength());
						// 重置节点
						inodes.set(index, new INode());
						// 重置数据块
						blocks.set(o1.getInode_address(), new MyFile());
						// 在目录的tree中删除数据
						MyDirectory file = (MyDirectory) now_file;
						file.getTree().remove(index);

						System.out.println(o1.getName() + "文件已删除！");

					} else {
						System.out.println(cmd[1] + "文件不存在！");
					}
				}

			}else if(cmd[0].trim().equals("attr")){
				
				int  flag=0;
				MyDirectory now_real_file=(MyDirectory)now_file;
				Set<Integer> dir_inodes = now_real_file.getTree()
						.keySet();
				Iterator<Integer> iteratore = dir_inodes.iterator();
				while(iteratore.hasNext()){
					Object file = blocks.get(now_real_file.getTree().get(iteratore.next()));
					if (file instanceof MyDirectory) {
						if(((MyDirectory) file).getName().equals(cmd[1])){
							INode search_inode=inodes.get(((MyDirectory) file).getInode_address());
						if(flag==0){
							System.out.println("文件名\t创建者\t地址\t文件长度\t   只读0/可写1\t打开控制\t创建时间");
							flag=1;
						}
							System.out.println(((MyDirectory)file).getName()+"\t"
									+search_inode.getUsers()+"\t"
									+"inode "+search_inode.getAddress()+"\t"
									+search_inode.getLength()+"B\t"
									+search_inode.getRight()+"\t"
									+search_inode.getState()+"\t"
									+search_inode.getModifytime()
									);
							
							
							
						}
					}
					else if(file instanceof MyFile){
						if(((MyFile) file).getName().equals(cmd[1])){
							INode search_inode=inodes.get(((MyFile) file).getInode_address());
							if(flag==0){
								System.out.println("文件名\t创建者\t地址\t文件长度\t   只读0/可写1\t打开控制\t创建时间");
								flag=1;
							}
							
							System.out.println(((MyFile)file).getName()+"\t"
									+search_inode.getUsers()+"\t"
									+"inode "+search_inode.getAddress()+"\t"
									+search_inode.getLength()+"\t"
									+search_inode.getRight()+"\t"
									+search_inode.getState()+"\t"
									+search_inode.getModifytime()
									);
							
						}
					}
				}
				if(flag==0)System.out.println("不存在此文件或文件夹");
				
			}
			else if (cmd[0].trim().equals("cd")) {
				if(cmd.length==1){
					System.out.println("cd后面加入.或者..或者子目录名");
					continue;
				}
				if (".".equals(cmd[1])) {

				} else if ("..".equals(cmd[1])) {
					if (now_inode.getFather() == -1) {
						System.out.println("当前目录为根目录！");
					} else {
						MyDirectory now_directory = (MyDirectory) now_file;
						now_inode = inodes.get(now_inode.getFather());
						System.out.println(now_inode.getFather());
						now_file = blocks.get(now_inode.getAddress());
					}
				} else {
					boolean flag=false;
					MyDirectory now_real_file=(MyDirectory)now_file;
					Set<Integer> dir_inodes = now_real_file.getTree()
							.keySet();
					Iterator<Integer> iteratore = dir_inodes.iterator();
					while(iteratore.hasNext()){
						Object file = blocks.get(now_real_file.getTree().get(iteratore.next()));
						if (file instanceof MyDirectory) {
							if(((MyDirectory) file).getName().equals(cmd[1])){
								now_file=(MyDirectory)file;
								now_inode=inodes.get(((MyDirectory) file).getInode_address());
								flag=true;
							}
						}
					}
					
					if(!flag)System.out.println("不存在此目录!!");
					
/*					Object o1 = getFileByName(cmd[1]);
					if (o1 instanceof MyDirectory) {
						MyDirectory o = (MyDirectory) o1;
						now_file = o;
						now_inode = inodes.get(o.getInode_address());
					} else {
						System.out.println("输入的目录不存在，请检查！");
					}
*/
				}

			} else if (cmd[0].trim().equals("open")) {
				boolean flag=false;
				if(cnt>5){
					System.out.println("你已经打开了5个文件！");
					continue;
				}
				for(int i=0;i<blocks.size();i++){
					Object o=blocks.get(i);
					if(o instanceof MyFile){
						if(((MyFile)o).getName().equals(cmd[1])){
							INode inode=inodes.get(((MyFile)o).getInode_address());
							if(inode.getState().equals("open")){
								System.out.println("该文件已被打开！");
								flag=true;
							}
							else{
								
								inode.setState("open");
								System.out.println("文件打开成功！");
								cnt++;
								flag=true;
							}
						}
					}
					
				}
				if(!flag)System.out.println("文件不存在！");
			
			}

			else if (cmd[0].trim().equals("close")) {
				boolean flag=false;
			
				for(int i=0;i<blocks.size();i++){
					Object o=blocks.get(i);
					if(o instanceof MyFile){
						if(((MyFile)o).getName().equals(cmd[1])){
							INode inode=inodes.get(((MyFile)o).getInode_address());
							if(inode.getState().equals("close")){
								System.out.println("该文件已被关闭！");
								flag=true;
							}
							else{
								
								inode.setState("close");
								System.out.println("文件关闭成功！");
								cnt--;
								flag=true;
							}
						}
					}
					
				}
				if(!flag)System.out.println("文件不存在！");
				
			} else if (cmd[0].trim().equals("rename")) {

				// System.out.println("文件" + file[0] + "已经关闭");
				if (rename(cmd)) {
					System.out.println("重命名成功！");
				} else {
					System.out.println("重命名失败！");
				}

			}
			// read操作（文件已经打开的话可移执行文件的读操作，如果文件没有打开，则可以执行文件的读操作否则不可以）
			else if (cmd[0].trim().equals("read")) {

				Object o = this.getFileByName(cmd[1]);
				if (null != o) {
					if (o instanceof MyDirectory) {
						MyDirectory o1 = (MyDirectory) o;
						System.out.println(o1.getName() + "目录不能执行此命令！");
					} else if (o instanceof MyFile) {

						MyFile o1 = (MyFile) o;
						System.out.println(o1.getName() + "文件内容如下：");
						System.out.println(o1.getSubstance().substring(0,
								o1.getSubstance().lastIndexOf("\r\n")));
					}
				}
			} else if (cmd[0].trim().equals("write")) {

				Object o = this.getFileByName(cmd[1]);
				if (null != o) {
					if (o instanceof MyDirectory) {
						MyDirectory o1 = (MyDirectory) o;
						System.out.println(o1.getName() + "目录不能执行此命令！");
					} else if (o instanceof MyFile) {
						MyFile o1 = (MyFile) o;
						// System.out.println(o1.getName());
						System.out.println("1.续写;2.重写; 请选择");
						String select = sc.next();
						while (true) {

							if ("1".equals(select)) {
								System.out.println("请输入续写的数据，以###结束");
								StringBuffer content = new StringBuffer(o1
										.getSubstance().substring(
												0,
												o1.getSubstance().lastIndexOf(
														"\r\n")));
								while (true) {
									String tem = sc.next();
									if (tem.equals("###")) {
										System.out.println("文件输入结束");
										break;// 文件输入结束
									}else{
										content.append(tem + "\r\n");
									}
									
								}
								o1.setSubstance(content.toString());
								System.out.println("续写操作成功！");
								break;

							} else if ("2".equals(select)) {
								System.out.println("请输入重写的数据，以###结束");
								StringBuffer content = new StringBuffer();
								while (true) {
									String tem = sc.next();
									if (tem.equals("###")) {
										System.out.println("文件输入结束");
										break;// 文件输入结束
									}else{
										content.append(tem + "\r\n");
									}
									
								}
								o1.setSubstance(content.toString());
								System.out.println("重写操作成功！");
								break;

							} else {
								System.out.println("输入错误，请重新输入！");
								select = sc.next();
							}
						}
					}
				} else {
					System.out.println("输入错误，请重新输入！");

				}
			}
			// 退出操作---保存数据
			else if (cmd[0].trim().equals("exit")) {
				
				FileTools.write("f:\\super.dat", sb);
				FileTools.write("f:\\users.dat",users);
				FileTools.write("f:\\inodes.dat", inodes);
				FileTools.write("f:\\data.dat", blocks);
				
				
				System.exit(0);
			}else if(cmd[0].trim().equals("logout")){
				FileTools.write("f:\\super.dat", sb);
				FileTools.write("f:\\users.dat",users);
				FileTools.write("f:\\inodes.dat", inodes);
				FileTools.write("f:\\data.dat", blocks);
				System.out.println("退出登陆！");
				login();
			}
			// help操作
			else if (cmd[0].trim().equals("help")) {
				help();

			}
			//修改
			//format操作
			else if(cmd[0].trim().equals("format")){
				
				format();
				System.out.println("格式化完毕!");
			}
			else {
				System.out.println(commond);
				System.out.println("错误命令，请输入help命令进行参考");
			}

		}

	}

	/**
	 * regeist(String name) 注册用户
	 * 
	 * @param name
	 */
	public boolean regeist(Users user) {
		 
		int inode_free_index = 0;
		inode_free_index=this.getFreeInode();
		if (inode_free_index > -1) {
			now_inode = inodes.get(inode_free_index);
			now_inode.setAddress(inode_free_index);// 文件快的地址
			now_inode.setModifytime();
			now_inode.setRight(1);
			now_inode.setState("close");
			now_inode.setType(0);
			now_inode.setUsers(user.name);
			now_inode.setPath(user.getName() + "->");
			now_inode.setMe(inode_free_index);// 当前Inode的索引
			inodes.set(inode_free_index, now_inode);
			MyDirectory block = new MyDirectory();
			block.setName(user.getName());
			blocks.set(inode_free_index, block);
			users.add(user);
			FileTools.write("f:\\users.dat", users);
			FileTools.write("f:\\inodes.dat", inodes);
			return true;
		}

		return false;
	}

	public void help() {
		System.out.println("\thelp\t\t显示命令帮助");
		System.out.println("\tdir\t\t显示当前目录下的文件和文件夹");
		System.out.println("\tcd [目录名]\t进入一个目录");
		System.out.println("\tcdir [目录名]\t创建一个目录");
		System.out.println("\tcreate [文件名]\t创建一个文本文件");
		System.out.println("\tedit [文件名]\t编辑一个已经存在的文本文件");
		System.out.println("\tdelete [文件名]\t删除文件");
		System.out.println("\tattr [文件名]\t显示该文件的属性");
		System.out.println("\texit\t\t退出系统");
		System.out.println("\tlogout\t\t退出登陆");
		System.out.println("\tformat\t\t格式化");
		//System.out.println();
	}

	private Object getFileByName(String name) {
		for (Object o : blocks) {
			if (o instanceof MyDirectory) {
				MyDirectory o1 = (MyDirectory) o;
				if (o1.getName().equals(name)) {
					return o1;
				}
			} else if (o instanceof MyFile) {
				MyFile o1 = (MyFile) o;
				if (o1.getName().equals(name)) {
					return o1;
				}
			}
		}
		return null;

	}

	/**
	 * isInNames(String name) 判断用户名是否存在
	 * 
	 * @param name
	 * @return
	 */
	private Users isInNames(String name) {
		for (Users u : users) {
			if (u.getName().equals(name))
				return u;
		}
		return null;
	}

	/**
	 * getFreeInode() 得到空的inode
	 * 
	 * @return
	 */
	private int getFreeInode() {

		return sb.getInode_free();
	}

	/**
	 * getInode(String path) 由path得到Inode
	 * 
	 * @param name
	 * @return
	 */
	private INode getInode(String path) {
		for (int i = 0; i < 100; i++) {
			if (path.equals(inodes.get(i).getPath())) {
				return inodes.get(i);
			}
		}
		return null;
	}

	/**
	 * getBlock() 得到空闲的block的序号
	 * 
	 * @param name
	 * @return
	 */
	private int getBlock() {
		for (int i = 0; i < 100; i++) {
			if (null == blocks.get(i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * rename(String[] cmd) 重命名函数
	 * 
	 * @param cmd
	 * @return
	 */
	private boolean rename(String[] cmd) {
		if (cmd.length < 3) {
			System.out.println("命令输入错误！");
			return false;
		}
		Object o = getFileByName(cmd[1]);
		if (null == o)
			return false;
		else {
			if (o instanceof MyDirectory) {
				MyDirectory oo = (MyDirectory) o;
				oo.setName(cmd[2]);
				// inode.setPath(now_inode.getPath() + cmd[1] + "->");
				inodes.get(oo.getInode_address()).setPath(now_inode.getPath()
						+ cmd[2] + "->");
				return true;
			} else {
				MyFile oo = (MyFile) o;
				oo.setName(cmd[2]);
				return true;
			}
		}

	}
	
	public  void format(){
		blocks=(ArrayList<Object>)FileTools.read("f:\\data.dat");
		
		sb = (SuperBlock) FileTools.read("f:\\super.dat");
		
		inodes = (ArrayList<INode>) FileTools.read("f:\\inodes.dat");	
		
		users = (ArrayList<Users>) FileTools.read("f:\\users.dat");
		
		
		//sb = new SuperBlock();
		//blocks=new ArrayList<Object>(100);
/*		for(int i=0;i<blocks.size();i++){
			blocks.remove(Object);
		}*/
		blocks.clear();
		for(int i=0;i<100;i++){
			blocks.add(new MyFile());
		}
		FileTools.write("f:\\data.dat",blocks);
		
/*		for (int i= 0; i < 100; i++) {
				inodes.add(new INode());
			}	*/			
		inodes.clear();
		for (int i = 0; i < 100; i++) {
			sb.setInode_free(i);
		}
		//Users u=new Users("admin","admin");
		//regeist(u);
		
		
		for (int i= 0; i < 100; i++) {
			inodes.add(new INode());
		}				
	
		for (int i = 0; i < 100; i++) {
			sb.setInode_free(i);
		}
		FileTools.write("f:\\super.dat", sb);
		
		users.clear();// 存放整个文件系统
		users = new ArrayList<Users>();// 存放整个文件系统
		Users u=new Users("admin","admin");
		users.add(u);
		regeist(u);
		FileTools.write("f:\\users.dat", users);
		
		FileTools.write("f:\\inodes.dat", inodes);
	
		
	}
}