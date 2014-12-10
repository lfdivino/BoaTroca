<%@ page language="java" import="java.io.*" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%       
// get the content type information from JSP Request Header
String contentType = request.getContentType();       
// content type should not be null and data passed from mulitpart/form-data is greater than // or equal to 0
if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
    DataInputStream in = new DataInputStream(request.getInputStream());
    // get length of the data content
    int formDataLength = request.getContentLength();
    byte dataBytes[] = new byte[formDataLength];
    int byteRead = 0;
    int totalBytesRead = 0;
    // read bytes while available
    while (totalBytesRead < formDataLength) {
        byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
        totalBytesRead += byteRead;
    }                                       
    String file = new String(dataBytes);               
    // get filename for saving               
    String saveFile = file.substring(file.indexOf("filename=\"") + 10);               
    saveFile = saveFile.substring(0, saveFile.indexOf("\n"));               
    saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,saveFile.indexOf("\""));               
    int lastIndex = contentType.lastIndexOf("=");
          // get boundary delimiter from header              
    String boundary = contentType.substring(lastIndex + 1, contentType.length());               
    int pos;               
    // locate start and end positions of file data within request                
    pos = file.indexOf("filename=\"");               
    pos = file.indexOf("\n", pos) + 1;               
    pos = file.indexOf("\n", pos) + 1;               
    pos = file.indexOf("\n", pos) + 1;               
    int boundaryLocation = file.indexOf(boundary, pos) - 4;               
    int startPos = ((file.substring(0, pos)).getBytes()).length;               
    int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
    
    // path of the folder
    File f = new File("pictures/" + saveFile);
    
    // create a new file with the same name as source and copy the file contents to it               
    FileOutputStream fout = new FileOutputStream(f);               
    fout.write(dataBytes, startPos, (endPos - startPos));               
    fout.flush();               
    fout.close(); 
    
}
 
%>
