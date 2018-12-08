/**
 * Unlicensed code created by A Softer Space, 2018
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.krass;

import java.util.List;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.Utils;


public class Main {
	
	public final static String PROGRAM_TITLE = "Krass";
	public final static String VERSION_NUMBER = "0.0.0.1(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "8. December 2018";
	
	public static void main(String[] args) {
		
		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);
		
		addDisclaimerToProject("D:/prog/asofterspace/Krass/src/com/asofterspace/krass");
	}
	
	private static void addDisclaimerToProject(String projectPath) {

		boolean recursively = true;
		
		List<File> allFiles = addDisclaimerDir.getAllFiles(recursively);
		
		for (File file : allFiles) {
			if (file.getFilename().endsWith(".java")) {
				String newContent =
					"/**\r\n" +
					" * Unlicensed code created by A Softer Space, 2018\r\n" +
					" * www.asofterspace.com/licenses/unlicense.txt\r\n" +
					" */\r\n" +
					file.getContent();
				file.setContent(newContent);
				file.save();
			}
		}
	}
	
}
