package Commands;
import OS.FileSystem;

public class ChUser implements Command {
	private FileSystem.User userToChange;
	FileSystem fileSystem; //TODO: hmmm all commands need the file system.. so abstract class again
	String fullCommand;
	
	public ChUser(String command, FileSystem fileSystem) {
		this.fileSystem = fileSystem;
		this.fullCommand = command;
		setArguments(command);
	}
	
	public void setArguments(String command) {
		String userName = command.split(" ", 2)[1];
		this.userToChange = fileSystem.getUser(userName);
	}
	
	public boolean verifyErrors() {
		if (userToChange == null) {
			System.out.println("-8: " + fullCommand + ": User does not exist");
			return true;
		}
		return false;
	}
	
	public boolean execute() {
		if (verifyErrors() == true) {
			return false;
		}
		fileSystem.setCurrentUser(userToChange.getName());
		fileSystem.setCurrentDirectory(userToChange.getHomeDirectory());
		return true;
	}
}
