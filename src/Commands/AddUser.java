package Commands;
import OS.FileSystem;
//import OS.FileSystem.User;


public class AddUser implements Command{
	private FileSystem.User userToAdd;
	private FileSystem fileSystem; 
	private String fullCommand;
	
	public AddUser(String command, FileSystem fileSystem) {
		this.fileSystem = fileSystem;
		this.fullCommand = command;
		setArguments(command);
		
	}
	
	public void setArguments(String command) {
		String newUserName = command.split(" ", 2)[1];
		this.userToAdd = fileSystem.new User(newUserName);
	}
	
	public boolean verifyErrors() {
		if (!fileSystem.getCurrentUser().equals("root")) {
			System.out.println("-10: " + fullCommand + ": No rights to change user status");
			return true;
		}
		if (fileSystem.getUsers() != null)
		if (fileSystem.getUsers().contains(userToAdd)) {
			System.out.println("-9: " + fullCommand + ": User already exists");
			return true;
		}
		return false;
	}
	public boolean execute() {
		if (verifyErrors() == true) {
			return false;
		}
		fileSystem.getRootDirectory().addSubordinate(userToAdd.getHomeDirectory());
		fileSystem.addUser(userToAdd);
		return true;
	}
}
