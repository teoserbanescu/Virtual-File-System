package Commands;

import OS.FileSystem;
import OS.Entity;
import Utils.ErrorFlag;
import Utils.Pair;

public class Cd implements Command{
	
	protected String entityPath;
	protected String[] namesInPath;
	protected FileSystem fileSystem;
	protected String fullCommand;
	protected Entity entityLocation;
	protected ErrorFlag error;
	
	public Cd(String command, FileSystem fileSystem) {
		this.fileSystem = fileSystem;
		this.fullCommand = command;
		this.setArguments(command);
		
	}
	/*
	 * set arguments
	 */
	
	public void setArguments(String command) {
		entityPath = command.split(" ", 2)[1];
		this.setPath(entityPath);
	}
	/*
	 * path
	 */
	public void setPath(String path) {
		if(path.equals("/")) {
			this.entityLocation = fileSystem.getRootOfRootDirectory();
			return;
		}
		if (path.lastIndexOf("/") == path.length()) {
			path = path.substring(0, path.length() - 2);
		}
		namesInPath = entityPath.split("/");
	}
	
	@Override
	public boolean verifyErrors() {
		
		Pair<Entity, ErrorFlag> entityInfo = new Pair<Entity, ErrorFlag>();
		
		if (this.entityLocation == null) {
			if (this.getEntityPath() == null) {
				entityInfo.setFirst(fileSystem.getCurrentDirectory());
				if (fileSystem.getCurrentDirectory().executingPermission(fileSystem.getCurrentUser()) == 0) {
					entityInfo.setSecond(ErrorFlag.NoRightsToExecute);
				} else {
					entityInfo.setSecond(ErrorFlag.Succes);
				}
				
			} else
			
			if (this.getEntityPath().length() == 0) {
				entityInfo.setFirst(fileSystem.getRootDirectory());
				if (fileSystem.getRootDirectory().executingPermission(fileSystem.getCurrentUser()) == 0) {
					entityInfo.setSecond(ErrorFlag.NoRightsToExecute);
				} else {
					entityInfo.setSecond(ErrorFlag.Succes);
				}
			} else
			if (this.getEntityPath().startsWith("/")) {
				String leftNamesInPath[] = new String[this.getNamesInPath().length - 1];
				System.arraycopy(this.getNamesInPath(), 1, leftNamesInPath, 0, this.getNamesInPath().length - 1);
				entityInfo = fileSystem.getFileTFromPath(leftNamesInPath, fileSystem.getRootDirectory());
			} else {
				entityInfo = fileSystem.getFileTFromPath(this.getNamesInPath(), fileSystem.getCurrentDirectory());
			}
			
			entityLocation = entityInfo.getFirst();
			error = entityInfo.getSecond();
		} else {
			error = ErrorFlag.Succes;
		}
		
		if (entityLocation == null || error.equals(ErrorFlag.NoSuchEntity)) {
			System.out.println("-2: " + this.getFullCommand() + ": No such directory");
			return true;
		}
		
		if (error.equals(ErrorFlag.NotDirectory) ||
				(entityLocation.getType().equals("f") && this.pathShouldBeDirectory())) {
			System.out.println("-3: " + this.getFullCommand() + ": Not a directory");
			return true;
		}
		
		if (error.equals(ErrorFlag.NoRightsToExecute) ||
				entityLocation.executingPermission(fileSystem.getCurrentUser()) == 0) {
			System.out.println("-6: " + this.getFullCommand() + ": No rights to execute");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean execute() {
		if (verifyErrors() == true) {
			return false;
		}
		fileSystem.setCurrentDirectory(entityLocation);
		return true;
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
	
	/*
	 * Path should be directory for cd
	 */
	
	public boolean pathShouldBeDirectory() {
		return true;
	}
}
