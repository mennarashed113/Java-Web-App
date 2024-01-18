/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hanan
 */
@WebServlet (urlPatterns = {"/search_survlet"})
public class search_survlet extends HttpServlet {

//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet search_survlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet search_survlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }

    private static void searchFn(Document doc, String search,PrintWriter out,String att) 
    {
        NodeList studentNodes = doc.getElementsByTagName("student");
        int count = 0;
        for (int i = 0; i < studentNodes.getLength(); i++) 
        {
            Element studentElement = (Element) studentNodes.item(i);
            String searchAttVal = studentElement.getElementsByTagName(att).item(0).getTextContent();
            //String gpa = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
            if (searchAttVal.equals(search)) 
            {
                out.println("First Name: " + studentElement.getElementsByTagName("firstname").item(0).getTextContent()+"<br>");
                out.println("Last Name: " + studentElement.getElementsByTagName("lastname").item(0).getTextContent()+"<br>");
                out.println("GPA: " + studentElement.getElementsByTagName("GPA").item(0).getTextContent()+"<br>");
                out.println("Gender: " + studentElement.getElementsByTagName("gender").item(0).getTextContent()+"<br>");
                out.println("Level: " + studentElement.getElementsByTagName("level").item(0).getTextContent()+"<br>");
                out.println("Address: " + studentElement.getElementsByTagName("address").item(0).getTextContent()+"<br><br><hr>");
                count++;
            }
        }
        if(count==0){
            out.print("No matches found");
        }else if(count>0){
            out.print("The number of matches records is "+count);
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
        String search= request.getParameter("search");
        String att = request.getParameter("att");
        try{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc;
        try {
            doc = dBuilder.parse(new File("D:\\FCAI\\four sems1\\WebApplication1\\src\\java\\students.xml"));
            searchFn(doc,search,out,att);
        } catch (SAXException | IOException ex) {
            // Create a new document and root element if the XML file doesn't exist
            out.print("not found file");
        }
        
    }catch(ParserConfigurationException a){
        out.println("<html><body><h2>Error: " +a.getMessage() + "</h2></body></html>");
    }
}

//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>

}
