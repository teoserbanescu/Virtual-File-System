package OS;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
	protected String owner;
	protected String name;
	private int permissions;
	protected Entity parent;
	protected String content;
	
	protected List<Entity> subordinates;
	/*
	 * To create empty entity
	 */
	public Entity() {
		
	}
	/*
	 * Files and directories have a name passed as a parameter in the constructor
	 * That name is NOT the absolute path
	 * The absolute path is built recursively every time we need it
	 * !When we construct a new file/dir, we must add it in its parent list of subordinates afterwards
	 * !When we delete a file/dir, we must first delete it from its parent list of subordinates
	 * !and then effectively delete it (well.. you have garbage collector do the job for you)
	 */
	
	Entity(String owner, String name) {
		this.owner = owner;
		this.name = name;
		switch (name) {
		case "/":
			this.setPermissions(75);
			break;
		default:
			this.setPermissions(70);
		}
		this.setParent(this);
		subordinates = new ArrayList<Entity>();
	}
	
	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}
	
	public int getPermissions() {
		return this.permissions;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public int permission(String user, int perm) {
		//System.out.println(name + " owner " + this.getOwner());
		if (user.equals("root")){
			return 1;
		}
		if (user.equals(owner)) {
			//System.out.println("isOwner");
			return (permissions / 10) & perm;
		}
		else {
			return (permissions % 10) & perm;
		}
	}
	public int readingPermission(String user) {
		return permission(user, 4);
	}
	
	public int writingPermission(String user) {
		return permission(user, 2);
	}
	
	public int executingPermission(String user) {
		return permission(user, 1);
	}
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}
	
	public Entity getParent() {
		return parent;
	}
	
	public String getPermissionsString() {
		String stringPermissions = new String();
		for (int permission = 5; permission >= 0; --permission) {
			int owner = permission / 3;
			int power = permission % 3;
			int currentPermission = 0;
			switch (owner) {
			case 1:
				currentPermission = (permissions / 10) & (1 << power);
				break;
			case 0:
				currentPermission = (permissions % 10) & (1 << power);
				break;
			}
			if(currentPermission > 0) {
				switch (power) {
				case 2:
					stringPermissions += "r";
					break;
				case 1:
					stringPermissions += "w";
					break;
				case 0:
					stringPermissions += "x";
					break;
				}
			} else stringPermissions += "-";
		}
		return stringPermissions;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * if two files names are equal they are not necessarily equal but in this case
	 * equal is only used for operations like finding an element in the list of subordinates
	 * => the two absolute paths we compare are always equal
	 * not pretty but it works :)
	 */
	@Override
	public boolean equals(Object otherFile) {
		if (this.getName().equals(((Entity)otherFile).getName())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * method 2 in order to use indexOf in the list and not create a new instance of File
	 */
	public boolean equals(String otherFileName) {
		if (this.getName().equals(otherFileName)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * the following are for both file and directory
	 */
	public abstract String getType();
	
	public void print(int level) {
		System.out.print(new String(new char[level]).replace("\0", "\t"));
		System.out.println(this.getName() + " " + this.getType() + this.getPermissionsString() + " " + this.getOwner());
	}
	
	public void lsFile() {
		System.out.println(this.getName() + " " + this.getType() + this.getPermissionsString() + " " + this.getOwner());
	}
	
	public abstract void ls();
	
	public void removeSubordinate(String entityToRemove) {
		int index = 0;
		for (Entity entity : subordinates) {
			if (entity.getName().equals(entityToRemove)) {
				subordinates.remove(index);
				break;
			}
			++index;
		}
	}
	
	public Entity getEntityFromPath(String path, Directory startDirectory) {

		String namesInPath[] = path.split("/");
		Entity nextFile = startDirectory;
		int nextFileIndex;
		for (String name: namesInPath) {
			if (name.equals(".")) {
				continue;
			}
			if (name.equals("..")) {
				nextFile = nextFile.getParent();
				nextFileIndex = nextFile.getSubordinates().indexOf(nextFile.getParent());
			}
			else {
				nextFileIndex = nextFile.getSubordinates().indexOf(name);
			}
			if (nextFileIndex != -1) {
				nextFile = nextFile.getSubordinates().get(nextFileIndex);
			}
			else {
				return null;
			}
		}
		return nextFile;
	}
	/*
	 * file specific
	 */
	
	public abstract void catContent();

	public abstract void setContent(String content);
	
	/*
	 * the following are directory specific
	 */
	
	public abstract void addSubordinate(Entity newFile);

	public abstract List<Entity> getSubordinates();

	public abstract void setSubordinates(List<Entity> subordinates);
	
	public abstract Entity getNextFile(String targetName);
	
	public boolean isAncestor(Entity ancestor) {
		if (ancestor.equals(this)) {
			return true;
		}
		
		for (Entity subordinate : ancestor.getSubordinates()) {
			if (isAncestor(subordinate)) {
				return true;
			}
		}
		return false;
	}
	
}
