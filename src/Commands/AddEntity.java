package Commands;
import OS.FileSystem;

public abstract class AddEntity extends Cd {//implements Command{
	private String[] namesInPath;
	public String fullCommand;
	protected String addedEntityName;
	
	public AddEntity(String command, FileSystem fileSystem) {
		super(command, fileSystem);
		this.fullCommand = command;
	}
	@Override
	public void setPath(String path) {
		if(path.equals("/")) {
			this.entityLocation = fileSystem.getRootOfRootDirectory();
			this.addedEntityName = path;
			return;
		}
		
		/*
		 * ends with /
		 */
		if (path.lastIndexOf("/") == path.length() - 1) {
			path = path.substring(0, path.length() - 1);
		}
		
		if (path.lastIndexOf("/") >= 0) {
			this.entityPath = path.substring(0, path.lastIndexOf("/"));
			this.addedEntityName = path.substring(path.lastIndexOf("/") + 1);
			this.setNamesInPath(entityPath.split("/"));
		} else {
			this.entityPath = null;
			this.addedEntityName = path;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#verifyErrors()
	 * v2. extends Cd
	 */
	
	public boolean verifyErrors() {
		if (super.verifyErrors()) {
			return true;
		}
		
		if (entityLocation.writingPermission(fileSystem.getCurrentUser()) == 0) {
			System.out.println("-5: " + fullCommand + ": No rights to write");
			return true;
		}
		
		if (entityLocation.getNextFile(addedEntityName) != null) {
			
			if (entityLocation.getNextFile(addedEntityName).getType().equals("f")) {
				this.pathIsAlreadyAFile();
				return true;
			} else {
				System.out.println("-1: " + fullCommand + ": Is a directory");
				return true;
			}
		}
		
		return false;
		
	}
	
	public abstract boolean execute();
	
	public abstract void pathIsAlreadyAFile();
	
	public String getFullCommand() {
		return this.fullCommand;
	}
	
	public String getEntityPath() {
		return this.entityPath;
	}
	
	public String[] getNamesInPath() {
		return namesInPath;
	}

	public void setNamesInPath(String[] namesInPath) {
		this.namesInPath = namesInPath;
	}
	
	public String pathToDir(String command) {
		if (command.lastIndexOf("/") >= 0) {
			return command.substring(0, command.lastIndexOf("/"));
		}
		else {
			return "";
		}
	}

}
