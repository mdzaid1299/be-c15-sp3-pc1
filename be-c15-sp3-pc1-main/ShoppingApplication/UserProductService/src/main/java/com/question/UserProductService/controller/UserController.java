package com.question.UserProductService.controller;

import com.question.UserProductService.domain.Product;
import com.question.UserProductService.domain.User;
import com.question.UserProductService.exception.ProductNotFoundException;
import com.question.UserProductService.exception.UserAlreadyExistsException;
import com.question.UserProductService.exception.UserNotFoundException;
import com.question.UserProductService.rabbitMQ.CommonUser;
import com.question.UserProductService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/userdata/api/")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user) throws UserAlreadyExistsException {
        ResponseEntity responseEntity = null;
        try{
            user.setProductList(new ArrayList<>());
            responseEntity = new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
        }catch (UserAlreadyExistsException e){
            throw new UserAlreadyExistsException();
        }catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @PostMapping("/common")
    public ResponseEntity<?> addUserDetails(@RequestBody CommonUser commonUser){
        return new ResponseEntity<>(userService.addUser1(commonUser),HttpStatus.OK);
    }
    @PutMapping("/product/addProduct/{userId}")
    public ResponseEntity<?> addProductForUser(@PathVariable String userId, @RequestBody Product product) throws UserNotFoundException {
        ResponseEntity responseEntity = null;
        try{
            responseEntity = new ResponseEntity<>(userService.addProductForUser(userId,product), HttpStatus.CREATED);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @DeleteMapping("/product/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProductForUser(@PathVariable int productId,@RequestBody User user) throws ProductNotFoundException, UserNotFoundException {
        ResponseEntity responseEntity = null;
        try{
            responseEntity = new ResponseEntity<>(userService.deleteProductForUser(user.getUserId(), productId), HttpStatus.OK);
        }catch (ProductNotFoundException e){
            throw new ProductNotFoundException();
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        } catch(Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @GetMapping("/product/products")
    public ResponseEntity<?> getProductsForUser(@RequestBody User user) throws UserNotFoundException {
        ResponseEntity responseEntity = null;
        try{
            responseEntity = new ResponseEntity<>(userService.getProductForUser(user.getUserId()), HttpStatus.OK);
        }catch(UserNotFoundException e){
            throw new UserNotFoundException();
        }
        catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
