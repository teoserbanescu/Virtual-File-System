package OS;

import java.util.ArrayList;
import java.util.List;


public class Directory extends Entity{
	
	public Directory(String owner, String name) {
		super(owner, name);
		subordinates = new ArrayList<Entity>();
	}
	
	@Override
	public String getType() {
		return "d";
	}
	@Override
	public void print(int level) {
		super.print(level);
		for (Entity subordinate: subordinates) {
			subordinate.print(level + 1);
		}
	}
	
	@Override
	public void ls() {
		for (Entity subordinate : subordinates) {
			subordinate.lsFile();
		}
	}
	@Override
	public void catContent() {
	}
	@Override
	public void setContent(String content) {
		
	}
	
	public void addSubordinate(Entity newFile) {
		if (! (newFile.getName().equals("/") && this.getName().equals("/")) ) {
			newFile.setParent(this);
		}
		subordinates.add(newFile);
		/*
		 * Problem: . and ..
			If parent is deleted all the elements from the subtree are deleted(even the current element)
			Solution: add . and .. in the subordinates list
			. and .. are PRIVATE(boolean variable in Entity) directories. So I will not print them
			Therefore when i create the subordinates list in a dir i first add . and ..
			but for the moment everytime I check the path to the file/dir
			if . and .. appear I will treat them with some hardcoded ifs srry
		 */
	}
	@Override
	public List<Entity> getSubordinates() {
		return subordinates; // can return null if directory is leaf
	}
	@Override
	public void setSubordinates(List<Entity> subordinates) {
		this.subordinates = subordinates;
	}
	@Override
	public Entity getNextFile(String targetName) {
		switch (targetName) {
		case ".." : 
			return this.getParent();
		case "." : 
			return this;
		}
		for (Entity file : subordinates) {
			if (file.getName().equals(targetName)) {
				return file;
			}
		}
		return null;
	}
	
}
