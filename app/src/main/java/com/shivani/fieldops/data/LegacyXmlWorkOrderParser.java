package com.shivani.fieldops.data;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** Demonstrates interoperability with a legacy XML work-order endpoint. */
public final class LegacyXmlWorkOrderParser {
    public List<WorkOrderDto> parse(InputStream input) throws Exception {
        List<WorkOrderDto> result = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(input, "UTF-8");
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG && "workOrder".equals(parser.getName())) {
                result.add(new WorkOrderDto(
                    parser.getAttributeValue(null, "id"),
                    parser.getAttributeValue(null, "title"),
                    parser.getAttributeValue(null, "site"),
                    parser.getAttributeValue(null, "priority"),
                    parser.getAttributeValue(null, "status"),
                    parser.getAttributeValue(null, "updatedAt")
                ));
            }
            event = parser.next();
        }
        return result;
    }
}
