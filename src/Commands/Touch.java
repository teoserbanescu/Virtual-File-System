package Commands;

import OS.Entity;
import OS.File;
import OS.FileSystem;

public class Touch extends AddEntity {
	public Touch(String command, FileSystem fileSystem) {
		super(command, fileSystem);
	}
	
	public boolean execute() {
		if (this.verifyErrors() == true) {
			return false;
		}
		Entity newFile = new File(fileSystem.getCurrentUser(), addedEntityName);
		entityLocation.addSubordinate(newFile);
		return true;
	}
	
	public void pathIsAlreadyAFile() {
		System.out.println("-7: " + fullCommand + ": File already exists");
	}
}