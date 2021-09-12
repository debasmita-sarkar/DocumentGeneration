package com.mavenir.thymeleaf.helper;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import java.io.IOException;
import java.util.Base64;

public class B64ImgReplacedElementFactory implements ReplacedElementFactory {

    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box,
            UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element e = box.getElement();
        if (e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        if (nodeName.equals("img")) {
            String attribute = e.getAttribute("src");
            FSImage fsImage;
            try {
                fsImage = buildImage(attribute, uac);
            } catch (BadElementException | IOException e1) {
                fsImage = null;
            }
            if (fsImage != null) {
                if (cssWidth != -1 || cssHeight != -1) {
                    fsImage.scale(cssWidth, cssHeight);
                }
                return new ITextImageElement(fsImage);
            }
        }
        return null;
    }

    protected FSImage buildImage(String srcAttr, UserAgentCallback uac)
            throws IOException, BadElementException {
        FSImage fsImage;
        if (srcAttr.startsWith("data:image/")) {
            String b64encoded = srcAttr.substring(
                    srcAttr.indexOf("base64,") + "base64,".length(),
                    srcAttr.length());
            byte[] decodedBytes = Base64.getDecoder()
                    .decode(b64encoded.getBytes());
            final Image image = Image.getInstance(decodedBytes);
            final int factor = ((ITextUserAgent) uac).getSharedContext()
                    .getDotsPerPixel();
            image.scaleAbsolute(image.getPlainWidth() * factor,
                    image.getPlainHeight() * factor);
            fsImage = new ITextFSImage(image);
        } else {
            fsImage = uac.getImageResource(srcAttr).getImage();
        }
        return fsImage;
    }

    @Override
    public void reset() {
        // do nothing
    }

    @Override
    public void remove(Element e) {
        // do nothing
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
        // do nothing
    }
}