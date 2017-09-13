import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Commands.Command;
import Commands.Factory;
import OS.FileSystem;

/*
 * 
 */

public class Test {
	
	public static void main(String[] args) {
		FileSystem fileSystem = new FileSystem();
		Factory factory = new Factory(fileSystem);
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(args[0]));
			String line = br.readLine();
			
			line = br.readLine();
			Command command;
		    while (line != null) {
		    	command = factory.getCommand(line);
		    	if (command != null) {
		    		command.execute();
		    	}
		        line = br.readLine();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileSystem.getRootDirectory().print(0);
	}
}
