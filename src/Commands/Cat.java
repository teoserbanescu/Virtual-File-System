package Commands;

import OS.FileSystem;

public class Cat extends CommandsUsingPath {

	public Cat(String command, FileSystem fileSystem) {
		super(command, fileSystem);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.CommandsUsingPath#setArguments(java.lang.String)
	 * pathToFile = argument2;
	 */
	@Override
	public void setArguments(String command) {
		String[] arguments = command.split(" ");
		this.setPath(arguments[1]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#verifyErrors()
	 * -1: <command>: Is a directory
	 * -11: <command>: No such file
	 * -4: <command>: No rights to read
	 */
	
	@Override
	public boolean verifyErrors() {
		if (super.verifyErrors()) {
			return true;
		}
		
		if (entityLocation.getNextFile(entityName) == null) {
			System.out.println("-11: " + fullCommand + ": No such file");
			return true;
		}
		
		if (entityLocation.getNextFile(entityName).getType().equals("d")) {
			System.out.println("-1: " + fullCommand + ": Is a directory");
			return true;
		}
		
		if (entityLocation.getNextFile(entityName).readingPermission(fileSystem.getCurrentUser()) == 0) {
			System.out.println("-4: " + fullCommand + ": No rights to read");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean execute() {
		if (this.verifyErrors() == true) {
			return false;
		}
		
		entityLocation.getNextFile(entityName).catContent();
		return true;
	}

}