/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hanan
 */
@WebServlet(urlPatterns = {"/search"})
public class search extends HttpServlet {



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
        String search= request.getParameter("search");
        
        String filename="D:\\FCAI\\four sems1\\soa lecs\\ass3\\WebApplication2\\src\\java\\employee.json";
        
        JsonReader jsonReader = Json.createReader(new FileReader(filename));
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();
        boolean flag = true;
        int count = 0; 
        for (JsonValue jsonValue : jsonArray) {
            JsonObject employee = (JsonObject) jsonValue;
            int search_id = employee.getInt("EmployeeID",-1);
            String search_des = employee.getString("Designation","");
            
            if(search_des.equals(search)){
                out.println("FirstName: " + employee.getString("FirstName")+"<br/>");
                out.println("LastName: " + employee.getString("LastName")+"<br/>");
                out.println("EmployeeID: " + employee.getInt("EmployeeID",-1)+"<br/>");
                out.println("Designation: " +search_des +"<br/>");
                JsonArray knownLanguages = employee.getJsonArray("KnownLanguages");
                out.println("KnownLanguages:"+"<br/>");
                flag=false;
                count++;
                for (JsonValue languageValue : knownLanguages) {
                    JsonObject languageObj = (JsonObject) languageValue;
                    out.println("  LanguageName: " + languageObj.getString("LanguageName")+"<br/>");
                    out.println("    ScoreOutof100: " + languageObj.getInt("ScoreOutof100")+"<br/>");
                }
                
                out.print("<hr/>");
            } else if(search.matches("\\d+")){
                if(Integer.parseInt(search)== search_id){
                out.println("FirstName: " + employee.getString("FirstName")+"<br/>");
                out.println("LastName: " + employee.getString("LastName")+"<br/>");
                out.println("EmployeeID: " + employee.getInt("EmployeeID",-1)+"<br/>");
                out.println("Designation: " +search_des +"<br/>");
                JsonArray knownLanguages = employee.getJsonArray("KnownLanguages");
                out.println("KnownLanguages:"+"<br/>");
                flag=false;
                count++;
                for (JsonValue languageValue : knownLanguages) {
                    JsonObject languageObj = (JsonObject) languageValue;
                    out.println("  LanguageName: " + languageObj.getString("LanguageName")+"<br/>");
                    out.println("    ScoreOutof100: " + languageObj.getInt("ScoreOutof100")+"<br/>");
                }
                out.print("<hr/>");
            }
        }
        }
        if(flag){
            out.print("<h1>NOT FOUND</h1>");
        }else{
            out.print("The number of matched records : "+count);
        }
        
    }


}
