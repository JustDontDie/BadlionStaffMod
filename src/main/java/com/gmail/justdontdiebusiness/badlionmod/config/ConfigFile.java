package com.gmail.justdontdiebusiness.badlionmod.config;

import java.io.*;
import java.security.KeyPair;

public class ConfigFile {

    private File file;
    private File keyPairFile;

    public ConfigFile() {
        File folder = null;
        String os = System.getProperty("os.name").toUpperCase();

        if (os.contains("WIN")) {
            folder = new File(System.getenv("APPDATA") + File.separator + "Stove");
        } else if (os.contains("MAC")) {
            folder = new File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "Stove");
        } else if (os.contains("NUX")) {
            folder = new File(System.getProperty("user.dir") + "Stove");
        }

        if ((folder != null) && (!folder.exists())) {
            folder.mkdirs();
        }

        this.file = new File(folder.getPath() + File.separator + "cgfoi.dat");

        if (!this.file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.keyPairFile = new File(folder.getPath() + File.separator + "fcgio.dat");

        if (!this.keyPairFile.exists()) {
            try {
                this.keyPairFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getEncryptedKey() {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(":");

                reader.close();

                return args[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public KeyPair getKeyPair() {
        if (this.keyPairFile == null || !this.keyPairFile.exists()) return null;
        ObjectInputStream reader;
        KeyPair pair = null;

        try {
            reader = new ObjectInputStream(new FileInputStream(this.keyPairFile));

            pair = (KeyPair) reader.readObject();

            reader.close();

            return pair;
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            if (pair != null) {
                return pair;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setKey(String key) {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(file));

            writer.write("key:" + key);
            writer.newLine();

            writer.flush();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setKeyPair(KeyPair keyPair) {
        ObjectOutputStream writer;

        try {
            writer = new ObjectOutputStream(new FileOutputStream(this.keyPairFile));

            writer.writeObject(keyPair);
            writer.flush();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
