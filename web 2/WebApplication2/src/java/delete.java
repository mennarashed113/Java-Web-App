/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hanan
 */
@WebServlet(urlPatterns = {"/delete"})
public class delete extends HttpServlet {
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String delete= request.getParameter("delete");
        
        String filename="D:\\FCAI\\four sems1\\soa lecs\\ass3\\WebApplication2\\src\\java\\employee.json";
        
        JsonReader jsonReader = Json.createReader(new FileReader(filename));
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();
        
        JsonArrayBuilder newJsonArray = Json.createArrayBuilder();

        for (JsonValue jsonValue : jsonArray) {
            JsonObject employee = (JsonObject) jsonValue;
            int delete_id = employee.getInt("EmployeeID",-1);
            
            if(Integer.parseInt(delete)!= delete_id){
                newJsonArray.add(employee);
            }
        }
        JsonArray updatedArray = newJsonArray.build();
        Map<String, Boolean> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory writerFactory = Json.createWriterFactory(config);
            // Write the updated JSON array to the file
            try (JsonWriter jsonWriter = writerFactory.createWriter(new FileWriter(filename))) {
                jsonWriter.writeArray(updatedArray);
                out.println("Data has been Deleted successfully.");
            }catch (Exception e) {
            Logger.getLogger(addEmp.class.getName()).log(Level.SEVERE, null, e);
            out.println("error "+e);
            out.println("Error saving JSON Array to file: " + e.getMessage());
        }
    }
}

