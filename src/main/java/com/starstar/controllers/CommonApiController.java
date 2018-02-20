package com.starstar.controllers;

import com.starstar.Utils.ImageUtils;
import com.starstar.models.User;
import com.starstar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/v1/profile")
//@PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
public class CommonApiController {
    public static final String FILE_PATH = "/avatar";
    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public  User updateProfile(@PathVariable("id") String id,
                              @RequestBody User u){


        try {
            User user=userService.loadUserByUserId(id);
            System.out.println("#####################"+user.getUsername());
            if(user!=null){
                if(u.getFirst()!=null){
                    user.setFirst(u.getFirst());
                }
                if(u.getLast()!=null){
                    user.setLast(u.getLast());
                }
                if(u.getEmail()!=null){
                    user.setEmail(u.getEmail());
                }
                if(u.getPassword()!=null){
                    user.setPassword(u.getPassword());
                }
                if(u.getPhones()!=null){
                    user.setPhones(u.getPhones());
                }
                System.out.println("#####################2"+user.getLast());
                user = userService.save(user);
                user.setPassword("");
                System.out.println("#####################3"+user.getLast());
                return user;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User();
    }

    @RequestMapping(value = "avatars/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Boolean uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable("id") String id, HttpSession session) throws IOException {
        if (file.isEmpty()) return false;
        String fileNameOriginal = file.getOriginalFilename();
        String mimeType = session.getServletContext().getMimeType(fileNameOriginal);
        System.out.print("update file process!!");
        if (!mimeType.startsWith("image/")) {
            return false;
        }
        String type = ImageUtils.getImageType(file.getBytes());
        if (type.equals(ImageUtils.IMAGE_NOT_IMAGE)) {
            return false;
        }

        File tempFile = new File(System.getProperty("user.home") + FILE_PATH, id + type);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        File file1 = new File(System.getProperty("user.home") + FILE_PATH, id + ImageUtils.IMAGE_JPEG);
        File file2 = new File(System.getProperty("user.home") + FILE_PATH, id + ImageUtils.IMAGE_PNG);
        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        try {
            file.transferTo(tempFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @RequestMapping(value = "avatars/{id}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable("id") String id, HttpServletResponse res) {
        File file = null;
        FileInputStream fis = null;
        File file1 = new File(System.getProperty("user.home") + FILE_PATH, id + ImageUtils.IMAGE_JPEG);
        File file2 = new File(System.getProperty("user.home") + FILE_PATH, id + ImageUtils.IMAGE_PNG);
        if (file1.exists()) {
            file = file1;
        }
        if (file2.exists()) {
            file = file2;
        }
        if (file != null) {
            try {
                fis = new FileInputStream(file);
                byte[] b = new byte[fis.available()];
                fis.read(b);
                String fileType = new MimetypesFileTypeMap().getContentType(file);
                res.setContentType(fileType);
                OutputStream out = res.getOutputStream();
                out.write(b);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                res.getWriter().println("Avatar does not exists");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    }
