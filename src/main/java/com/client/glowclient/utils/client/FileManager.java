/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 */
package com.client.glowclient.utils.client;

import com.client.glowclient.utils.base.setting.builder.SettingManager;
import com.client.glowclient.utils.mod.mods.friends.Enemy;
import com.client.glowclient.utils.mod.mods.friends.EnemyManager;
import com.client.glowclient.utils.mod.mods.friends.Friend;
import com.client.glowclient.utils.mod.mods.friends.FriendManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;

public class FileManager {
    private static final FileManager INSTANCE = new FileManager();
    private final File baseDirectory = new File(FileManager.getWorkingDir(), "config/glowclient");
    private final File settingsDirectory;

    public static FileManager getInstance() {
        return INSTANCE;
    }

    private static File getWorkingDir() {
        return Paths.get("", new String[0]).toAbsolutePath().toFile();
    }

    private FileManager() {
        this.baseDirectory.mkdirs();
        this.settingsDirectory = new File(FileManager.getWorkingDir(), "config/glowclient/settings");
        this.baseDirectory.mkdirs();
    }

    public void saveConfig() {
        String config = SettingManager.encodeConfig();
        try {
            File file = new File(this.settingsDirectory.getPath() + File.separatorChar + "Config.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            IOUtils.write((byte[])config.getBytes(), (OutputStream)new FileOutputStream(file));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFriends() {
        try {
            File file = new File(this.settingsDirectory, "Friends.json");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            FriendManager.getFriends();
            for (Friend friend : FriendManager.friendsList) {
                out.write(friend.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void saveEnemies() {
        try {
            File file = new File(this.settingsDirectory, "Enemies.json");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            EnemyManager.getEnemies();
            for (Enemy enemy : EnemyManager.enemyList) {
                out.write(enemy.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void loadConfig() {
        String launcherJson = null;
        File file = new File(this.settingsDirectory.getPath() + File.separatorChar + "Config.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
                this.saveConfig();
                launcherJson = "{}";
            } else {
                launcherJson = IOUtils.toString((InputStream)new FileInputStream(file));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        SettingManager.decodeConfig(launcherJson);
    }

    public void loadFriends() {
        try {
            String line;
            File file = new File(this.settingsDirectory, "Friends.json");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                FriendManager.getFriends().addFriend(name);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEnemies() {
        try {
            String line;
            File file = new File(this.settingsDirectory, "Enemies.json");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                EnemyManager.getEnemies().addEnemy(name);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        this.saveConfig();
        this.saveFriends();
        this.saveEnemies();
    }

    public void loadAll() {
        this.loadConfig();
        this.loadEnemies();
        this.loadFriends();
    }
}

