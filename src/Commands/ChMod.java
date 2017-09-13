package Commands;

import OS.FileSystem;

public class ChMod extends CommandsUsingPath {
	public ChMod(String command, FileSystem fileSystem) {
		super(command, fileSystem);
	}

	private int newPermissions;
	/*
	 * (non-Javadoc)
	 * @see Commands.CommandsUsingPath#setArguments(java.lang.String)
	 * splits arguments
	 * newPermissions = argument2
	 * call CommandsUsingPath.setPath(argument3)
	 */
	@Override
	public void setArguments(String command) {
		String[] arguments = command.split(" ");
		newPermissions = Integer.parseInt(arguments[1]);
		this.setPath(arguments[2]);
		//System.out.println("argument3 should be" + arguments[2]);
	}
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#verifyErrors()
	 * -12: <command>: No such file or directory
	 * -5: <command>: No rights to write
	 */
	@Override
	public boolean verifyErrors() {
		if (super.verifyErrors()) {
			return true;
		}
		
		if (entityLocation.getNextFile(entityName) == null) {
			System.out.println("-12: " + fullCommand + ": No such file or directory");
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
		
		entityLocation.getNextFile(entityName).setPermissions(newPermissions);
		return true;
	}
	
	
	
}
