package com.example.Profile.Controler;

import com.example.Profile.Model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class Controller {
    @Autowired
    private Connection db;

    @PostMapping("/account")
    public int createAccount(@RequestParam String email, @RequestParam String passw){
        try{
            PreparedStatement stm = db.prepareStatement("insert into account values(0, ?, ?, null);");
            stm.setString(1, email);
            stm.setString(2, passw);
            stm.execute();
            ResultSet rs = db.prepareStatement("select last_insert_id() as id;").executeQuery();
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("Error in query -> " + e.getSQLState());
            e.printStackTrace();
        }
        return -1;
    }

    @GetMapping("/account")
    public int login(@RequestParam String email, @RequestParam String passw){
        try {
            PreparedStatement stm = db.prepareStatement("select id from account where email = ? and passw = ?");
            stm.setString(1, email);
            stm.setString(2, passw);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error in query -> " + e.getSQLState());
            e.printStackTrace();
        }
        return -1;
    }

    //@DeleteMapping("/account/{id}")
    // Under construction
//    public void deleteAccount(@PathVariable int id){
//        try {
//            PreparedStatement stm = db.prepareStatement("");
//        } catch (SQLException e) {
//
//        }
//    }

    @PostMapping("/account/{id}/profile")
    public int createProfile(@PathVariable int id, @RequestBody Profile p){
        try {
            PreparedStatement stm = db.prepareStatement("insert into profile values(0, ?, ?, ?, ?);");
            stm.setString(1, p.getName());
            stm.setString(2, p.getFname());
            stm.setString(3, p.getEmail());
            stm.setString(4, p.getPhone());
            stm.execute();
            stm = db.prepareStatement("select last_insert_id() as id");
            ResultSet rs = stm.executeQuery();
            rs.next();
            int ret =  rs.getInt("id");
            stm = db.prepareStatement("update account set profileId=? where id=?;");
            stm.setInt(1, ret);
            stm.setInt(2, id);
            stm.execute();
            return ret;
        } catch (SQLException e) {
            System.out.println("Error in query -> " + e.getSQLState());
            e.printStackTrace();
        }
        return -1;
    }

    @PostMapping("/profile/{id}/pic")
    public void createPic(@PathVariable int id,@RequestBody MultipartFile file){
        try{
            PreparedStatement stm = db.prepareStatement("insert into picture values(?, ?, ?, ?);");
            stm.setInt(1, id);
            stm.setString(2, file.getName());
            stm.setInt(3, (int) file.getSize());
            stm.setBlob(4, new SerialBlob(file.getBytes()));
            stm.execute();
        } catch (SQLException e) {
            System.out.println("Error in query -> " + e.getSQLState());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
