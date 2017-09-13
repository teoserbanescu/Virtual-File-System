package Commands;

import OS.FileSystem;

/*
 * commands that  need cd and path
 */

public abstract class CommandsUsingPath extends Cd{
	private String[] namesInPath;
	protected String entityName;
	
	public CommandsUsingPath(String command, FileSystem fileSystem) {
		super(command, fileSystem);
	}
	/*
	 * can have multiple types of arguments
	 */
	public abstract void setArguments(String command);
	/*
	 * (non-Javadoc)
	 * @see Commands.Cd#setPath(java.lang.String)
	 * set the entityPath and namesInPath to verify with Cd
	 * path is full path to the entity
	 */
	@Override
	public void setPath(String path) {
		if(path.equals("/")) {
			this.entityLocation = fileSystem.getRootOfRootDirectory();
			this.entityName = path;
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
			this.entityName = path.substring(path.lastIndexOf("/") + 1);
			this.namesInPath = entityPath.split("/");
		} else {
			this.entityPath = null;
			this.entityName = path;
		}
	}
	
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

}
