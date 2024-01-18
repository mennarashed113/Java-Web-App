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
@WebServlet(urlPatterns = {"/update"})
public class update extends HttpServlet {


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
        PrintWriter out = response.getWriter();
        String id= request.getParameter("up");
        String des=request.getParameter("des");
        
        String filename="D:\\FCAI\\four sems1\\soa lecs\\ass3\\WebApplication2\\src\\java\\employee.json";
        
        JsonReader jsonReader = Json.createReader(new FileReader(filename));
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();
        
        JsonArrayBuilder updatedArrayBuilder = Json.createArrayBuilder();

        for (JsonValue jsonValue : jsonArray) {
            JsonObject employee = (JsonObject) jsonValue;
            int employeeID = employee.getInt("EmployeeID", -1);
            
            if (Integer.parseInt(id) == employeeID) {
                // Update the Designation for the matching employee ID
                JsonObject updatedEmployee = Json.createObjectBuilder()
                        .add("FirstName",employee.getString("FirstName"))
                        .add("LastName",employee.getString("LastName"))
                        .add("EmployeeID",employee.getInt("EmployeeID", -1))
                        .add("Designation", des)
                        .add("KnownLanguages",employee.getJsonArray("KnownLanguages"))
                        .build();
                updatedArrayBuilder.add(updatedEmployee);
            } else {
                updatedArrayBuilder.add(employee);
            }
        }

        JsonArray updatedArray = updatedArrayBuilder.build();
        
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);

        try (JsonWriter jsonWriter = writerFactory.createWriter(new FileWriter(filename))) {
            jsonWriter.writeArray(updatedArray);
            out.println("Data has been updated successfully.");
        } catch (Exception e) {
            out.println("Error updating Designation: " + e.getMessage());
        }
        
    }

}
