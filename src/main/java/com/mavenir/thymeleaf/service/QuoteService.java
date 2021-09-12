package com.mavenir.thymeleaf.service;

import com.lowagie.text.pdf.BaseFont;
import com.mavenir.thymeleaf.helper.B64ImgReplacedElementFactory;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
@Slf4j
public class QuoteService {
    @Autowired
    private Environment environment;


    String processTemplate(String template, Map<String, String> keyValueMap) {
        log.info("Entered processTemplate()");
        String html = template;
        //html = populatePlaceholders(html, keyValueMap);
        Document document = Jsoup.parse(html);;
        String xhtml = parseIntoXHTML(document);
        xhtml = overrideSpecialChars(xhtml);
        log.debug("Exiting processTemplate()");
        return xhtml;
    }
    String populatePlaceholders(String html, Map<String, String> keyValueMap) {
        log.debug("Entered populatePlaceholders()");
        /* Replace placeholders with values in the html */
        for (String key : keyValueMap.keySet()) {
            String value = keyValueMap.get(key);
            html = html.replace(key, value);
        }
        log.debug("Exiting populatePlaceholders()");
        return html;
    }
    String parseIntoXHTML(Document document) {
        log.debug("Entered parseIntoXHTML()");

        /* Retrieve XHTML from Document object in form of a string */
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        String parsedHTML = document.html();
        log.debug("Exiting parseIntoXHTML()");
        return parsedHTML;
    }

    private void generatePDF(String xhtml) throws IOException {
        log.debug("Entered generatePDF()");
        log.debug("Environment variable: {}", this.environment);
        String fontsDirPath = this.environment.getProperty("fonts-dir-path");
        log.debug("fontsDirPath: {}", fontsDirPath);
        String fontsFilePath = fontsDirPath + File.separator + "Meiryo.ttf";
        log.debug("fontsFilePath: {}", fontsFilePath);
        ITextRenderer renderer = new ITextRenderer();
        //renderer.getFontResolver().addFont(fontsFilePath, BaseFont.IDENTITY_H,
          //  BaseFont.EMBEDDED);
        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext
            .setReplacedElementFactory(new B64ImgReplacedElementFactory());
        renderer.setDocumentFromString(xhtml);
        renderer.layout();
        OutputStream os = new FileOutputStream("D://Thyme.pdf");
        renderer.createPDF(os);
        log.debug("Exiting generatePDF()");

    }

    String overrideSpecialChars(String xhtml) {
        log.debug("Entered overrideSpecialChars()");
        xhtml = xhtml.replace("nbsp", "#160");
        xhtml = xhtml.replace("\\n", "<br/>");
        log.debug("Exiting overrideSpecialChars()");
        return xhtml;
    }

    public void createPDF(String html) throws IOException {
        log.debug("Entered createPDF()");
        String xhtml = processTemplate(html,null);
        generatePDF(xhtml
            );
        log.debug("Exiting createPDF()");
    }
}

