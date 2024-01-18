/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hanan
 */
@WebServlet(urlPatterns = {"/update_survlet"})
public class update_survlet extends HttpServlet {

    
    private static void saveXmlToFile(Document doc, String filePath, PrintWriter out) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            // Write the transformed XML to the file
            transformer.transform(source, new StreamResult(fileWriter));
            out.println("Student data updated successfully.");
        } catch (IOException e) {
            out.println("Error writing to the file: " + e.getMessage());
        }
    }
    
     private void updateFn(Document doc, String id,PrintWriter out,String frname,String lsname,String gen,String gpa,String lv,String ad,String path) throws TransformerException 
    {
        NodeList studentNodes = doc.getElementsByTagName("student");
        int count = 0;
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Element studentElement = (Element) studentNodes.item(i);
            String searchIDVal = studentElement.getAttribute("ID");

            if (searchIDVal.equals(id)) {
                Element fr = (Element) studentElement.getElementsByTagName("firstname").item(0);
                Element lname = (Element) studentElement.getElementsByTagName("lastname").item(0);
                Element gender = (Element) studentElement.getElementsByTagName("gender").item(0);
                Element gpaElement = (Element) studentElement.getElementsByTagName("GPA").item(0);
                Element level = (Element) studentElement.getElementsByTagName("level").item(0);
                Element address = (Element) studentElement.getElementsByTagName("address").item(0);
                
                if (frname != null && !frname.isEmpty()) { 
                    // Update the first name element
                    fr.setTextContent(frname);
                }

                if (lsname != null && !lsname.isEmpty()) {
                    // Update the last name element
                    lname.setTextContent(lsname);
                }
                
                if (gen!=null&& !gen.isEmpty()) {
                    // Update the gender element
                    
                    gender.setTextContent(gen);
                }

                if (gpa != null && !gpa.isEmpty()) {                    
                    // Update the GPA element
                    gpaElement.setTextContent(gpa);
                }


                if (lv != null && !lv.isEmpty()) {                   
                    // Update the level element
                    level.setTextContent(lv);
                }

                if (ad != null && !ad.isEmpty()) {
                    // Update the address element
                    address.setTextContent(ad);
                }
                count++;
                saveXmlToFile(doc,path,out);
                break;
                
            }
        }
        if (count == 0) {
            out.println("No student found with ID: " + id);
        }
    }


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
        String id= request.getParameter("ID");
        String frname = request.getParameter("firstname");
        String lsname = request.getParameter("lastname");
        String gen = request.getParameter("gender");
        String gpa = request.getParameter("gpa");
        String lv = request.getParameter("level");
        String ad = request.getParameter("address");
        try{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc;
        try {
            doc = dBuilder.parse(new File("D:\\FCAI\\four sems1\\WebApplication1\\src\\java\\students.xml"));
            updateFn(doc,id,out,frname,lsname,gen,gpa,lv,ad,"D:\\FCAI\\four sems1\\WebApplication1\\src\\java\\students.xml");
        } catch (SAXException | IOException ex) {
            // Create a new document and root element if the XML file doesn't exist
            out.print("not found file");
        }
        
    }catch(ParserConfigurationException | TransformerException a){
        out.println("<html><body><h2>Error: " +a.getMessage() + "</h2></body></html>");
    }
    }


}
