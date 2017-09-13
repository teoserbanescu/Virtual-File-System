package Commands;
import OS.FileSystem;
/*
 * factory of commands
 */

public class Factory{
	
	private FileSystem fileSystem;
	
	public Factory(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	public Command getCommand(String fullCommand) {
		String command = fullCommand.split(" ", 2)[0];
		
		switch (command) {
		case "adduser":
			return new AddUser(fullCommand, fileSystem);
		case "deluser":
			return new DelUser(fullCommand, fileSystem);
		case "chuser":
			return new ChUser(fullCommand, fileSystem);
		case "cd":
			return new Cd(fullCommand, fileSystem);
		case "mkdir":
			return new MkDir(fullCommand, fileSystem);
		case "ls":
			return new Ls(fullCommand, fileSystem);
		case "chmod":
			return new ChMod(fullCommand, fileSystem);
		case "touch":
			return new Touch(fullCommand, fileSystem);
		case "rm":
			return new Rm(fullCommand, fileSystem);
		case "rmdir":
			return new RmDir(fullCommand, fileSystem);
		case "writetofile":
			return new WriteToFile(fullCommand, fileSystem);
		case "cat":
			return new Cat(fullCommand, fileSystem);
		default:
			return null;
		}
	}
	
}
