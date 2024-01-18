/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
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
@WebServlet (urlPatterns = {"/myserver"})
public class myserve extends HttpServlet {

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
//            out.println("<title>Servlet myserve</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet myserve at " + request.getContextPath() + "</h1>");
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
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html");
//        PrintWriter out = response.getWriter();
//        String name = request.getParameter("tex");
//        out.print("you enterd "+ name);
//    }
    
    public static boolean isIdUnique(Document doc, String idToCheck)
            throws ParserConfigurationException, IOException {

        Set<String> idSet = new HashSet<>();

        // Extract existing IDs from the XML
        NodeList nodeList = doc.getElementsByTagName("student"); // Assuming "student" is your XML element
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                // Assuming "id" is the attribute you want to check for uniqueness
                String id = element.getAttribute("ID");
                
                idSet.add(id);
                
            }
        }

        // Check if the ID to check is already in the set
        return !idSet.contains(idToCheck);
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

    int numStudents = Integer.parseInt(request.getParameter("num"));

    // Use try-with-resources to automatically close resources like PrintWriter
    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc;
        try {
            doc = dBuilder.parse(new File("D:\\FCAI\\four sems1\\WebApplication1\\src\\java\\students.xml"));
        } catch (SAXException | IOException ex) {
            // Create a new document and root element if the XML file doesn't exist
            doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("University");
            doc.appendChild(rootElement);
        }

        Element rootElement = doc.getDocumentElement();

        for (int i = 1; i <= numStudents; i++) {
            String id = request.getParameter("id_" + i);
            if(isIdUnique(doc,id)){
                String frname = request.getParameter("frname_" + i);
                String lsname = request.getParameter("lsname_" + i);
                String gen = request.getParameter("gender_" + i);
                String gpa = request.getParameter("gpa_" + i);
                String lv = request.getParameter("level_" + i);
                String ad = request.getParameter("address_" + i);

                Element studentElement = doc.createElement("student");
                studentElement.setAttribute("ID",id);
                Element frnameElement = doc.createElement("firstname");
                frnameElement.appendChild(doc.createTextNode(frname));
                Element lsnameElement = doc.createElement("lastname");
                lsnameElement.appendChild(doc.createTextNode(lsname));
                Element genElement = doc.createElement("gender");
                genElement.appendChild(doc.createTextNode(gen));
                Element gpaElement = doc.createElement("GPA");
                gpaElement.appendChild(doc.createTextNode(gpa));
                Element lvElement = doc.createElement("level");
                lvElement.appendChild(doc.createTextNode(lv));
                Element addElement = doc.createElement("address");
                addElement.appendChild(doc.createTextNode(ad));

                studentElement.appendChild(frnameElement);
                studentElement.appendChild(lsnameElement);
                studentElement.appendChild(genElement);
                studentElement.appendChild(gpaElement);
                studentElement.appendChild(lvElement);
                studentElement.appendChild(addElement);

                rootElement.appendChild(studentElement);
            
                try (FileOutputStream fos = new FileOutputStream("D:\\FCAI\\four sems1\\WebApplication1\\src\\java\\students.xml")) {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(fos);
                    transformer.transform(source, result);
                }
                out.println("<html><body><h2 style=\"color: green;\">Student's "+i+" data saved successfully!</h2></body></html>");
            }else{
                out.println("<html><body><h2 style=\"color: red;\"> The student's "+i+" ID is already saved </h2></body></html>");    
            }  
        }
    } catch (ParserConfigurationException | TransformerException d) {
        out.println("<html><body><h2style=\"color: red;\">Error: " + d.getMessage() + "</h2></body></html>");
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
