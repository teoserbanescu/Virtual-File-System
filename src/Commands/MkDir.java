package Commands;

import OS.Directory;
import OS.Entity;
import OS.FileSystem;

public class MkDir extends AddEntity {
	public MkDir(String command, FileSystem fileSystem) {
		super(command, fileSystem);
	}
	
	/*public boolean verifyErrors() {
		return super.verifyErrors();
	}*/
	
	
	public boolean execute() {
		if (this.verifyErrors() == true) {
			return false;
		}
		//System.out.println(fileSystem);
		Entity newDir = new Directory(fileSystem.getCurrentUser(), addedEntityName);
		
		
		//dirLocation = fileSystem.getCurrentDirectory();
		
		
		entityLocation.addSubordinate(newDir);
		return true;
		/*fileSystem.getRootDirectory().addSubordinate(userToAdd.getHomeDirectory());
		fileSystem.addUser(userToAdd);*/
	}
	
	public void pathIsAlreadyAFile() {
		System.out.println("-3: " + fullCommand + ": Not a directory");
	}
}
