/*
 * Copyright 2015, Supreme Court Republic of Slovenia 
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by 
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and  
 * limitations under the Licence.
 */
package si.vsrs.cif.svev.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import si.vsrs.cif.svev.example.exception.FOPException;

/**
 *
 * @author Jože Rihtaršič
 */
public class FOPUtils {

    private static final Logger mlog = Logger.getLogger(FOPUtils.class);
    FopFactory mfopFactorory = null;
    String mTransformationFolder;
    File msfConfigFile;

    public FOPUtils(File configfile) {
        msfConfigFile = configfile;
    }

    private FopFactory getFopFactory() throws SAXException, IOException {
        if (mfopFactorory == null) {
            mfopFactorory = FopFactory.newInstance();
            if (msfConfigFile != null) {
                mfopFactorory.setUserConfig(msfConfigFile);
            }
        }
        return mfopFactorory;

    }

    public File generateVisualizationToFile(File inputRequest, InputStream xslt) throws FOPException {
        // get service  + "get vssl"
        String name = inputRequest.getName();
        String substring = name.substring(0, name.lastIndexOf("."));

        File outputDocument = new File(inputRequest.getParent(), substring + ".pdf");

        try (FileOutputStream fos = new FileOutputStream(outputDocument)) {

            Source src = new StreamSource(inputRequest);
            generateVisualization(src, fos, new StreamSource(xslt));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FOPUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputDocument;
    }

    public byte[] generateVisualization(File inputRequest, InputStream xslt) throws FOPException {
        // get service  + "get vssl"
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Source src = new StreamSource(inputRequest);
        generateVisualization(src, bos, new StreamSource(xslt));
        return bos.toByteArray();
    }

    public void generateVisualization(Source src, OutputStream out, Source xslt) throws FOPException {

        try {
            Fop fop = getFopFactory().newFop(MimeConstants.MIME_PDF, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslt);
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (IOException | SAXException | TransformerException ex) {
            String msg = "Error generating visualization";
            throw new FOPException(msg, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {

            }
        }
    }

}
