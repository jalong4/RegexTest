package ca.jimlong.regextest.Models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import com.google.gson.Gson;

public class Settings {

    private String logFilename = "";

	public Settings(File settingsFile) {
         
        if (!settingsFile.exists()) {
        	System.out.println("Missing " + settingsFile.getName()  + " file, exitting...");
            return;
        }
         
        String json;
		try {
			json = new String(Files.readAllBytes(settingsFile.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        Gson gson = new Gson();

		Settings settings = gson.fromJson(json, Settings.class);

		logFilename = settings.logFilename;

		if (logFilename.startsWith("~" + File.separator)) {
			logFilename = System.getProperty("user.home") + logFilename.substring(1);
		}
	}

	public String getLogFilename() {
		return logFilename;
	}


    
}