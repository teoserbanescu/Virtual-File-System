package Commands;
import java.util.List;

import OS.FileSystem;
import OS.FileSystem.User;
import OS.Entity;


public class DelUser implements Command{
	private User userToDel;
	private FileSystem fileSystem; 
	private String fullCommand;
	
	public DelUser(String command, FileSystem fileSystem) {
		this.fileSystem = fileSystem;
		this.fullCommand = command;
		setArguments(command);
		
	}
	
	public void setArguments(String command) {
		String userName = command.split(" ", 2)[1];
		this.userToDel = fileSystem.getUser(userName);
	}
	
	public boolean verifyErrors() {
		if (!fileSystem.getCurrentUser().equals("root")) {
			System.out.println("-10: " + fullCommand + ": No rights to change user status");
			return true;
		}
		
		if (userToDel == null) {
			System.out.println("-8: " + fullCommand + ": User does not exist");
			return true;
		}
		return false;
	}
	
	public boolean execute() {
		if (verifyErrors() == true) {
			return false;
		}
		fileSystem.delUser(userToDel);
		User newOwner = fileSystem.getDefaultUser();
		resetOwner(fileSystem.getRootDirectory(), userToDel, newOwner);
		return true;
	}
	
	public void resetOwner(Entity file, User oldOwner, User newOwner) {
		if (file.getOwner().equals(oldOwner.getName())) {
			file.setOwner(newOwner.getName());
		}
		List<Entity> subordinates = file.getSubordinates();

		for (Entity subordinate: subordinates) {
			resetOwner(subordinate, oldOwner, newOwner);
		}
	}

}
