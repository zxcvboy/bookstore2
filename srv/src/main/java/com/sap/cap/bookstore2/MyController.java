package com.sap.cap.bookstore2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sap.cds.Result;
import com.sap.cds.Row;
import com.sap.cds.Struct;
import com.sap.cds.feature.xsuaa.XsuaaUserInfo;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Upsert;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.CqnUpsert;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.persistence.PersistenceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cds.gen.sap.capire.bookstore2.Books;
import cds.gen.sap.capire.bookstore2.Books_;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
// @PreAuthorize("isAuthenticated()")
// @RequestMapping("/test-api")
@RequestMapping(value = "/rest")
public class MyController {
    @Autowired
    PersistenceService db;
    @Autowired
    XsuaaUserInfo xsuaaUserInfo;

    // @RequestMapping(method = RequestMethod.GET, value="/allstudent")
    @PreAuthorize("permitAll()")
    @GetMapping("/test")
    @ResponseBody
    public String getAllStudents() {

        return "Hello Rest" ;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/getAllBooks", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    // @ResponseBody
    public List<Books> getAllBooks() {

        
        List<Books> rtn = new ArrayList();
        CqnSelect sel = Select.from(Books_.class);

        Result rs = db.run(sel);

        for (Row row : rs) {
            Books tmp = row.as(Books.class);
            rtn.add(tmp);
        }
        // rtn = rs.listOf(Books.class);

        return rtn;

    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/create-book", method = RequestMethod.POST)
    @ResponseBody
    public Books createBooks(@RequestBody String a) {

        try {
               
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> data = objectMapper.readValue(a, Map.class);
            
            Books myBook = Struct.access(data).as(Books.class);
            
             CqnUpsert upsert = Upsert.into(Books_.class).entry(myBook);
                 

          Result rs = db.run(upsert);
        
          if(rs.rowCount()>0)
          {
                return rs.single(Books.class);
          }
          else
          {
                return null;
          }
            

        } catch (Exception e) {
            // TODO Auto-generated catch block`
            e.printStackTrace();
        }
        return null;
      
     
  
  }
}