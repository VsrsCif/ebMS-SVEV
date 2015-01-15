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

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Element;

/**
 *
 * @author Jože Rihtaršič
 */
public class XMLUtils {


     public static Object deserialize(Element elmnt, Class cls) throws JAXBException {
        final Unmarshaller um = JAXBContext.newInstance(cls).createUnmarshaller();
        return um.unmarshal(elmnt);
    }

    public static synchronized void deserialize(Source source, Result result, InputStream xsltSource) throws TransformerConfigurationException, JAXBException, TransformerException {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = factory.newTransformer(new StreamSource(xsltSource));
            transformer.transform(source, result);
    }
    
    public static synchronized void deserialize(InputStream source, Result result) throws TransformerConfigurationException, JAXBException, TransformerException {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = factory.newTransformer();
            transformer.transform(new StreamSource(source), result);
    }
    
    public static synchronized Object deserialize(InputStream source, InputStream xsltSource, Class cls) throws TransformerConfigurationException, JAXBException, TransformerException {       
        return deserialize(new StreamSource(source), xsltSource, cls);
    }

    
    public static synchronized Object deserialize(Source source, InputStream xsltSource, Class cls) throws TransformerConfigurationException, JAXBException, TransformerException {
        Object obj = null;
        JAXBContext jc = JAXBContext.newInstance(cls);

        if (xsltSource != null) {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = factory.newTransformer(new StreamSource(xsltSource));

            JAXBResult result = new JAXBResult(jc);
            transformer.transform( source, result);
            obj = result.getResult();
        } else {
            obj = jc.createUnmarshaller().unmarshal(source);
        }
        return obj;
    }

}
