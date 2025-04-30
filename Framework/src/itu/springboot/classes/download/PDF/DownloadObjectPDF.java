package itu.springboot.classes.download.PDF;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponseWrapper;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder ;

import itu.springboot.classes.download.DownloadObject;

public class DownloadObjectPDF extends DownloadObject {
    
    String jspFile ; 

    public String getJspFile(){
        return jspFile ;
    }

    public void setJspFile(String jspFile) {
        this.jspFile = jspFile ;
    }

    public DownloadObjectPDF(){
        super();
    }

    public void convert(HttpServletRequest request, HttpServletResponse response) throws Exception{

        if(jspFile==null) return ;
        String htmlContent = renderJSPtoString(request, response, getJspFile());

        // 2. Convertir en PDF
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(htmlContent, null);
        builder.toStream(pdfOutputStream);
        builder.run();

        byte[] pdfData = pdfOutputStream.toByteArray();
        setBytes(pdfData);
        setContentType("application/pdf");
        // // 3. Envoyer en réponse
        // response.setContentType("application/pdf");
        // response.setHeader("Content-Disposition", "inline; filename=\"document.pdf\"");
        // response.setContentLength(pdfData.length);

        // OutputStream out = response.getOutputStream();
        // out.write(pdfData);
        // out.flush();

    }

    public String renderJSPtoString(HttpServletRequest request, HttpServletResponse response, String jspPath) throws Exception {
        
        StringWriter stringWriter = new StringWriter();
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            private final PrintWriter writer = new PrintWriter(stringWriter);
            
            @Override
            public PrintWriter getWriter() {
                return writer;
            }
        };

        request.getRequestDispatcher(jspPath).include(request, responseWrapper);
        return stringWriter.toString();
    }


}
