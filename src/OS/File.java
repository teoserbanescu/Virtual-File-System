package OS;

import java.util.List;

/*public class File extends FileT{
	
}*/

public class File extends Entity{
	/*
	 * Files and directories have a name passed as a parameter in the constructor
	 * That name is NOT the absolute path
	 * The absolute path is built recursively every time we need it
	 * !When we construct a new file/dir, we must add it in its parent list of subordinates afterwards
	 * !When we delete a file/dir, we must first delete it from its parent list of subordinates
	 * ! and then effectively delete it ( why delete it when u have garbage collector do the job for you? )
	 */
	
	public File(String owner, String name) {
		super(owner, name);
		this.setContent(new String());
	}
	
	public String getType() {
		return "f";
	}
	@Override
	public void ls() {
		this.lsFile();
	}
	@Override
	public void addSubordinate(Entity newFile) {
	}
	@Override
	public List<Entity> getSubordinates() {
		return subordinates;
	}
	@Override
	public void setSubordinates(List<Entity> subordinates) {
		this.subordinates = subordinates;
	}
	@Override
	public Entity getNextFile(String targetName) {
		return null;
	}
	@Override
	public void catContent() {
		System.out.println(this.content);
	}
	@Override
	public void setContent(String content) {
		this.content = content;
	}
	
}
