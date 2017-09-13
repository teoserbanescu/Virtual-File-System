package Commands;

import OS.FileSystem;

public class RmDir extends CommandsUsingPath {
	public boolean parentDirectory = false;

	public RmDir(String command, FileSystem fileSystem) {
		super(command, fileSystem);
		
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.CommandsUsingPath#setArguments(java.lang.String)
	 * pathToDir = argument2;
	 */
	@Override
	public void setArguments(String command) {
		// TODO Auto-generated method stub
		String[] arguments = command.split(" ");
		//newPermissions = Integer.parseInt(arguments[1]);
		this.setPath(arguments[1]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#verifyErrors()
	 * -3: <command>: Not a directory
		-13: <command>: Cannot delete parent or current directory
		-14: <command>: Non empty directory
		-5: <command>: No rights to write
		-2: <command>: No such directory
	 */
	
	@Override
	public boolean verifyErrors() {
		if (super.verifyErrors()) {
			return true;
		}
		
		if (entityLocation.getNextFile(entityName) == null) {
			System.out.println("-2: " + fullCommand + ": No such directory");
			return true;
		}
		
		if (entityLocation.getNextFile(entityName).getType().equals("f")) {
			System.out.println("-3: " + fullCommand + ": Not a directory");
			return true;
		}
		
		if (fileSystem.getCurrentDirectory().isAncestor(entityLocation.getNextFile(entityName))) {
			System.out.println("-13: " + fullCommand + ": Cannot delete parent or current directory");
			return true;
		}
		
		if (!entityLocation.getNextFile(entityName).getSubordinates().isEmpty()) {
			System.out.println("-14: " + fullCommand + ": Non empty directory");
			return true;
		}
		/*System.out.println("rmdir verify errors " + entityName);
		System.out.println(entityLocation.getNextFile(entityName).getPermissionsString());
		System.out.println(fileSystem.getCurrentUser());*/
		
		if (entityLocation.getNextFile(entityName).writingPermission(fileSystem.getCurrentUser()) == 0 ||
				entityLocation.writingPermission(fileSystem.getCurrentUser()) == 0) {
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
		//System.out.println("chmod execute");
		
		entityLocation.getSubordinates().remove(entityLocation.getNextFile(entityName));
		return true;
	}
	
	public void isAncestor(String directory) {
		
	}

}