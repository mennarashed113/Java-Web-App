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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(urlPatterns = {"/sort_survlet"})
public class sort_survlet extends HttpServlet {
    
    private static void saveXmlToFile(Document doc, String filePath, PrintWriter out) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            // Write the transformed XML to the file
            transformer.transform(source, new StreamResult(fileWriter));
            out.println("Student data sorted successfully.<br><br><br>");
        } catch (IOException e) {
            out.println("Error writing to the file: " + e.getMessage());
        }
    }
    
    private void bubbleSort(List<Element> elements, String sortByAttribute, String order) {
        for (int i = 0; i < elements.size() - 1; i++) {
            for (int j = 0; j < elements.size() - i - 1; j++) {
                Element element1 = elements.get(j);
                Element element2 = elements.get(j + 1);
                String value1="";                                      
                String value2="";
                if(sortByAttribute.equals("ID")){
                    value1 = element1.getAttribute(sortByAttribute);
                    value2 = element2.getAttribute(sortByAttribute);
                }else{   
                    value1 = element1.getElementsByTagName(sortByAttribute).item(0).getTextContent();
                    value2 = element2.getElementsByTagName(sortByAttribute).item(0).getTextContent();
                }

                int comparisonResult = value1.compareTo(value2);

                if ("des".equalsIgnoreCase(order)) {
                    // Swap elements if in descending order
                    if (comparisonResult < 0) {
                        swap(elements, j, j + 1);
                    }
                } else {
                    // Swap elements if in ascending order
                    if (comparisonResult > 0) {
                        swap(elements, j, j + 1);
                    }
                }
            }
        }
    }

    private void swap(List<Element> elements, int i, int j) {
        Element temp = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, temp);
    }
    
    private void sortAndReturnXml(PrintWriter out,String sortByAttribute,String order,Document doc,String path)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("student");
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) node);
            }
        }

        bubbleSort(elements, sortByAttribute, order);

        Node parent = nodeList.item(0).getParentNode();
        for (Element element : elements) {
            parent.appendChild(element);
        }
        saveXmlToFile(doc,path,out);
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
        String att = request.getParameter("att");
        String order = request.getParameter("order");
        try{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc;
        try {
            doc = dBuilder.parse(new File("D:\\FCAI\\four sems1\\WebApplication1\\src\\java\\students.xml"));
           
            if (att != null && !att.isEmpty() && order != null) {
                sortAndReturnXml(out,att,order,doc,"D:\\FCAI\\four sems1\\WebApplication1\\src\\java\\students.xml");
               
                NodeList studentNodes = doc.getElementsByTagName("student");
                for (int i = 0; i < studentNodes.getLength(); i++) {
                   Element studentElement = (Element) studentNodes.item(i);
                   out.print("ID : "+studentElement.getAttribute("ID")+"<br>");
                    NodeList childNodes = studentElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if (childNodes.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            Element childElement = (Element) childNodes.item(j);
                            out.println(childElement.getTagName() + ": " + childElement.getTextContent()+"<br>");
                        }
                    }
                    out.println("<br><hr>");
                }
                
            } else {
                out.println("<error>Invalid parameters for sorting.</error>");
            }
        

        } catch (SAXException | IOException ex) {
            // Create a new document and root element if the XML file doesn't exist
            out.print("not found file");
        }   catch (TransformerException ex) {
                Logger.getLogger(sort_survlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }catch(ParserConfigurationException a){
        out.println("<html><body><h2>Error: " +a.getMessage() + "</h2></body></html>");
    }
    }


}
