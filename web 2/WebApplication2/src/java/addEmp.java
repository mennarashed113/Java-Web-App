/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author hanan
 */
@WebServlet(urlPatterns = {"/addEmp"})
public class addEmp extends HttpServlet {

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
        
        String filename="D:\\FCAI\\four sems1\\soa lecs\\ass3\\WebApplication2\\src\\java\\employee.json";
        
        try{
             
            String first = request.getParameter("firstName");
            String last = request.getParameter("lastName");
            int id = Integer.parseInt(request.getParameter("ID"));
            String des = request.getParameter("designation");
                 
            int num=Integer.parseInt(request.getParameter("num"));
            JsonArrayBuilder knownLanguagesArrayBuilder = Json.createArrayBuilder();
            for (int i = 1; i <= num; i++) {
                String lang = request.getParameter("lang" + i);
                int score = Integer.parseInt(request.getParameter("score" + i));
                JsonObject languageObject = Json.createObjectBuilder()
                        .add("LanguageName", lang)
                        .add("ScoreOutof100", score)
                        .build();

                knownLanguagesArrayBuilder.add(languageObject);
            }
            JsonObject newEmployee = Json.createObjectBuilder()
                .add("FirstName", first)
                .add("LastName", last)
                .add("EmployeeID", id)
                .add("Designation", des)
                .add("KnownLanguages",knownLanguagesArrayBuilder).build();

            
            // Read existing data from the JSON file
            JsonReader jsonReader = Json.createReader(new FileReader(filename));
            JsonArray jsonArray = jsonReader.readArray();
            jsonReader.close();

            // Add the new employee to the existing array
            JsonArrayBuilder newArrayBuilder = Json.createArrayBuilder();
            
            for (JsonValue jsonValue : jsonArray) {
            JsonObject employee = (JsonObject) jsonValue;
            int delete_id = employee.getInt("EmployeeID",-1);
            
            if(id== delete_id){
                out.print("already exist");
            }
            else{
                newArrayBuilder.add(jsonValue);
                newArrayBuilder.add(newEmployee);
                JsonArray updatedData = newArrayBuilder.build();

            Map<String, Boolean> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory writerFactory = Json.createWriterFactory(config);
            // Write the updated JSON array to the file
            try (JsonWriter jsonWriter = writerFactory.createWriter(new FileWriter(filename))) {
                jsonWriter.writeArray(updatedData);
                out.println("Data has been written to the file successfully.");
            }
            }
            
        }
            
            

        } catch (Exception e) {
            Logger.getLogger(addEmp.class.getName()).log(Level.SEVERE, null, e);
            out.println("error "+e);
            System.err.println("Error saving JSON Array to file: " + e.getMessage());
        }
    }
}
