package engine;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Alaimo Pizylon8 Copiright (C) 2015 <br>
 * <b>Email:</b> alaim.marco@gmail.com<br>
 *
 * created on 26/set/2015 13:10:59
 *
 */
public class MultiSearchMonitor extends Thread
{
	private File source;
	private File destination;
	private List<File> filesFound;
	private List<String> foldersFound;
	public 	List<Thread> thList;
	private List<String> blacklistByPath;
	private List<String>blacklistByName;
	private List<String>blacklistByExtension;
	
	public MultiSearchMonitor(File source, File destination, 
			List<String> blacklistByPath, List<String>blacklistByName,
			List<String>blacklistByExtension)
	{
		this.source=source;
		this.destination = destination;
		
		this.thList=new ArrayList<Thread>();
		this.blacklistByPath=blacklistByPath;
		this.blacklistByName=blacklistByName;
		this.blacklistByExtension=blacklistByExtension;	
		
	}

	
	public synchronized void addFileList(File file)
	{
		this.filesFound.add(file);
	}
	
	public synchronized void addFolderList(String folder)
	{
		this.foldersFound.add(folder);
	}
	
	

	
	
	public synchronized void run()
	{
		
		this.filesFound=new ArrayList<File>();
		this.foldersFound=new ArrayList<String>();
		
		File files[]=this.source.listFiles();
		
		if(files.length>0)
			{
				thList.add(this);
			}
		
		for(int i=0;i<files.length;i++)
			{
				
				if(files[i].isDirectory())
					{
						
						if(isAllowDirectory(files[i]))
							{
								addFolderList(files[i].getAbsolutePath());
								//createDir(files[i]);
								MultiThreadSearch temp =new MultiThreadSearch(files[i].getAbsolutePath(), this);
								temp.start();
							}
						//thList.add(temp);
						
						
					}else{
						
						if(isAllowExtension(files[i]))
							{
								this.filesFound.add(files[i]);
							}
					}
			}
		
		while(this.thList.size()>0)
			{
					try{
						
						//System.out.println("sono in wait "+thList.size());
						this.wait();
						thList.remove(this);
						//System.out.println("mi sveglio ");
						
					}catch(Exception e){
						e.printStackTrace();
					}
					
				
			}
		System.out.println("sono libero da thread");
		this.interrupt();
		
		
	}
	
	/**
	 * 
	 */@Deprecated
	synchronized public void createDir(File file) {
		String rel = source.toURI().relativize(file.toURI()).getPath();
		
		/**
		 * create the dir of destination if not exists
		 */
		File dir = new File(destination.getAbsoluteFile() + "\\" + rel);
		if(!dir.exists()){
			dir.mkdir();
		}
		
	}


	/**
	 * @param file
	 * @return
	 */
	public synchronized boolean isAllowExtension(File file)
	{
		boolean isAllow=true;
		if(this.blacklistByExtension!=null)
			{
				for(int i=0;i<blacklistByExtension.size();i++)
					{
						//System.out.println("name     "+file.getName().substring(file.getName().lastIndexOf(".")));
						
						if(file.getName().contains(".")&&file.getName().substring(file.getName().lastIndexOf(".")).equals(blacklistByExtension.get(i)))
							{
								//System.out.println("scarto estensione "+file.getName());
								return false;
							}
					}
			}
		
		
		
		return isAllow;
	}


	/**
	 * @param file
	 * @return
	 */
	public synchronized boolean isAllowDirectory(File file)
	{
		boolean is=true;
		String abPath=file.getAbsolutePath();
		if(this.blacklistByPath!=null)
			{
				for(int i=0;i<this.blacklistByPath.size();i++)
					{
						if(abPath.contains(this.blacklistByPath.get(i)))
							{
								//System.out.println("scarto la directory"+abPath);
								return false;
							}
					}
					
			}
			
			String directoryName=file.getName();
			if(this.blacklistByName!=null)
				{
					
					for(int i=0;i<this.blacklistByName.size();i++)
						{
							if(directoryName.equals(this.blacklistByName.get(i)))
								{
									//System.out.println("scarto la directory by name "+abPath);
									return false;
								}
						}
				}
					

		
		
		return is;
	}


	public List<File> getFileList()
	{
		
		return this.filesFound;
	}
	
	public List<String> getFolderList()
	{
		
		return this.foldersFound;
	}

	public static void main(String args[])
	{
		List<String> path=new ArrayList<String>();
		//path.add("\\.svn");
		MultiSearchMonitor mt=new MultiSearchMonitor(new File("C:\\Users\\Pizylon8\\Documents\\Documenti Tesi\\"), null, path, path,null);
		mt.run();
		
		List<File> flist=mt.getFileList();
		List<String> dlist=mt.getFolderList();
		System.out.println("Total files found: "+flist.size()+" \nTotal directories found: "+dlist.size());
		for(int i=0;i<flist.size();i++)
			{
				//if(flist.get(i).getAbsolutePath().contains(path.get(0)))
				//System.out.println("path "+flist.get(i));
				
			}
	}
}
