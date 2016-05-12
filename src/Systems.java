import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Systems {
	Scanner sc = new Scanner(System.in);// �ӿ���̨��ȡ����

	public static SuperBlock sb = null;// ������ ��¼������̵�����Ϣ
	public static ArrayList<Users> users;// �û�������;
	public static ArrayList<INode> inodes = new ArrayList<INode>(100);// inode��¼���ݽṹ
	public static INode[] inodes2;
	public static ArrayList<Object> blocks = new ArrayList<>(100);// �ļ���Ľṹ��
	public static String name = null;// ��ǰ��¼�û���
	public static String password= null;// ��ǰ��¼����
	public static int cnt;
	public static INode now_inode = null;// ��ǰ�ڵ�
	public static Object now_file = null;

	// public static INode father;//���ڵ�
	// public static INode me;//�Լ��ĵ�ǰ�ڵ�

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Systems sts = new Systems();
		//int cnt;
		sts.init();// ��ʼ�����ݣ�
		sts.login();
		//System.out.println("yun");
	}

	public void init() {
	
		System.out.println("***************��ӭʹ�ø��ļ�����ϵͳ*************");
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
			users = new ArrayList<Users>();// ��������ļ�ϵͳ
			Users u=new Users("admin","admin");
//			u.setName("admin");
//			u.setPassword("admin");
			users.add(u);
			regeist(u);
			FileTools.write("f:\\users.dat", users);
		}
		
	}

	public void login() {

		
		System.out.println("���ȵ�¼->");
		System.out.print("user name:");
		name = sc.next();
		System.out.print("password:");
		password= sc.next();
		Users user=isInNames(name);
		if (user==null) {
			System.out.println("���û��������ڣ��Ƿ�ע����û���y/n");
			if ("y".equals(sc.next())) {
				Users u=new Users(name,password);
				
				if (regeist(u)) {
					System.out.println("regeist success��");
					login();
				} else {
					System.out.println("ע��ʧ�ܣ�");
					System.exit(0);
				}

			} else {
				login();
			}

		} else {
			
			
			if(name.equals(user.getName())&&password.equals(user.getPassword())){
				System.out.println("login success!!");
				now_inode = getInode(name+ "->");// �õ���ǰ��inode
				now_file = blocks.get(now_inode.getAddress());// �õ���ǰ��Ŀ¼
				help();
				execute();
			}else {
				
				System.out.println("login fail!!");
				login();
			}
	
			
		}
	}

	/**
	 * ����ִ�е�������
	 */
	public void execute() {
		
		String commond = null;
		String cmd[] = null;// ������������ cmd[0] ���������� cmd[1]�������ļ�
		/*
		 * INode id = new INode();// �ļ��Ľڵ� int fileNumber = 0;// ӵ���ļ������� int
		 * getNumber = 0;// �洢���ļ��Ľڵ������ţ�����˵��������ڴ��ַ int emptyNumber = 0;//
		 * �յ��ļ�Ŀ¼������
		 */// System.out.println(now_inode.getPath());

		while (true) {
			System.out.print(now_inode.getPath());
			commond = sc.nextLine();
			if (commond.equals(""))
				commond = sc.nextLine();
			cmd = commond.trim().split(" ");
			// �о�ͬһ�����û������ļ�Ŀ¼
			if (cmd[0].trim().equals("dir")) {
				int m = 0;
				
				if (now_file instanceof MyDirectory) {
					MyDirectory now__real_file = (MyDirectory) now_file;
					m = now__real_file.getTree().size();
					if (m == 0) {
						System.out.println("û��Ŀ¼��");
					} else {
						System.out.println("�ļ���\t\t��ַ \tֻ��0/��д1\t�ļ�����\t ");//�޸�
						Set<Integer> dir_inodes = now__real_file.getTree()
								.keySet();
						Iterator<Integer> iteratore = dir_inodes.iterator();
						while (iteratore.hasNext()) {

							Object file = blocks.get(now__real_file.getTree().get(
									iteratore.next()));
							if (file instanceof MyDirectory) {
								MyDirectory real_file = (MyDirectory) file;
								INode real_inode = inodes.get(real_file.getInode_address());
								// "�ļ���\t�û���\t��ַ\t�ļ�����\tֻ��1/��д2\t�򿪿���\t����ʱ��"
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
						System.out.println("�ļ�����---" + m);
					}

				} else {
					MyFile now__real_file = (MyFile) now_file;
				}

			}
			// �����ļ�
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
					inode.setRight(1);// ��д
					inode.setState("open");
					inode.setType(1);// �ļ�
					inode.setAddress(index);
					inodes.set(index, inode);
					my_file.setInode_address(index);
					MyDirectory real_file = (MyDirectory) now_file;
					blocks.set(index,my_file);
					real_file.getTree().put(index, index);
					System.out.println(cmd[1] + "�ļ��Ѿ��򿪣�����������,��###��������");
					StringBuffer content = new StringBuffer();
					while (true) {//�޸�
						String tem = sc.nextLine();
						if (tem.equals("###")) {
							System.out.println("�ļ��������");
							break;// �ļ��������
						}else{
							content.append(tem + "\r\n");
						}
						
					}
					my_file.setSubstance(content.toString());
					inodes.get(index).setLength(content.length());
					inodes.get(index).setState("close");
					System.out.println(cmd[1] + "�ļ��ѹرգ�");
					sb.setAlreadyuse(content.length());
					sb.setInode_busy(index);
				} else {
					System.out.println("inode����ʧ�ܣ�");
				}

			}
			// �����ļ�Ŀ¼
			else if (cmd[0].trim().equals("cdir")) {
				if(cmd.length==1){
					System.out.println("û������Ŀ¼����");//�޸�
					continue;
				}
				int index = getFreeInode();//ȡ��ǰ���еĵ�һ��inode�±�
				if (index != -1) {
					MyDirectory my_file = new MyDirectory();
					my_file.setName(cmd[1]);
					INode inode = new INode();
					inode.setFather(now_inode.getMe());
					inode.setUsers(name);
					inode.setMe(index);
					inode.setModifytime();
					inode.setPath(now_inode.getPath() + cmd[1] + "->");
					System.out.println("�½��ĵ�ַ"+now_inode.getPath() + cmd[1] + "->");
					inode.setRight(1);// ��д
					inode.setType(0);//0��ʾĿ¼
					inode.setAddress(index);
					inodes.set(index, inode);
					my_file.setInode_address(index);
					MyDirectory real_file = (MyDirectory) now_file;
					blocks.set(index,my_file);
					real_file.getTree().put(index, index);
					inodes.get(index).setLength(0);
					sb.setInode_busy(index);

				} else {
					System.out.println("inode����ʧ�ܣ�");
				}

			}
			// ɾ���ļ��Ĳ���
			else if (cmd[0].trim().equals("delete")) {

				Object o = this.getFileByName(cmd[1]);
				if (null != o) {
					if (o instanceof MyDirectory) {
						MyDirectory o1 = (MyDirectory) o;

						if (o1.getTree().size() == 0) {
							int index = o1.getInode_address();
							sb.setInode_free(index);
							// ���ýڵ�
							inodes.set(index, new INode());
							// �������ݿ�
							blocks.set(o1.getInode_address(), new MyFile());
							// ��Ŀ¼��tree��ɾ������
							MyDirectory file = (MyDirectory) now_file;
							file.getTree().remove(index);

							System.out.println(o1.getName() + "Ŀ¼��ɾ����");
						} else {
							System.out.println(o1.getName() + "Ŀ¼��Ϊ�գ�������ɾ��");
						}
					} else if (o instanceof MyFile) {
						MyFile o1 = (MyFile) o;

						int index = o1.getInode_address();
						// ���ó�����
						sb.setInode_free(index);
						sb.setFreeuse(inodes.get(index).getLength());
						// ���ýڵ�
						inodes.set(index, new INode());
						// �������ݿ�
						blocks.set(o1.getInode_address(), new MyFile());
						// ��Ŀ¼��tree��ɾ������
						MyDirectory file = (MyDirectory) now_file;
						file.getTree().remove(index);

						System.out.println(o1.getName() + "�ļ���ɾ����");

					} else {
						System.out.println(cmd[1] + "�ļ������ڣ�");
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
							System.out.println("�ļ���\t������\t��ַ\t�ļ�����\t   ֻ��0/��д1\t�򿪿���\t����ʱ��");
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
								System.out.println("�ļ���\t������\t��ַ\t�ļ�����\t   ֻ��0/��д1\t�򿪿���\t����ʱ��");
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
				if(flag==0)System.out.println("�����ڴ��ļ����ļ���");
				
			}
			else if (cmd[0].trim().equals("cd")) {
				if(cmd.length==1){
					System.out.println("cd�������.����..������Ŀ¼��");
					continue;
				}
				if (".".equals(cmd[1])) {

				} else if ("..".equals(cmd[1])) {
					if (now_inode.getFather() == -1) {
						System.out.println("��ǰĿ¼Ϊ��Ŀ¼��");
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
					
					if(!flag)System.out.println("�����ڴ�Ŀ¼!!");
					
/*					Object o1 = getFileByName(cmd[1]);
					if (o1 instanceof MyDirectory) {
						MyDirectory o = (MyDirectory) o1;
						now_file = o;
						now_inode = inodes.get(o.getInode_address());
					} else {
						System.out.println("�����Ŀ¼�����ڣ����飡");
					}
*/
				}

			} else if (cmd[0].trim().equals("open")) {
				boolean flag=false;
				if(cnt>5){
					System.out.println("���Ѿ�����5���ļ���");
					continue;
				}
				for(int i=0;i<blocks.size();i++){
					Object o=blocks.get(i);
					if(o instanceof MyFile){
						if(((MyFile)o).getName().equals(cmd[1])){
							INode inode=inodes.get(((MyFile)o).getInode_address());
							if(inode.getState().equals("open")){
								System.out.println("���ļ��ѱ��򿪣�");
								flag=true;
							}
							else{
								
								inode.setState("open");
								System.out.println("�ļ��򿪳ɹ���");
								cnt++;
								flag=true;
							}
						}
					}
					
				}
				if(!flag)System.out.println("�ļ������ڣ�");
			
			}

			else if (cmd[0].trim().equals("close")) {
				boolean flag=false;
			
				for(int i=0;i<blocks.size();i++){
					Object o=blocks.get(i);
					if(o instanceof MyFile){
						if(((MyFile)o).getName().equals(cmd[1])){
							INode inode=inodes.get(((MyFile)o).getInode_address());
							if(inode.getState().equals("close")){
								System.out.println("���ļ��ѱ��رգ�");
								flag=true;
							}
							else{
								
								inode.setState("close");
								System.out.println("�ļ��رճɹ���");
								cnt--;
								flag=true;
							}
						}
					}
					
				}
				if(!flag)System.out.println("�ļ������ڣ�");
				
			} else if (cmd[0].trim().equals("rename")) {

				// System.out.println("�ļ�" + file[0] + "�Ѿ��ر�");
				if (rename(cmd)) {
					System.out.println("�������ɹ���");
				} else {
					System.out.println("������ʧ�ܣ�");
				}

			}
			// read�������ļ��Ѿ��򿪵Ļ�����ִ���ļ��Ķ�����������ļ�û�д򿪣������ִ���ļ��Ķ��������򲻿��ԣ�
			else if (cmd[0].trim().equals("read")) {

				Object o = this.getFileByName(cmd[1]);
				if (null != o) {
					if (o instanceof MyDirectory) {
						MyDirectory o1 = (MyDirectory) o;
						System.out.println(o1.getName() + "Ŀ¼����ִ�д����");
					} else if (o instanceof MyFile) {

						MyFile o1 = (MyFile) o;
						System.out.println(o1.getName() + "�ļ��������£�");
						System.out.println(o1.getSubstance().substring(0,
								o1.getSubstance().lastIndexOf("\r\n")));
					}
				}
			} else if (cmd[0].trim().equals("write")) {

				Object o = this.getFileByName(cmd[1]);
				if (null != o) {
					if (o instanceof MyDirectory) {
						MyDirectory o1 = (MyDirectory) o;
						System.out.println(o1.getName() + "Ŀ¼����ִ�д����");
					} else if (o instanceof MyFile) {
						MyFile o1 = (MyFile) o;
						// System.out.println(o1.getName());
						System.out.println("1.��д;2.��д; ��ѡ��");
						String select = sc.next();
						while (true) {

							if ("1".equals(select)) {
								System.out.println("��������д�����ݣ���###����");
								StringBuffer content = new StringBuffer(o1
										.getSubstance().substring(
												0,
												o1.getSubstance().lastIndexOf(
														"\r\n")));
								while (true) {
									String tem = sc.next();
									if (tem.equals("###")) {
										System.out.println("�ļ��������");
										break;// �ļ��������
									}else{
										content.append(tem + "\r\n");
									}
									
								}
								o1.setSubstance(content.toString());
								System.out.println("��д�����ɹ���");
								break;

							} else if ("2".equals(select)) {
								System.out.println("��������д�����ݣ���###����");
								StringBuffer content = new StringBuffer();
								while (true) {
									String tem = sc.next();
									if (tem.equals("###")) {
										System.out.println("�ļ��������");
										break;// �ļ��������
									}else{
										content.append(tem + "\r\n");
									}
									
								}
								o1.setSubstance(content.toString());
								System.out.println("��д�����ɹ���");
								break;

							} else {
								System.out.println("����������������룡");
								select = sc.next();
							}
						}
					}
				} else {
					System.out.println("����������������룡");

				}
			}
			// �˳�����---��������
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
				System.out.println("�˳���½��");
				login();
			}
			// help����
			else if (cmd[0].trim().equals("help")) {
				help();

			}
			//�޸�
			//format����
			else if(cmd[0].trim().equals("format")){
				
				format();
				System.out.println("��ʽ�����!");
			}
			else {
				System.out.println(commond);
				System.out.println("�������������help������вο�");
			}

		}

	}

	/**
	 * regeist(String name) ע���û�
	 * 
	 * @param name
	 */
	public boolean regeist(Users user) {
		 
		int inode_free_index = 0;
		inode_free_index=this.getFreeInode();
		if (inode_free_index > -1) {
			now_inode = inodes.get(inode_free_index);
			now_inode.setAddress(inode_free_index);// �ļ���ĵ�ַ
			now_inode.setModifytime();
			now_inode.setRight(1);
			now_inode.setState("close");
			now_inode.setType(0);
			now_inode.setUsers(user.name);
			now_inode.setPath(user.getName() + "->");
			now_inode.setMe(inode_free_index);// ��ǰInode������
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
		System.out.println("\thelp\t\t��ʾ�������");
		System.out.println("\tdir\t\t��ʾ��ǰĿ¼�µ��ļ����ļ���");
		System.out.println("\tcd [Ŀ¼��]\t����һ��Ŀ¼");
		System.out.println("\tcdir [Ŀ¼��]\t����һ��Ŀ¼");
		System.out.println("\tcreate [�ļ���]\t����һ���ı��ļ�");
		System.out.println("\tedit [�ļ���]\t�༭һ���Ѿ����ڵ��ı��ļ�");
		System.out.println("\tdelete [�ļ���]\tɾ���ļ�");
		System.out.println("\tattr [�ļ���]\t��ʾ���ļ�������");
		System.out.println("\texit\t\t�˳�ϵͳ");
		System.out.println("\tlogout\t\t�˳���½");
		System.out.println("\tformat\t\t��ʽ��");
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
	 * isInNames(String name) �ж��û����Ƿ����
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
	 * getFreeInode() �õ��յ�inode
	 * 
	 * @return
	 */
	private int getFreeInode() {

		return sb.getInode_free();
	}

	/**
	 * getInode(String path) ��path�õ�Inode
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
	 * getBlock() �õ����е�block�����
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
	 * rename(String[] cmd) ����������
	 * 
	 * @param cmd
	 * @return
	 */
	private boolean rename(String[] cmd) {
		if (cmd.length < 3) {
			System.out.println("�����������");
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
		
		users.clear();// ��������ļ�ϵͳ
		users = new ArrayList<Users>();// ��������ļ�ϵͳ
		Users u=new Users("admin","admin");
		users.add(u);
		regeist(u);
		FileTools.write("f:\\users.dat", users);
		
		FileTools.write("f:\\inodes.dat", inodes);
	
		
	}
}