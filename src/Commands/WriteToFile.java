package Commands;

import OS.FileSystem;

public class WriteToFile extends CommandsUsingPath {
	
	private String content;

	public WriteToFile(String command, FileSystem fileSystem) {
		super(command, fileSystem);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.CommandsUsingPath#setArguments(java.lang.String)
	 * pathToFile = argument2;
	 * contentToWrite = argument3;
	 */
	@Override
	public void setArguments(String command) {
		String[] arguments = command.split(" ", 3);
		this.setPath(arguments[1]);
		this.content = arguments[2];
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#verifyErrors()
	 * -1: <command>: Is a directory
	 * -11: <command>: No such file
	 * -5: <command>: No rights to write
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
		
		if (entityLocation.getNextFile(entityName).writingPermission(fileSystem.getCurrentUser()) == 0) {
			System.out.println("-5: " + fullCommand + ": No rights to write");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean execute() {
		if (this.verifyErrors() == true) {
			return false;
		}
		
		entityLocation.getNextFile(entityName).setContent(this.content);
		return true;
	}

}