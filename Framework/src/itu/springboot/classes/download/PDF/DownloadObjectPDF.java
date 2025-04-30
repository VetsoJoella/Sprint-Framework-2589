package itu.springboot.classes.download.PDF;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

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

}
