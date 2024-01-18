/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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
@WebServlet(urlPatterns = {"/searchnew"})
public class searchnew extends HttpServlet {


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
        String lang= request.getParameter("lang");
        String sc= request.getParameter("score");
        String oper= request.getParameter("operation");
       
        String filename="D:\\FCAI\\four sems1\\soa lecs\\ass3\\WebApplication2\\src\\java\\employee.json";
        
        JsonReader jsonReader = Json.createReader(new FileReader(filename));
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();
        boolean flag = true;
        int count = 0; 
        JsonArrayBuilder resultArrayBuilder = Json.createArrayBuilder();

        for (JsonValue jsonValue : jsonArray) {
            JsonObject employee = (JsonObject) jsonValue;
            JsonArray knownLanguages = employee.getJsonArray("KnownLanguages");
            for (JsonValue languageValue : knownLanguages) {
                JsonObject languageObj = (JsonObject) languageValue;
                if(oper.equals("equal")){
                    if(languageObj.getString("LanguageName").toLowerCase().equals(lang.toLowerCase())&&languageObj.getInt("ScoreOutof100")==Integer.parseInt(sc)){
                       resultArrayBuilder.add(employee);
                       count++;
                    }
                }else if(oper.equals("higher")){
                    if(languageObj.getString("LanguageName").toLowerCase().equals(lang.toLowerCase())&&languageObj.getInt("ScoreOutof100")>Integer.parseInt(sc)){
                        resultArrayBuilder.add(employee);
                        count++;
                    }
                }else if(oper.equals("lower")){
                    if(languageObj.getString("LanguageName").toLowerCase().equals(lang.toLowerCase())&&languageObj.getInt("ScoreOutof100")<Integer.parseInt(sc)){
                       resultArrayBuilder.add(employee);
                       count++;
                    }
                }
            }
        }
        
        if(count==0){
            out.print("<h1>No Match</h1>");
        }else{
            JsonArray sortedResult = resultArrayBuilder.build();
            JsonArrayBuilder builder = Json.createArrayBuilder();
            //bymshy 3ala el known lang array , ygeb el lang = lang w ya5od el score bta3aha as a comparitor 34an y3ml 3aleh el sort 
           sortedResult.getValuesAs(JsonObject.class)
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getJsonArray("KnownLanguages")
                        .getValuesAs(JsonObject.class)
                        .stream()
                        .filter(langObj -> langObj.getString("LanguageName").equalsIgnoreCase(lang))
                        .findFirst()
                        .map(langObj -> langObj.getInt("ScoreOutof100"))
                        .orElse(0)))
                .forEach(builder::add);
            
            out.println(builder.build().toString());
        }
    }

}
