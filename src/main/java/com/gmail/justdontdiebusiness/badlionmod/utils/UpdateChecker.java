package com.gmail.justdontdiebusiness.badlionmod.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

    private static final String currentVersion = "1.0.3";

    public static boolean hasUpdate() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/JustDontDie/BadlionStaffMod/master/src/main/resources/mcmod.info");
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = inputStream.readLine()) != null) {
                if (line.startsWith("  \"version\": ")) {
                    String version = line.split("  \"version\": \"")[1].split("\",")[0];

                    if (!version.equals(currentVersion)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
