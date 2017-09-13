package Commands;
import OS.FileSystem;
/*
 * 
 */
public class Rm extends CommandsUsingPath {
	public boolean parentDirectory = false;
	public boolean recursively;

	public Rm(String command, FileSystem fileSystem) {
		super(command, fileSystem);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.CommandsUsingPath#setArguments(java.lang.String)
	 * pathToDir = argument2;
	 */
	@Override
	public void setArguments(String command) {
		String[] arguments = command.split(" ");
		if (arguments[1].equals("-r")) {
			this.setPath(arguments[2]);
			this.recursively = true;
		} else {
			this.setPath(arguments[1]);
			this.recursively = false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#verifyErrors()
	 * -1: <command>: Is a directory
	 * -11: <command>: No such file
	 * -5: <command>: No rights to write
	 * rm -r <path>
	 * -12: <command>: No such file or directory
	 * -13: <command>: Cannot delete parent or current directory
	 * -5: <command>: 
	 * do i have to cd to all the files recursively? aka do i need execute permissions on all of them?
	 * NO :)
		
	 */
	
	@Override
	public boolean verifyErrors() {
		if (super.verifyErrors()) {
			return true;
		}
		if (this.recursively == true) {
			/*
			 * hardcode not proud of this
			 */
			/*if (this.entityPath == null && this.entityName == null) {
				System.out.println("-13: " + fullCommand + ": Cannot delete parent or current directory");
				return true;
			}*/
			if (fileSystem.getCurrentDirectory().isAncestor(entityLocation.getNextFile(entityName))) {
				System.out.println("-13: " + fullCommand + ": Cannot delete parent or current directory");
				return true;
			}
			
			if (entityLocation.getNextFile(entityName) == null) {
				System.out.println("-12: " + fullCommand + ": No such file or directory");
				return true;
			}
			
			if (fileSystem.getCurrentDirectory().isAncestor(entityLocation.getNextFile(entityName))) {
				System.out.println("-13: " + fullCommand + ": Cannot delete parent or current directory");
				return true;
			}
			
			/*
			 * verify writing permissions on directory which contains the file
			 */
			
			if (entityLocation.writingPermission(fileSystem.getCurrentUser()) == 0) {
				System.out.println("-5: " + fullCommand + ": No rights to write");
				return true;
			}
		} else {
			if (entityLocation.getNextFile(entityName) == null) {
				System.out.println("-11: " + fullCommand + ": No such file");
				return true;
			}
			
			if (entityLocation.getNextFile(entityName).getType().equals("d")) {
				System.out.println("-1: " + fullCommand + ": Is a directory");
				return true;
			}
			
			/*
			 * verify writing permissions on directory which contains the file
			 */
			if (entityLocation.writingPermission(fileSystem.getCurrentUser()) == 0) {
				System.out.println("-5: " + fullCommand + ": No rights to write");
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean execute() {
		if (this.verifyErrors() == true) {
			return false;
		}
		entityLocation.removeSubordinate(entityName);
		return true;
	}

}