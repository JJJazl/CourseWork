package com.example.coursework;

import com.example.coursework.dao.DaoFactory;
import com.example.coursework.dao.FileDao;
import com.example.coursework.dao.UserDao;
import com.example.coursework.domain.User;
import com.example.coursework.exception.ConfirmPasswordException;
import com.example.coursework.exception.IncorrectPasswordException;
import com.example.coursework.exception.UserNotFoundException;
import com.example.coursework.exception.UserWithSameUsernameAlreadyExist;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class ActionManager {
    private static final String ALGORITHM = "AES";
    private final static String FIRST_PART_OF_PATH = "src/main/resources/files/";
    private static SecretKeySpec secretKey;
    private static ActionManager actionManager;
    private static byte[] key;
    private final DaoFactory daoFactory;
    private final UserDao userDao;
    private final FileDao fileDao;
    private static String secondPartOfFile;

    public ActionManager() {
        this.daoFactory = DaoFactory.getInstance();
        this.userDao = daoFactory.getUserDao();
        this.fileDao = daoFactory.getFileDao();
    }

    public static ActionManager getInstance() {
        if (actionManager == null) {
            actionManager = new ActionManager();
        }
        return actionManager;
    }
    public void login(String username, String password) {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Incorrect username or password");
        }
        String decodedPassword = new String(Base64.getDecoder().decode(user.orElseThrow().getPassword()));
        if (!password.equals(decodedPassword)) {
            throw new IncorrectPasswordException("Incorrect username or password");
        }
        secondPartOfFile = username;
    }
    public void createUserAndFile(String username, String password, String confirmPassword) {
        if (userDao.existByUsername(username)) {
            throw new UserWithSameUsernameAlreadyExist("User with same name already exist!");
        }
        if (!password.equals(confirmPassword)) {
            throw new ConfirmPasswordException("Password did not match!");
        }
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        int userId = userDao.create(username, encodedPassword);
        createFile(FIRST_PART_OF_PATH  + username, userId);
        secondPartOfFile = username;
    }
    private void createFile(String path, int userId) {
        File file = new File(path);
        try {
            file.createNewFile();
            fileDao.create(path, userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readFromFile() {
        File file = new File(FIRST_PART_OF_PATH + secondPartOfFile);
        try(BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(file))) {
            int c;
            StringBuilder builder = new StringBuilder();
            while((c = bis.read()) != -1) {
                builder.append((char) c);
            }
            return builder.toString();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void writeToFile(String text) {
        File file = new File(FIRST_PART_OF_PATH + secondPartOfFile);
        try(BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(file, false))) {
            bos.write(text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void prepareSecretKey(String myKey) {
        MessageDigest sha;

        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }
    public String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecretKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypt password");
        }
        return null;
    }
    public String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecretKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error while decrypt password");
        }
        return null;
    }

    public boolean deleteAccount() {
        com.example.coursework.domain.File file =
                fileDao.findByFilePath(FIRST_PART_OF_PATH + secondPartOfFile);
        File deletedFile = new File(FIRST_PART_OF_PATH + secondPartOfFile);
        if (deletedFile.delete()) {
            fileDao.delete(file.getId());
            userDao.delete(file.getUserId());
            return true;
        }
        return false;
    }
}

