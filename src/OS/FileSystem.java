package OS;

import java.util.ArrayList;
import java.util.List;
import Utils.*;


public class FileSystem {
	private String currentUser;
	private Entity currentDirectory;
	private Entity rootDirectory;
	private Entity rootOfRootDirectory;
	
	private List<User> users;
	
	public FileSystem() {
		this.currentUser = "root";
		this.rootOfRootDirectory = new Directory("root", "/");
		this.rootDirectory = new Directory("root", "/");
		this.rootOfRootDirectory.addSubordinate(rootDirectory);
		currentDirectory = rootDirectory;
		users = new ArrayList<User>();
		addUser(new User(currentUser));
	}
	
	public FileSystem(String user, Entity directory) {
		this.currentUser = user;
		this.currentDirectory = directory;
	}
	
	public String getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	
	public Entity getCurrentDirectory() {
		return currentDirectory;
	}
	
	public void setCurrentDirectory(Entity currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	
	public Entity getRootDirectory() {
		return rootDirectory;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public void delUser(User user) {
		users.remove(user);
	}
	
	public User getUser(String userName) {
		for (User user : users) {
			if (userName.equals(user.getName())) {
				return user;
			}
		}
		return null;
	}
	
	public User getDefaultUser() {
		if (users.size() > 1) {
			return users.get(1);
		} else {
			return users.get(0); // aka root. nobody else in the list
		}
	}
	
	/*
	 * i need this to check the path to the file. it returns File/Directory if the file/dir at the given path exists
	 * otherwise it returns null
	 * where it can be used:
	 * 		cd: -2: <command>: No such directory - returns null cause the path is not found
	 * 			-3: <command>: Not a directory - returns a file
	 * 		mkdir: -1: <command>: Is a directory - returns a file for the full path
	 * 			   -3: <command>: Not a directory - returns a directory for the full path
	 * 		       returns null if the path to the dir where the directory is not correct
	 * 							(but this is not necessary according to the problem)
	 * 		ls: -12: <command>: No such file or directory - returns null cause path is not found
	 * 		chmod: -12: <command>: No such file or directory - returns null cause path is not found
	 * 		touch: -1: <command>: Is a directory - returns Directory
	 *			   -7: <command>: File already exists - returns File so it already exists
	 */

	public class User {
		private String name;
		private Entity homeDirectory;
		public User(String name) {
			this.name = name;
			/*
			 * #hack
			 */
			if (name.equals("root")) {
				this.homeDirectory = getRootDirectory();
			} else {
				this.homeDirectory = new Directory(name, name);
			}
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Entity getHomeDirectory() {
			return homeDirectory;
		}
		public void setHomeDirectory(Entity homeDirectory) {
			this.homeDirectory = homeDirectory;
		}
		@Override
		public boolean equals(Object otherUser) {
			//System.out.println("user equals called");
			return name.equals(((User)otherUser).getName());
		}
		
	}
	
	/*
	 * we gonna do things recursively cause recursion is the best thing yaaa
	 */
	public Pair<Entity, ErrorFlag> getFileTFromPath(String namesInPath[], Entity currentFile) {		
		/*
		 * reached end of path //and everything is ok(path exists, but we do not know if it file or executable)
		 */
		if (namesInPath.length == 0) {
			/*
			 * no names left to traverse from the path
			 * found the file
			 */
			return new Pair<Entity, ErrorFlag>(currentFile, ErrorFlag.Succes);
		}
		
		/*
		 * -2: <command>: No such directory //entities from path do not exist
		 */
		
		if (currentFile == null) {
			return new Pair<Entity, ErrorFlag>(currentFile, ErrorFlag.NoSuchEntity);
		}
		
		//System.out.print(currentFile.getName() + " ");
		/*
		 * -3: <command>: Not a directory //somewhere in the path there is a file
		 */
		
		if (currentFile.getType().equals("f")) {
			return new Pair<Entity, ErrorFlag>(currentFile, ErrorFlag.NotDirectory);
		}
		
		/*
		 * -6: <command>: No rights to execute // some dir in the path
		 */
		
		if (currentFile.executingPermission(currentUser) == 0) {
			return new Pair<Entity, ErrorFlag>(currentFile, ErrorFlag.NoRightsToExecute);
		}
		
		String name = namesInPath[0];
		String leftNamesInPath[] = new String[namesInPath.length - 1];
		if (leftNamesInPath.length > 0) {
			System.arraycopy(namesInPath, 1, leftNamesInPath, 0, leftNamesInPath.length);
		}
		
		if (name.equals(".")) {
			return getFileTFromPath(leftNamesInPath, currentFile);
		}
		
		if (name.equals("..")) {
			return getFileTFromPath(leftNamesInPath, currentFile.getParent());
		}
		else {
			return getFileTFromPath(leftNamesInPath, currentFile.getNextFile(name));
		}
	}

	public Entity getRootOfRootDirectory() {
		return rootOfRootDirectory;
	}

	public void setRootOfRootDirectory(Entity rootOfRootDirectory) {
		this.rootOfRootDirectory = rootOfRootDirectory;
	}
}
 