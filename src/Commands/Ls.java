package Commands;

import OS.FileSystem;

public class Ls extends CommandsUsingPath{
	
	public Ls(String command, FileSystem fileSystem) {
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
	
	@Override
	public boolean verifyErrors() {
		if (super.verifyErrors()) {
			return true;
		}
		
		if (entityLocation.getNextFile(entityName) == null) {
			System.out.println("-12: " + fullCommand + ": No such file or directory");
			return true;
		}
		
		if (entityLocation.getNextFile(entityName).executingPermission(fileSystem.getCurrentUser()) == 0) {
			System.out.println("-6: " + fullCommand + ": No rights to execute");
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
		
		entityLocation.getNextFile(entityName).ls();
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#pathShouldBeDirectory()
	 * Path could be both file and directory for ls
	 */
	@Override
	public boolean pathShouldBeDirectory() {
		return false;
	}

}
