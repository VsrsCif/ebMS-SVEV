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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Jože Rihtaršič
 */
public class XSLTExtensions {

    private static final SimpleDateFormat msfDate = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat msfDateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static Object formatDate(String str) {
        if (str == null  || str.trim().isEmpty()){
            return null;
        }

        Date dt = org.apache.cxf.xjc.runtime.DataTypeAdapter.parseDateTime(str);
        return msfDate.format(dt);
    }
                         
    public static Object getZPPFictionDate(String str) {
        if (str == null  || str.trim().isEmpty()){
            return null;
        }
        Date dt = org.apache.cxf.xjc.runtime.DataTypeAdapter.parseDateTime(str);
        Calendar c = new GregorianCalendar();
        c.setTime(dt);
        c.add(Calendar.DAY_OF_MONTH, 15);
        return msfDate.format(c.getTime());
        
    }
    
    public static Object currentDate() {
        return msfDate.format(Calendar.getInstance().getTime());
    }
    
    public static Object currentDateTime() {
        return msfDateTime.format(Calendar.getInstance().getTime());
    }

}
